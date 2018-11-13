package com.lyqiang.sharding;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author guolq on 2018/11/13.
 */
public class Test {

    public static void main(String[] args) {

        /**
         * create table test_shard0 (
         id BIGINT(20) PRIMARY key,
         name varchar(64)
         );


         create table test_shard1 (
         id BIGINT(20) PRIMARY key,
         name varchar(64)
         );

         create table test_shard2 (
         id BIGINT(20) PRIMARY key,
         name varchar(64)
         );
         */

        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        JdbcTemplate jdbcTemplate = app.getBean("jdbcTemplate", JdbcTemplate.class);

        List list = jdbcTemplate.queryForList("select * from t_worker limit 10");

        System.out.println(list.size());

        for (int i = 0; i < 10; i++) {
            String sql = "insert into test_shard (id, name) values (" + i + " , 'hello')";
            System.out.println(sql);
            jdbcTemplate.execute(sql);
        }

        Map map = jdbcTemplate.queryForMap("select * from test_shard where id = 8");
        System.out.println(map.toString());

    }

}
