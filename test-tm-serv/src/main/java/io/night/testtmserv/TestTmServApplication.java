package io.night.testtmserv;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableTransactionManagerServer
public class TestTmServApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTmServApplication.class, args);
    }

}
