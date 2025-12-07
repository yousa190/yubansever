package com.yuban.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuban.shop.mapper.BannerMapper;
import com.yuban.shop.pojo.dto.BannerDto;
import com.yuban.shop.pojo.entity.Banner;
import com.yuban.shop.service.BannerService;
import com.yuban.shop.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public Map<String, Object> getBanners(String title, int page, int limit) {
        Page<Banner> pageParam = new Page<>(page, limit);
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(title != null && !title.isEmpty(), Banner::getTitle, title);
        // 只查询未删除的轮播图
        queryWrapper.eq(Banner::getIsDel, 0);
        Page<Banner> pageData = baseMapper.selectPage(pageParam, queryWrapper);
        
        Map<String, Object> response = new HashMap<>();
        response.put("list", pageData.getRecords());
        response.put("count", pageData.getTotal());
        response.put("totalPage", pageData.getPages());
        response.put("currentPage", page);
        return response;
    }

    @Override
    @Transactional
    public Banner addBanner(BannerDto bannerDto) {
        Banner banner = BeanCopyUtils.copyBean(bannerDto, Banner.class);
        banner.setCreateTime(LocalDateTime.now());
        banner.setUpdateTime(LocalDateTime.now());
        // 设置默认未删除状态
        banner.setIsDel(0);
        baseMapper.insert(banner);
        return banner;
    }

    @Override
    @Transactional
    public boolean updateBanner(BannerDto bannerDto) {
        Banner banner = BeanCopyUtils.copyBean(bannerDto, Banner.class);
        banner.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(banner);
        return true;
    }

    @Override
    @Transactional
    public void deleteBanner(Long id) {
        // 软删除：更新isDel字段为1
        Banner banner = new Banner();
        banner.setId(id);
        banner.setIsDel(1);
        baseMapper.updateById(banner);
    }
}