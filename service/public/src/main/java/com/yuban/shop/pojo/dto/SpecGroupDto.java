package com.yuban.shop.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.yuban.shop.pojo.entity.categoryConfig.SpecItem;
import lombok.Data;

import java.util.List;

@Data
public class SpecGroupDto {
    @TableId(type = IdType.AUTO)
    private Long groupId;
    private Long catId;
    private String catName;

    private List<SpecItem> groupConfig ;
}
