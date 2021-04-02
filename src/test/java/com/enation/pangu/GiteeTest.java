package com.enation.pangu;

import com.enation.pangu.domain.GiteeparamVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GiteeTest {

    //读取git仓库指定目录下文件
    @Test
    public void test() {
        Map<String, String> map = new HashMap<>();
        //仓库的拥有者(账号或个人空间地址)
        map.put("owner", "zhanghao666720");
        //仓库名
        map.put("repo", "kubernetes");
        //仓库下的具体路径
        map.put("path", "/api");
        //私人令牌(请登录gitee个人中心获取)
        map.put("access_token", "d2ae00761181e05ef9b1191c7980aa6c");
        //分支
        map.put("ref", "master");
        //System.out.println("参数："+map);
        String url = "https://gitee.com/api/v5/repos/" + map.get("owner")
                + "/" + map.get("repo") + "/contents/" + map.get("path") + "?access_token="
                + map.get("access_token") + "&ref=" + map.get("ref");
        //调gitee-api返回的最终结果
        String result = null;
        // 设置HTTP请求参数
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (ClientProtocolException e) {
            System.out.println("http接口调用异常：url is::" + url + e);
        } catch (IOException e) {
            System.out.println("http接口调用异常：url is::" + url + e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                System.out.println("http接口调用异常：url is::" + url + e);
            }
        }

        //将gitee-api返回的结果转为集合遍历取出
        ArrayList<Map> params = this.jsonToObject(result,ArrayList.class);
        List<GiteeparamVo> giteeparamVos = new ArrayList<>();
        GiteeparamVo giteeparamVo = new GiteeparamVo();
        for (Map param : params){
            giteeparamVo.setType(param.get("type").toString());
            giteeparamVo.setName(param.get("name").toString());
            giteeparamVo.setPath(param.get("path").toString());
            giteeparamVo.setDownload_url(param.get("download_url").toString());
            //下载报403,官方api未提供下载功能
           this.downloadFromUrl(giteeparamVo.getDownload_url());

            //添加到集合中
            giteeparamVos.add(giteeparamVo);
        }
    }


    public static String downloadFromUrl(String url) {
        String dir = "C:\\\\Users\\\\Administrator\\\\Desktop\\\\耶巴蒂\\\\莱维贝贝\\\\图片\\\\测试下载文件\\\\\"";
        try {
            URL httpurl = new URL(url);
            String fileName = getFileNameFromUrl(url);
            System.out.println(fileName);
            File f = new File(dir + fileName);
            FileUtils.copyURLToFile(httpurl, f);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fault!";
        }
        return "Successful!";
    }

    public static String getFileNameFromUrl(String url) {
        String name = new Long(System.currentTimeMillis()).toString() + ".X";
        int index = url.lastIndexOf("/");
        if (index > 0) {
            name = url.substring(index + 1);
            if (name.trim().length() > 0) {
                return name;
            }
        }
        return name;
    }

    //解析json
    public static <T> T jsonToObject(String jsonData,Class<T> clz){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonData,clz);
        } catch (IOException e) {
            System.out.println("转换Object 异常");
        }
        return null;
    }

}
