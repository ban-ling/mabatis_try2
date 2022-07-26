package com.cymabatis.mabatis_try2;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

@MapperScan("com.cymabatis.mabatis_try2.mapper")



@EnableMPP
public class MabatisTry2Application {
    public static void main(String[] args) {
        SpringApplication.run(MabatisTry2Application.class, args);
    }

}
