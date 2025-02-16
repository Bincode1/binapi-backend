package com.bin.binapibackend;

import com.bin.binapibackend.config.CorsProperties;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.bin.binapiclientsdk", "com.bin.binapibackend"})
@MapperScan("com.bin.binapibackend.mapper")
@EnableDubbo
@EnableConfigurationProperties(CorsProperties.class) // 启用配置属性
public class BinapiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinapiBackendApplication.class, args);
    }

}
