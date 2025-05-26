package com.yuban.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.pojo.SpecGroup.SpecGroup;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface CateConfService {
     Page<SpecGroup> getList(String catName, int page, int limit);

     boolean smartSave(SpecGroup specGroup);

    SpecGroup getByCatId(Long catId);

    boolean delete(Long groupId);

}
