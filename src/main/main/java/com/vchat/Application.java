package com.vchat;/**
 * Created by Administrator on 2019/7/24.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Administrator
 * @version 1.0
 * @ClassName Application
 * @Description TODO
 * @Date 2019/7/24 13:27
 **/
@SpringBootApplication
// 扫描mybatis mapper包路径
@MapperScan(basePackages="com.vchat.mapper")
// 扫描 所有需要的包, 包含一些自用的工具类包 所在的路径
@ComponentScan(basePackages= {"com.vchat", "org.n3r.idworker"})
public class Application {

    @Bean
    public SpringUtil getSpingUtil() {
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

