package com.enation.pangu.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.enation.pangu.annotation.Create;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;


/**
 * @author gy
 * @version v1.0
 * @date 2021/2/24
 * @since v7.2.0
 * 基本分组  针对机器.部署等进行分组
 */
public class BaseGroup {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分组名称
     */
    @NotBlank(message = "分组名称不能为空")
    @Length(max = 30,message = "分组名称不能超过30个字符")
    private String name;

    /**
     * 分组类型
     */
    @NotBlank(message = "分组类型不能为空",groups = Create.class)
    private  String type;

    /**
     * 创建时间
     */
    private Long addTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        BaseGroup baseGroup = (BaseGroup) o;

        return new EqualsBuilder()
                .append(id, baseGroup.id)
                .append(name, baseGroup.name)
                .append(type, baseGroup.type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(type)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "BaseGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
