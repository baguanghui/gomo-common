package com.gmfiot.data.test;

import com.gmfiot.core.util.ReflectionUtil;
import com.gmfiot.core.util.StringUtil;
import com.gmfiot.data.sql.SqlPlaceholderEnum;
import com.gmfiot.data.sql.SqlServerSqlBuilder;
import com.gmfiot.data.sql.SqlTypeEnum;

import java.util.*;

public class TestDemo {
    public static void main(String[] args) {
//        var tableInfo = SqlServerSqlGenerator.getTableInfo(User.class);
//        tableInfo = SqlServerSqlGenerator.getTableInfo(User.class);
        //System.out.println(tableInfo);

        var user = new TUser();
        user.setId(10001L);
        user.setName("张三");
        user.setStatus(1);
        user.setCreatedAt(new Date());
//        Map<String,String> paramsMap = new HashMap<>();
//        paramsMap.put("code","123456");

        String template = "用户Id:{id},用户名：{name}";
        var text = StringUtil.getTemplateText(user,template);
        System.out.println(text);

//        var nullColumns = SqlServerSqlGenerator.getNotNullColumns(user);
//
//        var updateSql = update(user);
//
//        var deleteSql = deleteById(10001L,User.class);

        //var selectByIdSql = selectById(10001L,User.class);

        var userQuery = new TUserQuery();
        userQuery.setId(10001L);
        userQuery.setIds(new Long[]{1L,2L,3L});
        userQuery.setName("张三");
        userQuery.setIdsNotIn(new Long[]{1L,2L,3L});
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

//        userQuery.setOrId(1002L);
//        userQuery.setOrName("张三");
//        userQuery.setModelClass(TUser.class);
//
        var sql =  SqlServerSqlBuilder
                //.getBuilderForQuery(userQuery)
                .getBuilder(TUser.class,userQuery)
                .setSqlPlaceholder(SqlPlaceholderEnum.COLON)
                .build(SqlTypeEnum.SELECT)
                //.build(SqlTypeEnum.DISTINCT)
                //.build(SqlTypeEnum.TOP)
                .build(SqlTypeEnum.WHERE)
                .build(SqlTypeEnum.ORDER_BY)
                .build(SqlTypeEnum.OFFSET_FETCH)
                .toString();
//        System.out.println(sql);
    }

    public static String getName(TUser u){
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