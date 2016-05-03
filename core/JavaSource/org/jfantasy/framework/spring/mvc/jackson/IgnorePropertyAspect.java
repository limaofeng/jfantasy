package org.jfantasy.framework.spring.mvc.jackson;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class IgnorePropertyAspect {
    public static final Log LOG = LogFactory.getLog(IgnorePropertyAspect.class);

    @Pointcut("execution(* org.jfantasy.*.rest.*.*(..))")
    private void anyMethod() {

    }

    @Around("anyMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object returnVal = pjp.proceed(); // 返回源结果
        return returnVal;
    }

    @AfterThrowing(pointcut = "anyMethod()", throwing = "e")
    public void doAfterThrowing(Exception e) {
        System.out.println(" -------- AfterThrowing -------- ");
    }
}