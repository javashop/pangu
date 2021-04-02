package com.enation.pangu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.enation.pangu.model.Machine;
import com.enation.pangu.model.MachineTag;
import com.enation.pangu.model.MachineVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author shenyw
 * @create 2020-11-11-14:23
 */

public interface MachineTagMapper extends BaseMapper<MachineTag> {
    /**
     * 查询分页列表
     */
    IPage<MachineVO> selelctMachineTag(IPage page, @Param("groupId") Long groupId);

    /**
     * 根据标签ID查询列表
     */
    List<Machine> selelctMachineByTagId(Long tagId);


    List<String> selectTagsById(@Param("machineId") Long machineId);


    /**
     * 添加批量machine_tag
     */
    void insertMT(@Param("mid") Long mid, @Param("tags") String[] tags);

}
