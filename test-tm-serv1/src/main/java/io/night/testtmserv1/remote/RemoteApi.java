package io.night.testtmserv1.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created By Night 2019/7/30
 */
@FeignClient("SERV2")
public interface RemoteApi {
    @GetMapping("/test2")
    public String postInfo();

}
