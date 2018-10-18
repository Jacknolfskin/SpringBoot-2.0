package com.example.codegenerateor;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.example.codegenerateor.util.EnvUtil;

/**
 * @Auther: feihu5
 * @Date: 2018/10/17 14:51
 * @Description: Mybatis-Plus版本
 */
public class CodeGenerator {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        if (EnvUtil.isLinux()) {
            gc.setOutputDir("/usr/CodeGenerator/");
        } else {
            gc.setOutputDir("D:/CodeGenerator/");
        }
        gc.setAuthor("feihu5");
        //是否覆盖已有文件
        gc.setFileOverride(true);
        // 开启 activeRecord 模式
        gc.setActiveRecord(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert());
        dsc.setUrl("jdbc:mysql://****:3306/mea_vrsp_biz?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&allowMultiQueries=true&useSSL=false");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("***");
        dsc.setPassword("***");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("Generator");
        pc.setParent("com.codegenerator");
        // 这里是控制器包名，默认web
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 字段名生成策略
        strategy.setFieldNaming(NamingStrategy.underline_to_camel);
        mpg.setStrategy(strategy);
        mpg.execute();
    }

}

