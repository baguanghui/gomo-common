package com.gmfiot.data.sql;

public enum SqlConditionTypeEnum {
    AND("AND","逻辑与条件"),
    OR("OR","逻辑或条件");

    private String name;
    private String description;

    SqlConditionTypeEnum(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
