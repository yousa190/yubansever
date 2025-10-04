package com.yuban.shop.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.pojo.entity.Result;
import com.yuban.shop.pojo.entity.categoryConfig.SpecGroup;
import com.yuban.shop.service.CategoryConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mallConf")
@Tag(name = "后台分类参数相关接口", description = "分类配置管理相关接口")
public class CategoryConfigController {

    @Autowired
    private CategoryConfigService categoryConfigService;

    @Operation(summary = "获取分类配置列表", description = "分页获取分类配置列表，支持按分类名称搜索")
    @GetMapping("/list")
    public Result getList(
            @Parameter(description = "分类名称，支持模糊搜索") @RequestParam(required = false,defaultValue = "") String catName,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int limit) {
        Page<SpecGroup> pageData = categoryConfigService.getList( catName, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("list", pageData.getRecords());
        response.put("count", pageData.getTotal());
        response.put("totalPage", pageData.getPages());
        response.put("currentPage", page);
        return Result.success(response);
    }

    @Operation(summary = "根据分类ID获取配置", description = "根据分类ID获取对应的分类配置信息")
    @GetMapping("/getone")
    public Result getone(
            @Parameter(description = "分类ID", required = true) @RequestParam Long catId ) {
        try {
            SpecGroup specGroup = categoryConfigService.getByCatId(catId);
            log.info("DB数据：{}", specGroup);
            return specGroup != null ?
                    Result.success(specGroup):
                    Result.error("未找到分类配置");
        } catch (IllegalArgumentException e) {
            return Result.error( e.getMessage());
        }
    }

    @Operation(summary = "保存或更新分类配置", description = "智能保存分类配置，如果ID存在则更新，否则新增")
    @PostMapping("/edit")
    public Result smartSave(
            @Parameter(description = "分类配置信息", required = true) @RequestBody SpecGroup specGroup){
        log.info(specGroup.toString());
        try {
             if(categoryConfigService.smartSave(specGroup)){
                 return Result.success("更新成功");
             }
             else return Result.error("更新出错");
        }catch (IllegalArgumentException e){
            return Result.error( e.getMessage());
        }
    }


    @Operation(summary = "删除分类配置", description = "根据配置组ID删除对应的分类配置")
    @GetMapping("/delete")
    public Result delete(
            @Parameter(description = "配置组ID", required = true) @RequestParam Long groupId){
    try{
        if (categoryConfigService.delete(groupId)){
            return Result.success("删除成功");
        }
        else {
            return Result.error("删除失败");
        }
    } catch (IllegalArgumentException e) {
        // 处理参数不合法的情况
        return Result.error("IllegalArgumentException");

    } catch (Exception e) {
        // 记录异常信息
        log.error("An unexpected error occurred while deleting group with ID: {}", groupId, e);
        return Result.error("发生未知错误，请联系管理员");
    }
}

}
