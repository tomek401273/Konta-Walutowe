package com.tgrajkowski;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KontaWalutoweApplication {

    public static void main(String[] args) {
        SpringApplication.run(KontaWalutoweApplication.class, args);
    }

}
