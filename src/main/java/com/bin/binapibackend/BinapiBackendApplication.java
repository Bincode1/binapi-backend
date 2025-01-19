package com.bin.binapibackend;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bin.binapiclientsdk", "com.bin.binapibackend"})
@MapperScan("com.bin.binapibackend.mapper")
@EnableDubbo
public class BinapiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinapiBackendApplication.class, args);
    }

}
