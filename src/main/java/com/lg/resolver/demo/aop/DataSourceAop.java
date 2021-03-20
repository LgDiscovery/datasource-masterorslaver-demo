package com.lg.resolver.demo.aop;

import com.lg.resolver.demo.annotation.DataSourceSwitcher;
import com.lg.resolver.demo.component.DataSourceContextHolder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Order(value = 1)
@Component
public class DataSourceAop {

    @Pointcut("@annotation(com.lg.resolver.demo.annotation.DataSourceSwitcher)")
    public void pointCut(){};

    @Around(value="pointCut()")
    public Object setDynamicDataSource(ProceedingJoinPoint pjp) throws Throwable {
        boolean clear = false;
        try {
        Method method = this.getMethod(pjp);
        DataSourceSwitcher dataSourceSwitcher = method.getAnnotation(DataSourceSwitcher.class);
        clear = dataSourceSwitcher.clear();
        DataSourceContextHolder.set(dataSourceSwitcher.value().getDataSourceName());
        log.info("数据源切换至：{}",dataSourceSwitcher.value().getDataSourceName());
            return pjp.proceed();
        } finally {
            if(clear){
                DataSourceContextHolder.clear();
            }
        }
    }


    private Method getMethod(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        return  signature.getMethod();
    }

}
