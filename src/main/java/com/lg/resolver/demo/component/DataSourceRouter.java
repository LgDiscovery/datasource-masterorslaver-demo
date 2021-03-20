package com.lg.resolver.demo.component;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 路由在主从分离是非常重要的,基本是读写切换的核心。
 * Spring 提供了 AbstractRoutingDataSource
 * 根据用户定义的规则选择当前的数据源，
 * 作用就是在执行查询之前，
 * 设置使用的数据源,实现动态路由的数据源，
 * 在每次数据库查询操作前执行它的抽象方法 determineCurrentLookupKey() 决定使用哪个数据源。
 *
 * 为了能有一个全局的数据源管理器,
 * 此时我们需要引入 DataSourceContextHolder
 * 这个数据库上下文管理器,
 * 可以理解为全局的变量,随时可取(见下面详细介绍),
 * 它的主要作用就是保存当前的数据源;
 */
public class DataSourceRouter extends AbstractRoutingDataSource {
    /**
     * 最终的determineCurrentLookupKey
     * 返回的是从DataSourceContextHolder中拿到的,
     * 因此在动态切换数据源的时候注解
     * 应该给DataSourceContextHolder设值
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.get();
    }
}
