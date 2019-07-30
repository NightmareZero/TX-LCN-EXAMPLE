package io.night.testtmserv3.controller;

import io.night.testtmserv3.remote.RemoteApi;
import io.night.testtmserv3.service.Test2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created By Night 2019/7/29
 */
@Controller("/")
public class Test1 {

    private Test2Service test2Service;


    @Autowired
    public Test1(Test2Service test2Service) {
        this.test2Service = test2Service;
    }


    @GetMapping("/test2")
    public ResponseEntity<String> postInfo() {
        boolean b = test2Service.doWork("hello");

        return ResponseEntity.ok("");
    }

}
