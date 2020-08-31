package com.gmfiot.core.data;

import com.gmfiot.core.BusinessException;

import java.io.Serializable;

/**
 * 分页数据结构
 * @author ThinkPad
 * @param <T>
 */
public class Paged<T> implements Serializable {

    public Paged(){}

    public Paged(T[] items,int count){
        if(items == null){
            throw new BusinessException();
        }
        this.items = items;
        this.count = Math.max(0,count);
    }

    public Paged(T[] items){
        this(items, items.length);
    }

    private int count;
    private T[] items;

    /**
     * 创建分页对象
     * @param items
     * @param count
     * @param <T>
     * @return
     */
    public static <T> Paged<T> create(T[] items,int count){
        return new Paged<>(items,count);
    }

    /**
     * 创建分页对象
     * @param items
     * @param <T>
     * @return
     */
    public static <T> Paged<T> create(T[] items){
        return new Paged<>(items);
    }
}