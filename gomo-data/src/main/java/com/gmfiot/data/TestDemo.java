package com.gmfiot.data;

import com.gmfiot.core.util.ReflectionUtil;
import com.gmfiot.core.util.User;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestDemo {
    public static void main(String[] args) {
//        var tableInfo = SqlServerSqlGenerator.getTableInfo(User.class);
//        tableInfo = SqlServerSqlGenerator.getTableInfo(User.class);
        //System.out.println(tableInfo);

        var user = new User();
        user.setId(10001L);
        user.setName("张三");
        var insertSql = insert(user);
        user.setStatus(1);
        user.setCreatedAt(new Date());
        var updateSql = update(user);

        System.out.println("执行" +TestDemo.class.getSimpleName()+ ":" + new Exception().getStackTrace()[0].getClassName());

        var deleteSql = deleteById(10001L,User.class);

        String className = Thread.currentThread().getStackTrace()[1].getClassName();//调用的类名
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();//调用的方法名
        int lineNumber = Thread.currentThread().getStackTrace()[1].getLineNumber();//调用的行数

        var selectByIdSql = selectById(10001L,User.class);

    }

    private static String getExecutingMethodName()
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stackTrace[2];
        return e.getMethodName();
    }

    public static String insert(User user) {
        var tableInfo = SqlServerSqlGenerator.getTableInfo(user.getClass());
        StringBuffer sql = new StringBuffer(String.format("insert into %s(",tableInfo.getTableName()));
        var columnList = SqlServerSqlGenerator.getNotNullColumns(user);
        var columnStr = String.join(",",columnList);
        sql.append(columnStr);
        sql.append(") values (");
        List<String> values = columnList.stream().map(field -> String.format("#{%s}",field)).collect(Collectors.toList());
        var valueStr = String.join(",",values);
        sql.append(valueStr);
        sql.append(")");
        return sql.toString();
    }

    public static String update(User user) {
        var tableInfo = SqlServerSqlGenerator.getTableInfo(user.getClass());
        StringBuffer sql = new StringBuffer(String.format("UPDATE %s SET ",tableInfo.getTableName()));
        List<String> values = SqlServerSqlGenerator.getNotNullColumns(user).stream().map(field -> String.format("%s = #{%s}",field,field)).collect(Collectors.toList());
        var valueStr = String.join(",",values);
        sql.append(valueStr);
        sql.append(" WHERE Id = #{id}");
        return  sql.toString();
    }

    public static String deleteById(long id,Class<?> clazz) {
        String methodName = ReflectionUtil.getCurrentMethodName();
        //var TestDemo.class.getDeclaredMethod(methodName);
        var tableInfo = SqlServerSqlGenerator.getTableInfo(clazz);
        var sql = String.format("DELETE %s WHERE Id = #{id}",tableInfo.getTableName());
        return  sql;
    }

    public static String selectById(Long id,Class<?> clazz) {
        String methodName = ReflectionUtil.getCurrentMethodName();
        var tableInfo = SqlServerSqlGenerator.getTableInfo(clazz);
        var columnStr = String.join(",",tableInfo.getColumns());
        var sql = String.format("SELECT %s FROM %s WHERE Id = #{id}",columnStr,tableInfo.getTableName());
        return sql;
    }
}
