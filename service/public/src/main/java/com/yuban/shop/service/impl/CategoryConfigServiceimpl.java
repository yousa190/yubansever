package com.yuban.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuban.shop.exception.SystemException;
import com.yuban.shop.mapper.CategoryConfigMapper;
import com.yuban.shop.pojo.enums.HttpCodeEnum;
import com.yuban.shop.pojo.origin.categoryConfig.SpecGroup;
import com.yuban.shop.service.CategoryConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class CategoryConfigServiceimpl implements CategoryConfigService {

//    处理JSON
    private static final ObjectMapper objectMapper = new ObjectMapper();



    @Autowired
    private CategoryConfigMapper categoryConfigMapper;

    @Override
    @Transactional(readOnly = true) // 只读事务优化
    public SpecGroup getByCatId(Long catId) {
        if (catId == null) {
            throw new SystemException(HttpCodeEnum.CATID_NOT_NULL);
        }

        LambdaQueryWrapper<SpecGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SpecGroup::getCatId, catId);
        return categoryConfigMapper.selectOne(queryWrapper);
    }

    @Override
    public Page<SpecGroup> getList(String catName, int page, int limit) {
        Page<SpecGroup> pageParam = new Page<>(page, limit);
        if (catName != null) {

        }
        Page<SpecGroup> result = categoryConfigMapper.selectByCondition(catName, pageParam);
        return result;
    }

    /*
      智能保存方法（存在则更新，不存在则新增）
      @param specGroup 参数组对象（需包含catId）
     * @return 操作结果
     */
    public boolean smartSave(SpecGroup specGroup) {
        try {
            // 参数校验
            if (specGroup.getCatId() == null) {
                throw new SystemException(HttpCodeEnum.CATID_NOT_NULL);
            }

            // 查询现有配置
            LambdaQueryWrapper<SpecGroup> query = Wrappers.lambdaQuery();
            query.eq(SpecGroup::getCatId, specGroup.getCatId());
            SpecGroup existing = categoryConfigMapper.selectOne(query);

            // 更新或新增
            if (existing != null) {
                existing.setCatName(specGroup.getCatName());
                existing.setGroupConfig(specGroup.getGroupConfig());
                return categoryConfigMapper.updateById(existing) > 0;
            }
            return categoryConfigMapper.insert(specGroup) > 0;

        } catch (DataAccessException e) {
            throw new SystemException(HttpCodeEnum.DB_TIMEOUT);
        }
    }



    @Override
    public boolean delete(Long groupId) {
        if (groupId == null) {
            log.warn("删除操作接收到空ID");
            throw new SystemException(HttpCodeEnum.GROUPID_NOT_NULL);
        }

        try {
            int affectedRows = categoryConfigMapper.deleteById(groupId);
            if (affectedRows > 0) {
                log.info("成功删除配置，ID：{}", groupId);
            } else {
                log.warn("尝试删除不存在的配置，ID：{}", groupId);
            }
            return affectedRows > 0;
        } catch (DataAccessException e) {
            log.error("数据库异常，删除失败，ID：{}", groupId, e);
            throw new SystemException(HttpCodeEnum.DB_TIMEOUT);
        }
    }
}
