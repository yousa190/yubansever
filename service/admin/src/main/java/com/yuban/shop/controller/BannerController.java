package com.yuban.shop.controller;

import com.yuban.shop.pojo.dto.BannerDto;
import com.yuban.shop.pojo.entity.Banner;
import com.yuban.shop.pojo.entity.Result;
import com.yuban.shop.pojo.enums.HttpCodeEnum;
import com.yuban.shop.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/banner")
@Tag(name = "轮播图管理接口", description = "轮播图的增删改查相关接口")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Operation(summary = "获取轮播图列表", description = "分页获取轮播图列表，支持按标题模糊搜索")
    @GetMapping("/list")
    public Result getBanners(
            @Parameter(description = "轮播图标题，支持模糊搜索") @RequestParam(required = false, defaultValue = "") String title,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> response = bannerService.getBanners(title, page, limit);
        return Result.success(response);
    }

    @Operation(summary = "添加轮播图", description = "新增轮播图")
    @PostMapping("/add")
    public Result addBanner(
            @Parameter(description = "轮播图信息", required = true) @RequestBody BannerDto bannerDto) {
        log.info(bannerDto.toString());
        try {
            Banner saved = bannerService.addBanner(bannerDto);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.error(HttpCodeEnum.SYSTEM_ERROR, "添加失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新轮播图", description = "更新轮播图信息")
    @PostMapping("/update")
    public Result updateBanner(
            @Parameter(description = "轮播图信息", required = true) @RequestBody BannerDto bannerDto) {
        log.info(bannerDto.toString());
        try {
            bannerService.updateBanner(bannerDto);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(HttpCodeEnum.SYSTEM_ERROR, "更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除轮播图", description = "根据ID删除轮播图")
    @GetMapping("/delete")
    public Result deleteBanner(
            @Parameter(description = "要删除的轮播图ID", required = true) @RequestParam Long id) {
        try {
            bannerService.deleteBanner(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(HttpCodeEnum.SYSTEM_ERROR, "删除失败: " + e.getMessage());
        }
    }
}