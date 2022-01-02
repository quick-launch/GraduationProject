package org.yxr_qrx.graduationproject.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yxr_qrx.graduationproject.controller.UserController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName:AopLog
 * @Author:41713
 * @Date 2021/10/31  23:17
 * @Version 1.0
 * 调用方法提示
 **/
@Component
@Aspect
public class AopLog {
    private static final Logger LOGGER = LoggerFactory.getLogger(AopLog.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");// 格式化时间
    /**
     *切点(第一个*表示返回值，第二个*表示任意类，第三个*表示任意方法，(..)表示任意参数)
     */
    @Pointcut("execution(* org.yxr_qrx.graduationproject.controller.*.*(..))" +
            "||execution(* org.yxr_qrx.graduationproject.service.*.*(..))")
    public void demo(){}

    /**
     * 前置通知
     * @param joinPoint
     */
    @Before("demo()")
    public void before(JoinPoint joinPoint){
        String controllerName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        LOGGER.info(sdf.format(new Date())+"---"+"IN "+controllerName+": "+methodName+"方法开始执行...");
    }

    /**
     * 后置通知
     * @param joinPoint
     */
    @After("demo()")
    public void after(JoinPoint joinPoint){
        String controllerName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        LOGGER.info(sdf.format(new Date())+"---"+"IN "+controllerName+": "+methodName+"方法执行结束...");
    }

    /**
     * 返回通知
     * @param joinPoint
     * @param result
     */
    @AfterReturning(value = "demo()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result){
        String controllerName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        LOGGER.info(sdf.format(new Date())+"---"+"IN "+controllerName+": "+methodName+"方法返回值为: "+result);
    }

    /**
     * 异常通知
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "demo()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e){
        String name = joinPoint.getSignature().getName();
        LOGGER.info(sdf.format(new Date())+"---"+name+"方法发生异常，异常是: "+e.getMessage());
    }

    /**
     * 环绕通知
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("demo()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        return proceedingJoinPoint.proceed();
    }
}
