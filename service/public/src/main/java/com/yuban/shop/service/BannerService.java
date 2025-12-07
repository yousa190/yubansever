package com.yuban.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuban.shop.pojo.dto.BannerDto;
import com.yuban.shop.pojo.entity.Banner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
public interface BannerService extends IService<Banner> {
    /**
     * 获取轮播图列表
     * @param title 轮播图标题（模糊查询）
     * @param page 页码
     * @param limit 每页数量
     * @return 轮播图列表数据
     */
    Map<String, Object> getBanners(String title, int page, int limit);

    /**
     * 添加轮播图
     * @param bannerDto 轮播图信息
     * @return 添加的轮播图
     */
    Banner addBanner(BannerDto bannerDto);

    /**
     * 更新轮播图
     * @param bannerDto 轮播图信息
     * @return 是否更新成功
     */
    boolean updateBanner(BannerDto bannerDto);

    /**
     * 删除轮播图
     * @param id 轮播图ID
     */
    void deleteBanner(Long id);
}