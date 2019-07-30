package io.night.testtmserv2.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created By Night 2019/7/30
 */
@FeignClient("SERV3")
public interface RemoteApi {
    @GetMapping("/test2")
    public String postInfo();

}
