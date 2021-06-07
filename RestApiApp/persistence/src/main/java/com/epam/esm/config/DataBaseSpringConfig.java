package com.epam.esm.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan("com.epam.esm")
@PropertySource("classpath:db.properties")
public class DataBaseSpringConfig {

    @Autowired
    private Environment env;

    @Bean
    public BasicDataSource basicDataSource(){
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(env.getProperty("db.driver"));
        ds.setUrl(env.getProperty("db.url"));
        ds.setUsername(env.getProperty("db.username"));
        ds.setPassword(env.getProperty("db.password"));
        ds.setInitialSize(Integer.parseInt(env.getProperty("db.initial.size")));
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate (){
        return new JdbcTemplate(basicDataSource());
    }

}
