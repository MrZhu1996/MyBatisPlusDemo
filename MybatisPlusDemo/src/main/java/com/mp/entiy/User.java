package com.mp.entiy;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;
    //主键,主键生成策略，局部策略优于全局策略
    @TableId(type = IdType.UUID)
    private Long id;
    //姓名
    @TableField(condition = SqlCondition.LIKE)
    private String name;
    //年龄
    @TableField(condition = "%s&lt;#{%s}")
    private Integer age;
    //邮箱
    private String email;
    //直属上级
    private Long managerId;
    //创建时间
    private LocalDateTime createTime;
    //备注
    @TableField(exist = false)
    private String remark;

}
