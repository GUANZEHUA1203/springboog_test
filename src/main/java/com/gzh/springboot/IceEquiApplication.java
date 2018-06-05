package com.gzh.springboot;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月19日
 * @Desc SPRING BOOT 启动
 */
@EnableAsync
@EnableWebMvc
@EnableScheduling
@EnableAutoConfiguration
@MapperScan("com.gzh.springboot.dao")
@SpringBootApplication
public class IceEquiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IceEquiApplication.class, args);
    }

}
