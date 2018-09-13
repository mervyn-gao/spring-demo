package com.springmvc.demo.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuli
 * @create 2018/8/1
 */
public class BeanUtil {
    /**
     * 将一个集合的拷贝到另一个集合(集合中实体类型可不同)
     *
     * @param src
     * @param targetType
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyList(List<S> src, Class<T> targetType) {
        List<T> target = new ArrayList<>();

        if (!CollectionUtils.isEmpty(src)) {
            for (S s : src) {
                T t = BeanUtils.instantiateClass(targetType);
                BeanUtils.copyProperties(s, t);
                target.add(t);
            }
        }
        return target;
    }

    /**
     * 将一个对象的值copy到新的对象中去
     *
     * @param src
     * @param targetType
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T copyProperties(S src, Class<T> targetType) {
        T target = BeanUtils.instantiateClass(targetType);
        //备注：这儿判断如果为null返回空对象，
        if(src==null){
            return target;
        }
        BeanUtils.copyProperties(src, target);
        return target;
    }
}
