package org.yxr_qrx.graduationproject.utils;

import org.springframework.stereotype.Component;

/**
 * @ClassNameStepExecutor
 * @Author41713
 * @Date 2021/10/31  20:55
 * @Version 1.0
 **/
@Component
public class StepExecutor implements Runnable {
    @Override
    public void run() {
        startStreamTask();

    }

    /**
     * 项目启动后打开1个页面
     */
    public void startStreamTask() {
        try {
            Runtime.getRuntime().exec("cmd   /c   start   http://localhost:8080/ ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
