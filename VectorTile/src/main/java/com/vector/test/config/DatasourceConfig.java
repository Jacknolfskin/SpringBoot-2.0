package com.vector.test.config;

import com.vector.test.Application;
import com.zaxxer.hikari.HikariDataSource;
import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.ext.DebugInterceptor;
import org.beetl.sql.ext.spring4.BeetlSqlDataSource;
import org.beetl.sql.ext.spring4.BeetlSqlScannerConfigurer;
import org.beetl.sql.ext.spring4.SqlManagerFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @Author JerFer
 * @Date 2018/8/3---14:49
 */
@Configuration
public class DatasourceConfig {

    @Bean(name = "beetlSqlScannerConfigurerGIS")
    public BeetlSqlScannerConfigurer getBeetlSqlScannerConfigurerGIS(Environment env) {
        BeetlSqlScannerConfigurer conf = new BeetlSqlScannerConfigurer();
        conf.setBasePackage(env.getProperty("beetlsql.ds.gis.basePackage"));
        conf.setDaoSuffix(env.getProperty("beetlsql.ds.gis.daoSuffix"));
        conf.setSqlManagerFactoryBeanName("sqlManagerFactoryBeanGIS");
        return conf;
    }


    @Bean(name = "sqlManagerFactoryBeanGIS")
    @Primary
    public SqlManagerFactoryBean getGISSqlManagerFactoryBean(@Qualifier("gis") DataSource datasource, Environment env) {
        SqlManagerFactoryBean factory = new SqlManagerFactoryBean();

        BeetlSqlDataSource source = new BeetlSqlDataSource();
        source.setMasterSource(datasource);
        factory.setCs(source);
        try {
            factory.setDbStyle((DBStyle) (Application.class.getClassLoader()).loadClass(env.getProperty("beetlsql.ds.gis.dbStyle")).newInstance());
            factory.setNc((NameConversion) (Application.class.getClassLoader().loadClass(env.getProperty("beetlsql.ds.gis.nameConversion")).newInstance()));//开启驼峰
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        Boolean isDebug = Boolean.parseBoolean(env.getProperty("beetl-beetlsql", "dev=false").split("=")[1]);
        if (isDebug)
            factory.setInterceptors(new Interceptor[]{new DebugInterceptor()});

        factory.setSqlLoader(new ClasspathLoader(env.getProperty("beetlsql.ds.gis.sqlPath")));//sql文件路径
        return factory;
    }


    @Bean(name = "gis")
    @Primary
    public DataSource datasourceOther(Environment env) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(env.getProperty("spring.datasource.gis.url"));
        ds.setUsername(env.getProperty("spring.datasource.gis.username"));
        ds.setPassword(env.getProperty("spring.datasource.gis.password"));
        ds.setDriverClassName(env.getProperty("spring.datasource.gis.driver-class-name"));

        ds.setConnectionTimeout(Long.parseLong(env.getProperty("spring.datasource.gis.hikari.connection-timeout")));
        ds.setIdleTimeout(Long.parseLong(env.getProperty("spring.datasource.gis.hikari.idle-timeout")));
        ds.setMaxLifetime(Long.parseLong(env.getProperty("spring.datasource.gis.hikari.max-lifetime")));
        ds.setMaximumPoolSize(Integer.parseInt(env.getProperty("spring.datasource.gis.hikari.maximum-pool-size")));
        ds.setMinimumIdle(Integer.parseInt(env.getProperty("spring.datasource.gis.hikari.minimum-idle")));
        return ds;
    }
}
