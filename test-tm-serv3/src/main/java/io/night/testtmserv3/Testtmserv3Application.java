package io.night.testtmserv3;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDistributedTransaction
@EnableFeignClients
@EnableEurekaClient
@EnableDiscoveryClient
public class Testtmserv3Application {

    public static void main(String[] args) {
        SpringApplication.run(Testtmserv3Application.class, args);
    }

}
