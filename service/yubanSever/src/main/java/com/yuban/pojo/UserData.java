package com.yuban.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private Integer user_id;

    private String user_name;


    private String email;

    private String img;

    private Date entry_date;

    private Integer level;

}
