package com.gcx.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    在使用了generator插件后，没办法在生成的mapper类等上使用@mapper等annotation
    所以需要用@MapperScan来指定mapper类的精确位置
 */

@SpringBootApplication
@MapperScan("com.gcx.community.mapper")
public class CommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}
