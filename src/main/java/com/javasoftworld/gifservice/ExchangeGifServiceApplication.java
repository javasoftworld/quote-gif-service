package com.javasoftworld.gifservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ExchangeGifServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeGifServiceApplication.class, args);
    }

}
