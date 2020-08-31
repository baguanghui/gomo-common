package com.gmfiot.core.data;

/**
 * 数据查询抽象类，用户表查询
 * @author ThinkPad
 */
public abstract class Query {

    private int skip;
    private int take = 20;
    private String orderBy;

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
