package com.yuban.entity.origin;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("sys_user")
public class UserData {

    @TableId(type= IdType.AUTO)
    private Integer userId; // 用户唯一标识

    private String username; // 登录账号（唯一）
    private String passwordHash; // 加密后的密码
    private String email; // 验证邮箱（唯一）
    private String phone; // 联系电话
    private Byte sex; // 性别
    private String avatar; // 头像地址
    private Date lastLogin; // 最后登录时间
//    private Date createdAt; // 创建时间
    private Date updatedAt; // 更新时间
    private Integer level;  //用户权限

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
