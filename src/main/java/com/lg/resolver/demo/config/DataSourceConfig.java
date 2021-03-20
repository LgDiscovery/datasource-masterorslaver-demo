package com.lg.resolver.demo.config;

import com.alibaba.druid.pool.DruidAbstractDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.lg.resolver.demo.component.DataSourceRouter;
import com.lg.resolver.demo.enumconstants.DataSourceEnum;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.IsolationLevelDataSourceRouter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 主从配置  作用就是实现mysql的读写分离技术
 * 使用主数据库写 从数据库读数据  主从数据实时同步数据（运维）
 */
@Configuration
@MapperScan(basePackages = "com.lg.resolver.demo.mapper",sqlSessionTemplateRef = "sqlTemplate")
public class DataSourceConfig {

    /**
     * 主数据源
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource master(){
    return DruidDataSourceBuilder.create().build();
    }

    /**
     * 从数据源
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slaver")
    public DataSource slaver(){
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 实例化数据源路由 动态数据源
     */
    @Bean
    public DataSourceRouter dynamicDB(@Qualifier("master") DataSource masterDataSource,
                                      @Autowired(required = false) @Qualifier("slaver") DataSource slaverDataSource) {
        DataSourceRouter dynamicDataSource = new DataSourceRouter();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceEnum.MASTER.getDataSourceName(), masterDataSource);
        if (slaverDataSource != null) {
            targetDataSources.put(DataSourceEnum.SLAVER.getDataSourceName(), slaverDataSource);
        }
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        return dynamicDataSource;
    }

    /**
     * 配置sessionFactory
     * @param dynamicDataSource
     * @return
     */
    @Bean
    public SqlSessionFactory sessionFactory(@Qualifier("dynamicDB")DataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResource("classpath*:mapper/*Mapper.xml"));
        bean.setDataSource(dynamicDataSource);
        return bean.getObject();
    }

    /**
     * 创建一个SqlSessionTemplate
     * @param sqlSessionFactory
     * @return
     */
    @Bean
    public SqlSessionTemplate sqlTemplate(@Qualifier("sessionFactory")SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 事务管理
     * @param dynamicDataSource
     * @return
     */
    @Bean(name = "dataSourceTx")
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dynamicDB") DataSource dynamicDataSource){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dynamicDataSource);
        return dataSourceTransactionManager;
    }
}
