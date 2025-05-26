package com.yuban.pojo.SpecGroup;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
//自动结果映射
@TableName(value = "spec_group",autoResultMap = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SpecGroup {
    @TableId(type = IdType.AUTO)
    private Long groupId;
    private Long catId;
    private String catName;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<SpecItem> groupConfig ;
}