package com.gmfiot.data;

import com.gmfiot.core.data.Query;

import java.util.Date;

public class TUserQuery extends Query {
    //分页
    //跳过多少条记录
//    private Integer skip;
//    //获取多少条记录
//    private Integer take = 20;
//    //排序
//    private String orderBy = "id desc";
//    private String groupBy = "";

    // where and 条件
    /**
     * and id = 1001
     */
    private Long id;
    // and id in (1001,1002,1003)
    private Long[] ids;
    // and id not in (1001,1002,1003)
    private Long[] idsNotIn;

    /**
     * private Long andId; and name = '张三'
     */
    private String name;
    /**
     * // and name != '张三'
     */
    private String nameNotEquals;
    // and name is null or name is not null
    private Boolean nameIsNull;
    // and name like '%张%'
    private String nameContains;
    // and name like '张%'
    private String nameStartsWith;
    // and name like '%张'
    private String nameEndsWith;
    // and name not like '%张%'
    private String nameNotContains;
    // and createdAt >= '2020-9-8'
    private Date createdAtFrom;
    // and createdAt <= '2020-9-9'
    private Date createdAtTo;
    // and createdAt > '2020-9-8'
    private Date createdAtGreaterThan;
    // and createdAt < '2020-9-8'
    private Date createdAtLessThan;

    private String[] names;


    // where and 条件
    // and id = 1001
    private Long orId;
    // and name = '张三'
    private String orName;
    // and name != '张三'
    private String orNameNotEquals;
    // and id in (1001,1002,1003)
    private Long[] orIds;
    // and id not in (1001,1002,1003)
    private Long[] orIdsNotIn;
    // and name is null or name is not null
    private Boolean orNameIsNull;
    // and name like '%张%'
    private String orNameContains;
    // and name like '张%'
    private String orNameStartsWith;
    // and name like '%张'
    private String orNameEndsWith;
    // and name not like '%张%'
    private String orNameNotContains;
    // and createdAt >= '2020-9-8'
    private Date orCreatedAtFrom;
    // and createdAt <= '2020-9-9'
    private Date orCreatedAtTo;
    // and createdAt > '2020-9-8'
    private Date orCreatedAtGreaterThan;
    // and createdAt < '2020-9-8'
    private Date orCreatedAtLessThan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameNotEquals() {
        return nameNotEquals;
    }

    public void setNameNotEquals(String nameNotEquals) {
        this.nameNotEquals = nameNotEquals;
    }

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    public Long[] getIdsNotIn() {
        return idsNotIn;
    }

    public void setIdsNotIn(Long[] idsNotIn) {
        this.idsNotIn = idsNotIn;
    }

    public Boolean getNameIsNull() {
        return nameIsNull;
    }

    public void setNameIsNull(Boolean nameIsNull) {
        this.nameIsNull = nameIsNull;
    }

    public String getNameContains() {
        return nameContains;
    }

    public void setNameContains(String nameContains) {
        this.nameContains = nameContains;
    }

    public String getNameStartsWith() {
        return nameStartsWith;
    }

    public void setNameStartsWith(String nameStartsWith) {
        this.nameStartsWith = nameStartsWith;
    }

    public String getNameEndsWith() {
        return nameEndsWith;
    }

    public void setNameEndsWith(String nameEndsWith) {
        this.nameEndsWith = nameEndsWith;
    }

    public String getNameNotContains() {
        return nameNotContains;
    }

    public void setNameNotContains(String nameNotContains) {
        this.nameNotContains = nameNotContains;
    }

    public Date getCreatedAtFrom() {
        return createdAtFrom;
    }

    public void setCreatedAtFrom(Date createdAtFrom) {
        this.createdAtFrom = createdAtFrom;
    }

    public Date getCreatedAtTo() {
        return createdAtTo;
    }

    public void setCreatedAtTo(Date createdAtTo) {
        this.createdAtTo = createdAtTo;
    }

    public Date getCreatedAtGreaterThan() {
        return createdAtGreaterThan;
    }

    public void setCreatedAtGreaterThan(Date createdAtGreaterThan) {
        this.createdAtGreaterThan = createdAtGreaterThan;
    }

    public Date getCreatedAtLessThan() {
        return createdAtLessThan;
    }

    public void setCreatedAtLessThan(Date createdAtLessThan) {
        this.createdAtLessThan = createdAtLessThan;
    }

    public Long getOrId() {
        return orId;
    }

    public void setOrId(Long orId) {
        this.orId = orId;
    }

    public String getOrName() {
        return orName;
    }

    public void setOrName(String orName) {
        this.orName = orName;
    }

    public String getOrNameNotEquals() {
        return orNameNotEquals;
    }

    public void setOrNameNotEquals(String orNameNotEquals) {
        this.orNameNotEquals = orNameNotEquals;
    }

    public Long[] getOrIds() {
        return orIds;
    }

    public void setOrIds(Long[] orIds) {
        this.orIds = orIds;
    }

    public Long[] getOrIdsNotIn() {
        return orIdsNotIn;
    }

    public void setOrIdsNotIn(Long[] orIdsNotIn) {
        this.orIdsNotIn = orIdsNotIn;
    }

    public Boolean getOrNameIsNull() {
        return orNameIsNull;
    }

    public void setOrNameIsNull(Boolean orNameIsNull) {
        this.orNameIsNull = orNameIsNull;
    }

    public String getOrNameContains() {
        return orNameContains;
    }

    public void setOrNameContains(String orNameContains) {
        this.orNameContains = orNameContains;
    }

    public String getOrNameStartsWith() {
        return orNameStartsWith;
    }

    public void setOrNameStartsWith(String orNameStartsWith) {
        this.orNameStartsWith = orNameStartsWith;
    }

    public String getOrNameEndsWith() {
        return orNameEndsWith;
    }

    public void setOrNameEndsWith(String orNameEndsWith) {
        this.orNameEndsWith = orNameEndsWith;
    }

    public String getOrNameNotContains() {
        return orNameNotContains;
    }

    public void setOrNameNotContains(String orNameNotContains) {
        this.orNameNotContains = orNameNotContains;
    }

    public Date getOrCreatedAtFrom() {
        return orCreatedAtFrom;
    }

    public void setOrCreatedAtFrom(Date orCreatedAtFrom) {
        this.orCreatedAtFrom = orCreatedAtFrom;
    }

    public Date getOrCreatedAtTo() {
        return orCreatedAtTo;
    }

    public void setOrCreatedAtTo(Date orCreatedAtTo) {
        this.orCreatedAtTo = orCreatedAtTo;
    }

    public Date getOrCreatedAtGreaterThan() {
        return orCreatedAtGreaterThan;
    }

    public void setOrCreatedAtGreaterThan(Date orCreatedAtGreaterThan) {
        this.orCreatedAtGreaterThan = orCreatedAtGreaterThan;
    }

    public Date getOrCreatedAtLessThan() {
        return orCreatedAtLessThan;
    }

    public void setOrCreatedAtLessThan(Date orCreatedAtLessThan) {
        this.orCreatedAtLessThan = orCreatedAtLessThan;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }
}
