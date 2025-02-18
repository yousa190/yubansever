package com.yuban.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class UserData {

    @TableId(type= IdType.AUTO)
    private Integer userId;

    private String userName;

    private String email;

    private String pwd;

    private Integer gender;

    private String img;

    private LocalDate entryDate;

    private Integer level;

    public UserData(String user_name, String pwd, String email) {
        this.userName = user_name;
        this.pwd = pwd;
        this.email = email;
    }

}
