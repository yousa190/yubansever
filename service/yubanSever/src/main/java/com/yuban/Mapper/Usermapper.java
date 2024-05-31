package com.yuban.Mapper;


import com.yuban.pojo.UserData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface Usermapper  {

    @Select("select count(user_id) from yubandata.user where user_name=#{user_name} ")
    Integer count_name(String user_name);

    @Select("select count(user_id) from yubandata.user where email=#{user_email} ")
    Integer count_email(String user_email);

    @Select("select user_id, user_name, email,pwd, gender, img, entry_date, level from yubandata.user where user_name=#{name} and pwd=#{pwd}")
    UserData get_msgByname(String  name, String pwd);

    @Select("select user_id, user_name, email,pwd, gender, img, entry_date, level  from yubandata.user where email=#{name} and pwd=#{pwd}")
    UserData get_msgByemail(String name, String
            pwd);
    @Insert("insert into yubandata.user(user_name,email,pwd) values(#{userName},#{emaill},#{pwd})")
    boolean sign(String userName, String emaill, String pwd);

    @Select("select * from yubandata.user where user_name=#{name} ")
    boolean check_name(String name);

    @Select("select * from yubandata.user where email=#{name}")
    boolean check_email(String name);

}
