package com.ptb.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by zuokui.fu on 2016/10/18.
 */
@SpringBootApplication
@ImportResource("classpath:dubbo_provider.xml")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}