package com.enation.pangu.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.enation.pangu.constant.PluginConstant;
import com.enation.pangu.domain.*;
import com.enation.pangu.enums.ExecutorEnum;
import com.enation.pangu.enums.PluginPathTypeEnum;
import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.mapper.PluginMapper;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.model.ResultCode;
import com.enation.pangu.service.PluginManager;
import com.enation.pangu.service.PluginNotExistException;
import com.enation.pangu.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * 插件管理实现
 *
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/12/29
 */
@Service
public class PluginManagerImpl implements PluginManager {
    /**
     * 插件的扩展名
     */
    private static final String PLUGIN_EXT = ".yml";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    PluginMapper pluginMapper;

    @Value("${pangu.synchronizer.url}")
    private String synchronizerUrl;

    @Override
    public void syncFormFileSystem() {

        //情况插件表
        QueryWrapper<Plugin> wrapper = new QueryWrapper<>();
        pluginMapper.delete(wrapper);

        //执行器
        List<Plugin> executorList = listFromFileSystem(PluginType.executor);

        //检查器
        List<Plugin> checkerList = listFromFileSystem(PluginType.checker);
        executorList.addAll(checkerList);
        int sort = 1;
        for (Plugin plugin : executorList) {
            wrapper = new QueryWrapper<>();
            wrapper.eq("kind", plugin.getKind());
            wrapper.eq("plugin_id", plugin.getPluginId());
            int count = pluginMapper.selectCount(wrapper);
            if (count > 0) {
                throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(), "插件id:" + plugin.getPluginId() + "存在重复。");
            }
            plugin.setSequence(sort++);
            pluginMapper.insert(plugin);
        }

    }

    @Override
    public List<Plugin> list(PluginType pluginType, String status) {
        QueryWrapper<Plugin> wrapper = new QueryWrapper<>();
        wrapper.eq("kind", pluginType.name());
        wrapper.eq(status != null, "status", status);
        wrapper.orderByAsc("sequence");
        List<Plugin> pluginList = pluginMapper.selectList(wrapper);


        return pluginList;
    }

    @Override
    public Boolean editPluginById(Plugin plugin) {
        if (!StringUtil.isEmpty(plugin.getPluginId())) {
            int result = this.pluginMapper.updateById(plugin);
            return result > 0 ? true : false;
        }
        return false;
    }

    @Override
    public Plugin getById(String pluginId) {
        return pluginMapper.selectById(pluginId);
    }

    @Override
    public ExecutorVO parsePlugin(String pluginId, PluginType pluginType, Map env) throws PluginNotExistException {
        if (ExecutorEnum.write_config.executorId().equals(pluginId)) {
            return initWriteConfigExecutor();
        }
        if (ExecutorEnum.ssh.executorId().equals(pluginId)) {
            return initSSHExecutor();
        }

        QueryWrapper<Plugin> wrapper = new QueryWrapper<>();
        wrapper.eq("kind", pluginType.name());
        wrapper.eq("plugin_id", pluginId);
        Plugin plugin = pluginMapper.selectOne(wrapper);

        if (plugin == null) {
            throw new PluginNotExistException("插件："+pluginId + "不存在");
        }

        //插件yml文件路径
        String pluginPath = plugin.getPath();

        //插件路径类型
        String pathType = plugin.getPathType();

        //加载执行器插件yml
        String script = ScriptUtil.renderScript(pluginPath, env, pluginType.getFolder(), pathType);
        Yaml yaml = new Yaml();
        Map map = yaml.load(script);

        return parsePlugin(map);
    }

    @Override
    public void insert(Plugin plugin) {
        pluginMapper.insert(plugin);
    }

    @Override
    public Plugin getModel(String pluginId, PluginType pluginType) {
        return new QueryChainWrapper<>(pluginMapper).eq("plugin_id", pluginId).eq("kind", pluginType.name()).one();
    }

    @Override
    public void installPlugin(Map<String, String> pluginMap) {
        String fileName = pluginMap.get("fileName");
        boolean isfile = true;
        if (fileName.indexOf(".") == -1) {
            //如果是文件夹
            isfile = false;
        }
        String metadataKind = pluginMap.get("metadataKind");
        String pluginFolder = PluginType.valueOf(metadataKind).getFolder();
        String pathfollow = PathUtil.getRootPath();
        String pluginsDir = pathfollow + File.separator + PluginConstant.PLUGINS_PATH_PROFIX + File.separator + pluginFolder;
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File path = new File(pluginsDir);
        if (!path.exists()) {
            path.mkdirs();
        }
        File f = null;
        String filePath = pluginsDir + File.separator + fileName;
        if (isfile) {
            f = new File(filePath);
        } else {
            f = new File(filePath + "_" + System.currentTimeMillis() / 1000 + ".zip");
        }
        try {
            urlfile = new URL(synchronizerUrl + "/git/file/download/plugin?fileName=" + fileName + "&pluginType=" + metadataKind);
            httpUrl = (HttpURLConnection) urlfile.openConnection();
            httpUrl.connect();
            InputStream inputStream = httpUrl.getInputStream();
            bis = new BufferedInputStream(inputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            bos = new BufferedOutputStream(fileOutputStream);
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            bos.close();
            bis.close();
            httpUrl.disconnect();
            if (!isfile) {
                //如果是文件夹，下载下来的是压缩文件，要解压文件
                ZipUtil.unZipUncompress(f.getPath(), pluginsDir);

                //解压后，找到该文件夹下面的yml文件
                File plugin = this.findPluginYml(new File(filePath));
                String ymlFileName = plugin.getName();
                filePath = File.separator + PluginConstant.PLUGINS_PATH_PROFIX + File.separator + pluginFolder + File.separator + fileName + File.separator + ymlFileName;
            } else {
                filePath = File.separator + PluginConstant.PLUGINS_PATH_PROFIX + File.separator + pluginFolder + File.separator + fileName;
            }

            //插件入库
            insertPluginIntoDb(pluginMap, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public WebPage findPluginList(String key, String metadataKind, int pageNo, int pageSize) {
        StringBuffer stringBuffer = new StringBuffer(synchronizerUrl);
        stringBuffer.append("/git/file/all?");
        try {
            stringBuffer.append("key=").append(key == null ? "" : URLEncoder.encode(key, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        stringBuffer.append("&metadataKind=").append(metadataKind == null ? "" : metadataKind);
        stringBuffer.append("&pageNum=").append(pageNo);
        stringBuffer.append("&pageSize=").append(pageSize);
        String res = HttpUtils.doGet(stringBuffer.toString());
        JSONObject jsonObject = JSONObject.fromObject(res);
        WebPage webPage = new WebPage();
        webPage.setData(formatData(jsonObject.getJSONArray("list"), metadataKind));
        webPage.setRecordsTotal(jsonObject.getLong("total"));
        return webPage;
    }

    private List formatData(JSONArray list, String pluginType) {

        Map<String, Plugin> pluginMap = new HashMap<>();
        List<Plugin> pluginList = this.list(PluginType.valueOf(pluginType), null);
        for (Plugin plugin : pluginList) {
            pluginMap.put(plugin.getPluginId(), plugin);
        }


        for (Object o : list) {
            JSONObject jsonObject = (JSONObject) o;

            String fileName = jsonObject.getString("fileName");
            String pluginId = fileName.split("[.]")[0];

            jsonObject.put("pluginId", pluginId);

            Plugin plugin = pluginMap.get(pluginId);
            if (plugin == null) {
                jsonObject.put("isInstall", 0);
            } else {
                jsonObject.put("isInstall", 1);
            }

        }
        return JSON.parseArray(JSON.toJSONString(list), Map.class);
    }

    private void insertPluginIntoDb(Map<String,String> pluginMap, String filePath) {
        String pluginType = pluginMap.get("metadataKind");

        Plugin plugin = this.getModel(pluginMap.get("pluginId"), PluginType.valueOf(pluginType));

        if (plugin != null) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(), "插件已存在");
        }

        plugin = new Plugin();
        plugin.setAuthor(pluginMap.get("metadataAuthor"));
        plugin.setAuthorUrl(pluginMap.get("metadataAuthorUrl"));
        plugin.setDesc(pluginMap.get("metadataDesc"));
        plugin.setKind(pluginMap.get("metadataKind"));
        plugin.setName(pluginMap.get("metadataName"));
        plugin.setPluginId(pluginMap.get("pluginId"));
        plugin.setPath(filePath);
        plugin.setPathType(PluginPathTypeEnum.absolute.value());
        this.insert(plugin);
    }

    public ExecutorVO parsePlugin(Map map) {

        ExecutorVO executorVO = new ExecutorVO();

        //meta解析
        Map metadataMap = (Map) map.get("metadata");
        Plugin pluginMetaDataVO = parsePluginMetaData(metadataMap);
        executorVO.setMetadata(pluginMetaDataVO);


        //command解析
        Map commandMap = (Map) map.get("command");
        if (commandMap != null) {
            executorVO.setType((String) commandMap.get("type"));
            executorVO.setExecList((List<String>) commandMap.get("exec"));
        }


        //copy解析
        List<Map> copyMapList = (List<Map>) map.get("copy");
        if (copyMapList != null) {
            List<Copy> copyList = new ArrayList<>();
            for (Map copyMap : copyMapList) {
                Copy copy = new Copy();
                copy.setSource((String) copyMap.get("source"));
                copy.setTarget((String) copyMap.get("target"));
                Boolean parseEnv = (Boolean) copyMap.get("parse_env");

                //是否解析变量，默认是true
                if (parseEnv == null) {
                    parseEnv = true;
                }
                copy.setParseEnv(parseEnv);

                copyList.add(copy);
            }

            executorVO.setCopyList(copyList);
        }

        //config解析
        Map configMap = (Map) map.get("config");

        if (configMap != null) {
            PluginConfigVO pluginConfigVO = parsePluginConfig(configMap);

            //检测器需要加入一个检测时长配置项
            if (pluginMetaDataVO.getKind().equals(PluginType.checker.name())) {
                ExecutorConfigItemVO item = new ExecutorConfigItemVO();
                item.setTitle("检测时长(秒数，默认120秒)");
                item.setName("duration");
                item.setType("input");
                item.setValue("120");
                item.setHtmlcss("width:200px");
                pluginConfigVO.getItemList().add(item);
            }

            executorVO.setConfig(pluginConfigVO);
        }

        //插件检测方法
        String checkfun = (String) map.get("checkfun");

        if (!StringUtil.isEmpty(checkfun)) {
            executorVO.setCheckFun(checkfun);
        }


        return executorVO;
    }

    private PluginConfigVO parsePluginConfig(Map configMap) {
        List<Map> itemMapList = (List<Map>) configMap.get("items");
        List<ExecutorConfigItemVO> itemList = new ArrayList<>();
        for (Map itemMap : itemMapList) {
            ExecutorConfigItemVO item = new ExecutorConfigItemVO();
            item.setTitle((String) itemMap.get("title"));
            item.setName((String) itemMap.get("name"));
            item.setType((String) itemMap.get("type"));
            item.setHtmlcss((String) itemMap.get("htmlcss"));
            itemList.add(item);
        }

        PluginConfigVO executorConfigVO = new PluginConfigVO();
        executorConfigVO.setItemList(itemList);
        return executorConfigVO;
    }

    private Plugin parsePluginMetaData(Map metadataMap) {
        Plugin pluginMetaDataVO = new Plugin();
        pluginMetaDataVO.setName(metadataMap.get("name").toString());
        pluginMetaDataVO.setPluginId(metadataMap.get("id").toString());
        pluginMetaDataVO.setDesc(metadataMap.get("desc").toString());
        pluginMetaDataVO.setAuthor(metadataMap.get("author").toString());
        pluginMetaDataVO.setAuthorUrl(metadataMap.get("author_url").toString());
        pluginMetaDataVO.setKind(metadataMap.get("kind").toString());

        return pluginMetaDataVO;
    }

    private ExecutorVO initSSHExecutor() {
        ExecutorVO executorVO = new ExecutorVO();
        PluginConfigVO executorConfig = new PluginConfigVO();
        ExecutorConfigItemVO executorConfigItem = new ExecutorConfigItemVO();
        executorConfigItem.setTitle("要执行的命令");
        executorConfigItem.setName("commands");
        executorConfigItem.setType("textarea");
        executorConfigItem.setHtmlcss("height:80px");
        executorConfig.setItemList(Arrays.asList(executorConfigItem));
        executorVO.setConfig(executorConfig);
        return executorVO;
    }


    private ExecutorVO initWriteConfigExecutor() {
        ExecutorVO executorVO = new ExecutorVO();
        PluginConfigVO pluginConfigVO = new PluginConfigVO();
        pluginConfigVO.setItemList(new ArrayList<>());
        executorVO.setConfig(pluginConfigVO);
        return executorVO;
    }

    private List<Plugin> initSpecialExecutor() {
        //ssh执行器
        Plugin sshExecutor = new Plugin();
        sshExecutor.setPluginId(ExecutorEnum.ssh.executorId());
        sshExecutor.setName(ExecutorEnum.ssh.des());
        sshExecutor.setDesc("ssh命令");
        sshExecutor.setAuthor("javashop");
        sshExecutor.setKind(PluginType.executor.name());
        sshExecutor.setAuthorUrl("https://www.javamall.com.cn");

        //配置文件执行器
        Plugin writeConfigExecutor = new Plugin();
        writeConfigExecutor.setPluginId(ExecutorEnum.write_config.executorId());
        writeConfigExecutor.setName(ExecutorEnum.write_config.des());
        writeConfigExecutor.setAuthor("javashop");
        writeConfigExecutor.setDesc("配置文件执行器");
        writeConfigExecutor.setKind(PluginType.executor.name());
        writeConfigExecutor.setAuthorUrl("https://www.javamall.com.cn");

        return new ArrayList<>(Arrays.asList(sshExecutor, writeConfigExecutor));
    }


    /**
     * 由文件系统中读取某类插件
     *
     * @param pluginType
     * @return
     */
    List<Plugin> listFromFileSystem(PluginType pluginType) {
        String separator = File.separator;
        //执行器目录
        String executorDir = System.getProperty("user.dir") + separator + "src" + separator + "main" + separator + "resources" + separator + pluginType.name();

        List<Plugin> pluginList = new ArrayList<>();

        //遍历执行器目录下的每一个文件
        File dir = new File(executorDir);

        File[] files = dir.listFiles(child -> {
            //只需要处理文件夹和yml文件
            if (child.isDirectory() || child.getName().endsWith(PLUGIN_EXT)) {
                return true;
            }
            return false;
        });

        File pluginFile = null;
        for (File file : files) {
            String pluginPath = "";
            //文件夹要识别文件夹里面的yml
            if (file.isDirectory()) {
                pluginFile = findPluginYml(file);
                pluginPath = "/" + file.getName() + "/" + pluginFile.getName();
            } else {
                //直接就是yml文件
                pluginFile = file;
                pluginPath = "/" + file.getName();
            }

            if (file == null) {
                continue;
            }


            //拿到解析后的参数
            Yaml yml = new Yaml();
            try {
                Map map = yml.load(new FileInputStream(pluginFile));
                Plugin plugin = parsePlugin(map).getMetadata();
                plugin.setKind(pluginType.name());
                plugin.setPath(pluginPath);

                //放入集合中返回给前端
                pluginList.add(plugin);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }


        return pluginList;
    }


    /**
     * 查找某个文件夹下的yml文件
     *
     * @param dir
     * @return
     */
    @Override
    public File findPluginYml(File dir) {

        File[] ymlFiles = dir.listFiles((dir1, name) -> {
            if (name.endsWith(PLUGIN_EXT)) {
                return true;
            }
            return false;
        });

        if (ymlFiles.length == 0) {
            return null;
        } else {
            return ymlFiles[0];
        }
    }

    @Override
    public Boolean sort(Integer sourceSequence, Integer targetSequence, String kind) {
        //由sequence排到newSequence
        QueryWrapper wrapper = new QueryWrapper<Plugin>().eq("kind", kind).last(" limit " + sourceSequence + ",1").orderByAsc("sequence");
        Plugin sourceExecutor = pluginMapper.selectOne(wrapper);

        wrapper = new QueryWrapper<Plugin>().eq("kind", kind).last(" limit " + targetSequence + ",1").orderByAsc("sequence");
        Plugin targetExecutor = pluginMapper.selectOne(wrapper);

        pluginMapper.increaseSort(targetExecutor.getSequence(), kind);
        sourceExecutor.setSequence(targetExecutor.getSequence());

        pluginMapper.updateById(sourceExecutor);
        return true;
    }

}
