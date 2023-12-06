package me.heartmon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HeartmonApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeartmonApplication.class, args);
    }

}
