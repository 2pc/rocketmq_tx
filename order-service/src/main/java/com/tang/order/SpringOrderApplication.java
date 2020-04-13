package com.tang.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Classname SpringOrderApplication
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/11 12:25
 * @Created by ASUS
 */
@MapperScan(basePackages = {"com.tang.order.mapper"})
@SpringBootApplication
public class SpringOrderApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringOrderApplication.class, args);

    }

}