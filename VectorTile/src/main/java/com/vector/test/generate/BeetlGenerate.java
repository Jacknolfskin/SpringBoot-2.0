package com.vector.test.generate;

import org.beetl.sql.core.*;
import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.PostgresStyle;
import org.beetl.sql.ext.DebugInterceptor;

import java.util.List;

public class BeetlGenerate {
    public static void main(String[] args) {
        ConnectionSource source = ConnectionSourceHelper.getSimple(
                "org.postgresql.Driver",
                "jdbc:postgresql://localhost:5432/gis",
//                "jdbc:postgresql://10.111.101.44:8082/ywgh",
                "postgres",
                "postgres");
        DBStyle postgresStyle = new PostgresStyle();
        // sql语句放在classpagth的/sql 目录下
        SQLLoader loader = new ClasspathLoader("/sql");
        // 数据库命名跟java命名一样，所以采用DefaultNameConversion，还有一个是UnderlinedNameConversion，下划线风格的，
        UnderlinedNameConversion nc = new UnderlinedNameConversion();
        // 最后，创建一个SQLManager,DebugInterceptor 不是必须的，但可以通过它查看sql执行情况
        SQLManager sqlManager = new SQLManager(postgresStyle, loader, source, nc, new Interceptor[]{new DebugInterceptor()});

        SQLReady sqlReady = new SQLReady("SELECT  tablename   FROM   pg_tables t where t.schemaname='public'");
        List<String> tabNames = sqlManager.execute(sqlReady, String.class);
        tabNames.clear();
        tabNames.add("t_build");
        tabNames.add("t_poi");
        tabNames.add("t_river");
        for (String string : tabNames) {
            try {
                sqlManager.genPojoCode(string,"cn.com.walkgis.microdraw.walkgisdraw.entity");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
