package com.yuban.shop.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.pojo.origin.categoryConfig.SpecGroup;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface CategoryConfigService {
     Page<SpecGroup> getList(String catName, int page, int limit);

     boolean smartSave(SpecGroup specGroup);

    SpecGroup getByCatId(Long catId);

    boolean delete(Long groupId);

}
