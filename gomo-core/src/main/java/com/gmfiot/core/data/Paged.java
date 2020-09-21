package com.gmfiot.core.data;

import com.gmfiot.core.BusinessException;

import java.io.Serializable;
import java.util.List;


/**
 * @author BaGuangHui
 * @param <T>
 */
public class Paged<T> implements Serializable {

    public Paged(){}

    public Paged(List<T> items,int count){
        if(items == null){
            throw new BusinessException();
        }
        this.items = items;
        this.count = Math.max(0,count);
    }

    public Paged(List<T> items){
        this(items, items.size());
    }

    private int count;
    private List<T> items;

    /**
     * 创建分页对象
     * @param items
     * @param count
     * @param <T>
     * @return
     */
    public static <T> Paged<T> create(List<T> items,int count){
        return new Paged<>(items,count);
    }

    /**
     * 创建分页对象
     * @param items
     * @param <T>
     * @return
     */
    public static <T> Paged<T> create(List<T> items){
        return new Paged<>(items);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}