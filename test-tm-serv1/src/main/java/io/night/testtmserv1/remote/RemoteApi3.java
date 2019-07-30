package io.night.testtmserv1.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created By Night 2019/7/30
 */
@FeignClient("SERV3")
public interface RemoteApi3 {
    @GetMapping("/test2")
    public String postInfo();

}
