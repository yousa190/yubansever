package com.yuban.service;

import com.yuban.pojo.UserData;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService {

    Integer count_name(String user_name);

    Integer count_email(String userEmail);

    boolean sign(String userName, String emaill, String pwd);

    boolean check_name(String name);

    boolean check_email(String name);


    UserData getByName(String name,String pwd);

    UserData getByemail(String name,String pwd);
}
