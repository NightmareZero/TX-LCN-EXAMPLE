package io.night.eserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EservApplication {

    public static void main(String[] args) {
        SpringApplication.run(EservApplication.class, args);
    }

}
