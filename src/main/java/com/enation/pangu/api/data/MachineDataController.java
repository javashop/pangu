package com.enation.pangu.api.data;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.enums.SecretKeyEnum;
import com.enation.pangu.model.*;
import com.enation.pangu.mapper.MachineTagMapper;
import com.enation.pangu.service.MachineManager;
import com.enation.pangu.service.SecretKeyManager;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.ssh.SshClientFactory;
import com.enation.pangu.utils.PageConvert;
import com.enation.pangu.utils.ParseException;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author shenyw
 * @create 2020-10-31-14:38
 */
@RestController
@RequestMapping("/data/machines")
public class MachineDataController {
    @Autowired
    private MachineManager machineManager;
    @Autowired
    private MachineTagMapper machineTagMapper;
    @Autowired
    private SecretKeyManager secretKeyManager;

    /**
     * 添加机器
     */
    @PostMapping()
    public String addMachine(Machine machine) {

        Machine machine1 = machineManager.checkMachine(machine);
        if (null == machine1) {

            machineManager.addMachine(machine);
            return "success";
        } else {
            if (machine1.getName().equals(machine.getName())) {
                throw new ServiceException(ResultCode.PARAMETER_CHECK_ERROR.getCode(),"名称已存在");
            }
        }
        return null;
    }

    /**
     * 编辑机器
     */
    @PutMapping()
    public void editMachine(Machine machine) {

        machineManager.editMachine(machine);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public void deleteMachine(@PathVariable Long id) {

        machineManager.deleteMachine(id);
    }


    /**
     * 查询列表
     */
    @GetMapping()
    public WebPage list(int pageNo, int pageSize) {
        WebPage list = machineManager.list(pageNo, pageSize);
        return list;
    }

    /**
     * 查询list列表
     */
    @GetMapping("/listVo")
    public WebPage<MachineVO> listVo(int pageNo, int pageSize, Long groupId) {
        IPage<MachineVO> machineVOIPage = machineTagMapper.selelctMachineTag(new Page<>(pageNo, pageSize), groupId);
        WebPage convert = PageConvert.convert(machineVOIPage);
        return convert;
    }

    /**
     * 查询
     */
    @GetMapping("/{id}")
    public Machine selectOne(@PathVariable Long id) {
        return machineManager.selectOne(id);
    }


    @PostMapping("/checker")
    public String checkForm(Machine properties) {
        if (SecretKeyEnum.publickey.name().equals(properties.getAuthType())) {
            if (StringUtil.isEmpty(properties.getSecretkeyId())) {
                throw new ServiceException(ResultCode.PARAMETER_CHECK_ERROR.getCode(), "请选择密钥");
            }
            //查出私钥
            SecretKey secretKey = secretKeyManager.selectById(Long.parseLong(properties.getSecretkeyId()));
            String privateKey = secretKey.getPrivateKey();
            properties.setPrivateKey(privateKey);
        }
        if (SecretKeyEnum.password.name().equals(properties.getAuthType()) && StringUtil.isEmpty(properties.getPassword())) {
            throw new ServiceException(ResultCode.PARAMETER_CHECK_ERROR.getCode(), "请填写密码!");
        }
        SshClientFactory.createSsh(properties);
        return "success";
    }


}
