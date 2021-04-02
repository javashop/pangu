package com.enation.pangu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.pangu.mapper.StepMapper;
import com.enation.pangu.model.Step;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 拍讯测试
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2021/2/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
@ComponentScan("com.enation.pangu")
public class SortTest {

    @Autowired
    StepMapper stepMapper;

    @Test
    public void test() {
        int sequence = 3;
        int newSequence = 1;
        long deploymentId=6;
        QueryWrapper wrapper = new QueryWrapper<Step>().eq("deployment_id", deploymentId).last(" limit " + sequence + ",1").orderByAsc("sequence");
        Step sourceStep = stepMapper.selectOne(wrapper);
        System.out.println(sourceStep);

        wrapper = new QueryWrapper<Step>().eq("deployment_id", deploymentId).last(" limit " + newSequence + ",1").orderByAsc("sequence");
        Step targetStep = stepMapper.selectOne(wrapper);
        System.out.println(targetStep);


//        QueryWrapper updateWrapper = new QueryWrapper<Step>().ge("sequence", targetStep.getSequence()).eq("deployment_id", deploymentId);

        stepMapper.increaseSort(targetStep.getSequence(), deploymentId);
        sourceStep.setSequence(targetStep.getSequence());
        
        stepMapper.updateById(sourceStep);

    }
}
