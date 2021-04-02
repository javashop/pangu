package com.enation.pangu;

import com.enation.pangu.mapper.EnvVarMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 环境变量相关单元测试
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/11/25
 */

@RunWith(SpringRunner.class)
@SpringBootTest()
@ComponentScan("com.enation.pangu")
public class EnvVarsTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EnvVarMapper envVarMapper;

    @Test
    public void testGroupVars() {
        List list = envVarMapper.groupVars(1L);
        System.out.println(list);
    }
}
