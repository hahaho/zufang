package com.apass.gfb.framework.mybatis;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Created by jie.xu on 17/4/20.
 */
@Configuration
public class AJPMybatisConfigurationV2 {
    private static Log logger = LogFactory.getLog(MybatisConfigurationV2.class);
    @Bean(name = "ajpmysqlSqlSessionFactory")
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("ajpmysqlDataSource") DataSource mysqlDataSource) {
        try {
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setDataSource(mysqlDataSource);
            sessionFactory.setTypeAliasesPackage("com.apass.zufang.domain.ajp.entity");
            sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:ajpmapper/**/*.xml"));
            return sessionFactory.getObject();
        } catch (Exception e) {
            logger.error("Could not confiure mybatis session factory", e);
            throw new RuntimeException(e);
        }
    }
    @Bean(name = "ajpmysqlMapperScannerConfigurer")
    public MapperScannerConfigurer mysqlMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.apass.zufang.mapper.ajp");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("ajpmysqlSqlSessionFactory");
        return mapperScannerConfigurer;
    }

}
