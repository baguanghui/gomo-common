package com.gmfiot.data.test;

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

    // 1.countAsOrderNumGroupBy = "userId,userName"
    // -> select userId,userName,count(*) as OrderNum where datetimeAt > '2019-1-1' and datetimeAt < '2020' group by userId,userName
    // 2.countAsOrderNumGroupBy = ""
    // -> select count(*) as OrderNum where datetimeAt > '2019-1-1' and datetimeAt < '2020'
    // count(*),sum(orderAmount),

    //sum(列名),max(列名),min(列名),avg(列名),count(*),count(列名)
    private String countStarAsOrderNumGroupBy;
    // = "empid,YEAR(orderdate)"
    private String sumAmountAsTotalOrders_maxAmountAsMaxAmountGroupBy;

    private String countNameAsNameNumGroupBy;

    private String maxNameAsMaxNameNumGroupBy;

    private String minAgeAsMinAgeGroupBy;

    private String sumSalaryAsTotalSalaryGroupBy;

    private String avgSalaryAsAvgSalaryGroupBy = "salary";

    private String having = "count(*) > 1";

    private Class modelClass;

    //private String having;

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

    public String getCountStarAsOrderNumGroupBy() {
        return countStarAsOrderNumGroupBy;
    }

    public void setCountStarAsOrderNumGroupBy(String countStarAsOrderNumGroupBy) {
        this.countStarAsOrderNumGroupBy = countStarAsOrderNumGroupBy;
    }

    public String getSumAmountAsTotalOrders_maxAmountAsMaxAmountGroupBy() {
        return sumAmountAsTotalOrders_maxAmountAsMaxAmountGroupBy;
    }

    public void setSumAmountAsTotalOrders_maxAmountAsMaxAmountGroupBy(String sumAmountAsTotalOrders_maxAmountAsMaxAmountGroupBy) {
        this.sumAmountAsTotalOrders_maxAmountAsMaxAmountGroupBy = sumAmountAsTotalOrders_maxAmountAsMaxAmountGroupBy;
    }

    public String getHaving() {
        return having;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public String getCountNameAsNameNumGroupBy() {
        return countNameAsNameNumGroupBy;
    }

    public void setCountNameAsNameNumGroupBy(String countNameAsNameNumGroupBy) {
        this.countNameAsNameNumGroupBy = countNameAsNameNumGroupBy;
    }

    public String getMaxNameAsMaxNameNumGroupBy() {
        return maxNameAsMaxNameNumGroupBy;
    }

    public void setMaxNameAsMaxNameNumGroupBy(String maxNameAsMaxNameNumGroupBy) {
        this.maxNameAsMaxNameNumGroupBy = maxNameAsMaxNameNumGroupBy;
    }

    public String getMinAgeAsMinAgeGroupBy() {
        return minAgeAsMinAgeGroupBy;
    }

    public void setMinAgeAsMinAgeGroupBy(String minAgeAsMinAgeGroupBy) {
        this.minAgeAsMinAgeGroupBy = minAgeAsMinAgeGroupBy;
    }

    public String getSumSalaryAsTotalSalaryGroupBy() {
        return sumSalaryAsTotalSalaryGroupBy;
    }

    public void setSumSalaryAsTotalSalaryGroupBy(String sumSalaryAsTotalSalaryGroupBy) {
        this.sumSalaryAsTotalSalaryGroupBy = sumSalaryAsTotalSalaryGroupBy;
    }

    public String getAvgSalaryAsAvgSalaryGroupBy() {
        return avgSalaryAsAvgSalaryGroupBy;
    }

    public void setAvgSalaryAsAvgSalaryGroupBy(String avgSalaryAsAvgSalaryGroupBy) {
        this.avgSalaryAsAvgSalaryGroupBy = avgSalaryAsAvgSalaryGroupBy;
    }

    public Class getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class modelClass) {
        this.modelClass = modelClass;
    }
}
