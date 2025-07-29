package com.yuban.shop.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.pojo.origin.Result;
import com.yuban.shop.pojo.origin.categoryConfig.SpecGroup;
import com.yuban.shop.service.CategoryConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mallConf")
public class CategoryConfigController {

    @Autowired
    private CategoryConfigService categoryConfigService;

    @GetMapping("/list")
    public Result getList( @RequestParam(required = false,defaultValue = "") String catName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Page<SpecGroup> pageData = categoryConfigService.getList( catName, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("list", pageData.getRecords());
        response.put("count", pageData.getTotal());
        response.put("totalPage", pageData.getPages());
        response.put("currentPage", page);
        return Result.success(response);
    }

    @GetMapping("/getone")
    public Result getone(@RequestParam Long catId ) {
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

    @PostMapping("/edit")
    public Result smartSave(@RequestBody SpecGroup specGroup){
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


@GetMapping("/delete")
public Result delete(@RequestParam Long groupId){
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
