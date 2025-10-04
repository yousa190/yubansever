package com.yuban.shop.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuban.shop.pojo.entity.categoryConfig.SpecGroup;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface CategoryConfigService  extends IService<SpecGroup> {
     Page<SpecGroup> getList(String catName, int page, int limit);

     boolean smartSave(SpecGroup specGroup);

    SpecGroup getByCatId(Long catId);

    boolean delete(Long groupId);

}
