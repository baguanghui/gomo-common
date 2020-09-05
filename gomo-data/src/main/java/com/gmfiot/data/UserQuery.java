package com.gmfiot.data;

import com.gmfiot.core.data.Query;

import java.util.Date;

public class UserQuery extends Query {
    private Long id;
    private String name;
    private Long[] ids;
    private String nameContains;
    private Date statisDateFrom;
    public Boolean statisDateFromExclusive = true;
    private Date StatisDateTo;
    public Boolean StatisDateToExclusive = true;

    private Long orId;
    private String orName;
    private Long[] orIds;
    private String orNameContains;
    private Date orStatisDateFrom;
    private Date orStatisDateTo;
    private String orderByField;
    private String orderByDirection;
}
