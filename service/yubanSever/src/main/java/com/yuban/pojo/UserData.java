package com.yuban.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private Integer id;

    private String username;

    private String emaill;

    private String pwd;

    private short gender;

    private String image;

}
