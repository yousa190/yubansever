package com.yuban.shop.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 用户实体类
 * 映射数据库中的sys_user表，包含用户的基本信息和状态
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("sys_user")
public class UserData {

    @TableId(type = IdType.AUTO)
    private Integer userId; // 用户唯一标识

    private String username; // 登录账号（唯一）
    private String passwordHash; // 加密后的密码
    private String email; // 验证邮箱（唯一）
    private String phone; // 联系电话
    private Integer sex; // 性别：1-男，0-女
    private String avatar; // 头像地址
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private Date lastLogin; // 最后登录时间
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private Date createdAt; // 创建时间
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private Date updatedAt; // 更新时间
    
    private Integer level; // 用户权限等级：1-普通用户，2-管理员，3-超级管理员
    private Integer isDel; // 软删除标记：0-未删除，1-已删除

    private Integer status; // 启用禁用：1-禁用，0-启用

    public UserData(String username, String passwordHash, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public UserData(String username, String email) {
        this.username = username;
        this.email = email;
    }
}