package com.gmfiot.data.test;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.gmfiot.data.SqlServerDataProvider;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author BaGuangHui
 */
//@Configuration
//@PropertySource(value = "classpath:sqlserver-druid.properties")
//@ComponentScan(basePackages = {"com.gmfiot.data"})
//@EnableScheduling
public class SpringConfig {

    //数据源
    @Bean
    public DataSource dataSource(Environment env) {
        Properties properties = new Properties();
        properties.put("driverClassName",env.getProperty("jdbc.driverClassName"));
        properties.put("url",env.getProperty("jdbc.url"));
        properties.put("username",env.getProperty("jdbc.username"));
        properties.put("password",env.getProperty("jdbc.password"));
        properties.put("initialSize",env.getProperty("jdbc.initialSize"));
        properties.put("maxActive",env.getProperty("jdbc.maxActive"));
        properties.put("maxWait",env.getProperty("jdbc.maxWait"));
        DataSource dataSource = null;
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return dataSource;
    }

    //jdbc 模板

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }

    //事务管理器
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    //事务模板
    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager){
        return new TransactionTemplate(platformTransactionManager);
    }

//    @Bean(destroyMethod = "shutdown")
//    public Executor taskExecutor(){
//        return Executors.newScheduledThreadPool(20);
//    }

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        //context.refresh();
        //context.getEnvironment().setConversionService();
        //context.addApplicationListener(new ContextListener());
        SqlServerDataProvider dataProvider = context.getBean(SqlServerDataProvider.class);
        var lazyComponent = context.getBean(LazyComponent.class);
        System.out.println(lazyComponent.testValue);



        //ScheduledExecutorService scheduledExecutorService;

//        var id = dataProvider.getId();
//        var user = new TUser();
//        user.setId(id);
//        user.setName("橙梦科技2");
//        user.setStatus(0);
//        user.setCreatedAt(new Date());
//        var query = new TUserQuery();
//        query.setIds(new Long[]{1L,36L});
        //query.setNames(new String[]{"管理员","巴光辉"});
        //ids1,ids2
        //query.setName("巴光辉");
        //var userList = dataProvider.select(query,TUser.class);
        //System.out.println(id);
//       var userId = dataProvider.executeInTransaction((status) -> {
//            var effectRows = dataProvider.delete(30063098L,TUser.class);
//            var id = dataProvider.getId();
//            var user = new TUser();
//            user.setId(id);
//            user.setName("橙梦科技2");
//            user.setStatus(0);
//            user.setCreatedAt(new Date());
//            dataProvider.insert(user);
//            return id;
//        });
//        context.start();
//        context.stop();
        System.in.read();
        context.close();
    }

}
