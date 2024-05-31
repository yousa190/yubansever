package com.yuban.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
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
