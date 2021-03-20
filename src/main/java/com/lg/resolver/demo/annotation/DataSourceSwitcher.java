package com.lg.resolver.demo.annotation;

import com.lg.resolver.demo.enumconstants.DataSourceEnum;

import java.lang.annotation.*;

/**
 * @author Administrator
 * 方法级别上使用该注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceSwitcher {

    /**
     * 默认数据源
     * @return
     */
    DataSourceEnum value() default DataSourceEnum.MASTER;

    /**
     * 清除
     * @return
     */
    boolean clear() default true;
}
