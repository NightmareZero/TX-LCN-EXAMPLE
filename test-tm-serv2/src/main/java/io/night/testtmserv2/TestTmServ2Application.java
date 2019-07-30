package io.night.testtmserv2;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDistributedTransaction
@EnableFeignClients
@EnableDiscoveryClient
public class TestTmServ2Application {

    public static void main(String[] args) {
        SpringApplication.run(TestTmServ2Application.class, args);
    }

}
