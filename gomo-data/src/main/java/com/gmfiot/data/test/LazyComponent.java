package com.gmfiot.data.test;


import com.gmfiot.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

//@Component
public class LazyComponent {
    public LazyComponent(){
        System.out.println("start to init");
    }

    @Value("#{'Hello World'.concat('!')}")
    public String testValue;

    @Scheduled(cron = "0/5 * *  * * ?")
    public void doSchedule(){
        System.out.println("Execute at " + System.currentTimeMillis());
    }
}
