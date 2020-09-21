package com.gmfiot.data.test;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.*;

public class ContextListener implements ApplicationListener<ApplicationContextEvent> {

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if(event instanceof ContextRefreshedEvent){
            //SpringContextUtil.setApplicationContext(event.getApplicationContext());
            System.out.println("context 刷新");
        }
        if (event instanceof ContextClosedEvent) {
            System.out.println("context 关闭");
        }
        if (event instanceof ContextStoppedEvent) {
            System.out.println("context 停止");
        }
        if (event instanceof ContextStartedEvent) {
            System.out.println("Context 开启");
        }
    }
}
