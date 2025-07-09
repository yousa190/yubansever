package com.yuban.shop.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    // 私有构造函数，防止实例化
    private BeanCopyUtils() {
    }

    /**
     * 复制单个 Bean 的属性
     * @param source 源对象
     * @param clazz 目标对象的类
     * @param <V> 目标对象的类型
     * @return 复制后的目标对象，如果出现异常则返回 null
     */
    public static <V> V copyBean(Object source, Class<V> clazz) {
        if (source == null || clazz == null) {
            return null;
        }
        try {
            V target = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Bean 拷贝失败", e);
        }
    }

    /**
     * 复制 Bean 列表的属性
     * @param sourceList 源对象列表
     * @param clazz 目标对象的类
     * @param <O> 源对象的类型
     * @param <V> 目标对象的类型
     * @return 复制后的目标对象列表
     */
    public static <O, V> List<V> copyBeanList(List<O> sourceList, Class<V> clazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return List.of();
        }
        return sourceList.stream()
                .map(source -> copyBean(source, clazz))
                .collect(Collectors.toList());
    }

    /**
     * 获取对象中值为 null 的属性名数组
     * @param source 源对象
     * @return 值为 null 的属性名数组
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}