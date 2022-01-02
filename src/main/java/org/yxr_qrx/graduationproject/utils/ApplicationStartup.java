package org.yxr_qrx.graduationproject.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @ClassNameApplicationStartup
 * @Author41713
 * @Date 2021/10/31  20:56
 * @Version 1.0
 **/
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ac = event.getApplicationContext();
        StepExecutor StepExecutor = ac.getBean(StepExecutor .class);
        Thread thread = new Thread(StepExecutor);
        thread.start();
    }
}
