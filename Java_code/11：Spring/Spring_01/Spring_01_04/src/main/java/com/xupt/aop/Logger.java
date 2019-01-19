package com.xupt.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/18 22:18
 */

@Aspect
@Component
public class Logger {

    @Pointcut("execution(* com.xupt.service.imp.*.*(..))")
    public void pointCutStart(){}

    @Before("pointCutStart()")
    public void beforeLog()
    {
        System.out.println("之前添加日志");
    }

    @After("pointCutStart()")
    public void afterLog()
    {
        System.out.println("之后添加日志");
    }
}
