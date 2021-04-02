package com.enation.pangu.api.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.pangu.mapper.MachineTagMapper;
import com.enation.pangu.model.MachineTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 机器标签关联数据控制器
 *
 * @author shenyw
 * @create 2020-11-12-8:54
 */
@RestController
@RequestMapping("/data/machinetags")
public class MachineTagDataController {
    @Autowired
    private MachineTagMapper machineTagMapperr;

    /**
     * 查询machine_tag列表
     */
    @GetMapping()
    public List<MachineTag> list() {
        return machineTagMapperr.selectList(new QueryWrapper<MachineTag>().orderByAsc("id"));
    }

    /**
     * 按照机器id查询标签
     */
    @GetMapping("/tags/{id}")
    public  String selectTagById(@PathVariable("id") Long id){
        String str="";
        QueryWrapper<MachineTag> query = new QueryWrapper<MachineTag>().eq("machine_id", id);
        List<MachineTag> machineTags = machineTagMapperr.selectList(query);
        for (MachineTag machineTag : machineTags) {
            str+=machineTag.getTagId().toString()+",";
        }
        return str;
    }

    @PostMapping()
    public void add(Long machineId, String[] tagId) {
        // 清空关联表
        QueryWrapper<MachineTag> query = new QueryWrapper<>();
        query.eq("machine_id", machineId);
        machineTagMapperr.delete(query);
        // 非空判断
        if (tagId != null && tagId.length > 0) {
            machineTagMapperr.insertMT(machineId, tagId);
        }

    }

    @PutMapping("/{id}")
    public String edit(MachineTag mt) {

        machineTagMapperr.updateById(mt);
        return "ok";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {

        machineTagMapperr.deleteById(id);
        return "ok";
    }
}
