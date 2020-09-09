package com.gmfiot.data;

import com.gmfiot.core.util.ReflectionUtil;
import com.gmfiot.data.sql.SqlClauseTypeEnum;
import com.gmfiot.data.sql.SqlServerSqlBuilder;
import com.gmfiot.data.sql.SqlTypeEnum;

import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class TestDemo {
    public static void main(String[] args) {
//        var tableInfo = SqlServerSqlGenerator.getTableInfo(User.class);
//        tableInfo = SqlServerSqlGenerator.getTableInfo(User.class);
        //System.out.println(tableInfo);

        var user = new User();
        user.setId(10001L);
        user.setName("张三");
        user.setStatus(1);
        user.setCreatedAt(new Date());

//        var nullColumns = SqlServerSqlGenerator.getNotNullColumns(user);
//
//        var updateSql = update(user);
//
//        var deleteSql = deleteById(10001L,User.class);

        //var selectByIdSql = selectById(10001L,User.class);

        var userQuery = new UserQuery();
        userQuery.setId(10001L);
        userQuery.setIds(new Long[]{1L,2L,3L});
        userQuery.setName("张三");
//        userQuery.setIdsNotIn(new Long[]{1L,2L,3L});
//        userQuery.setName("张三");
//        userQuery.setNameNotEquals("李四");
//        userQuery.setNameContains("李四");
//        userQuery.setNameIsNull(true);
//        userQuery.setNameEndsWith("ends");
//        userQuery.setNameStartsWith("starts");
//        userQuery.setNameNotContains("notContains");
//        userQuery.setCreatedAtFrom(new Date());
//        userQuery.setCreatedAtTo(new Date());
//        userQuery.setCreatedAtGreaterThan(new Date());
//        userQuery.setCreatedAtLessThan(new Date());
//        userQuery.setNames(new String[]{"张三","李四"});

        userQuery.setOrId(1002L);
        userQuery.setOrName("张三");

        var sql =  SqlServerSqlBuilder
                .getBuilder(user,userQuery)
                .build(SqlTypeEnum.SELECT)
                .build(SqlClauseTypeEnum.WHERE)
                .build(SqlClauseTypeEnum.ORDERBY)
                .build(SqlClauseTypeEnum.OFFSET_FETCH)
                .toString();
        System.out.println(sql);
    }

    public static String getName(User u){
//        Optional<User> user = Optional.ofNullable(u);
//        if(!user.isPresent()){
//            return "Unknow";
//        }
        return Optional.ofNullable(u)
                .map(user -> user.getName())
                .orElse("Unknow");
        //return user.get().getName();
    }



    private static String getExecutingMethodName()
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stackTrace[2];
        return e.getMethodName();
    }

    /*
    public String selectByQuery(UserQuery query) {
        QueryInfo queryInfo = SqlServerSqlGenerator.getQueryInfo(query.getClass());
        SqlServerSqlGenerator.getPropertyNameValueMapForQuery()
        StringBuffer sql = new StringBuffer("select * from users where 1=1 ");
        if(!query.getName().isBlank()) {
            sql.append(String.format("and username like '%s'", "%"+query.getName()+"%"));
        }
        return sql.toString();
    }*/
}