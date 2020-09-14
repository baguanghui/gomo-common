package com.gmfiot.core.data;

/**
 * 数据查询抽象类，用户表查询
 * @author ThinkPad
 */
public class Query {

    private Integer skip = 0;
    private Integer take = 20;
    private String orderBy = "id desc";
    //private Class modelClass;

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public Integer getTake() {
        return take;
    }

    public void setTake(Integer take) {
        this.take = take;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

//    public Class getModelClass() {
//        return modelClass;
//    }
//
//    public void setModelClass(Class modelClass) {
//        this.modelClass = modelClass;
//    }
}
