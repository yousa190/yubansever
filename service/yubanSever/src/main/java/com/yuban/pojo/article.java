package com.yuban.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("articles")
public class article {

    private Integer artId;

    private Integer userId;

    private Date pubDate;

    private String title;

    private String content;

    private Integer good;

    private Integer view;

    private Integer comments;
}
