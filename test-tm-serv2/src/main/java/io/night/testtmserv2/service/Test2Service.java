package io.night.testtmserv2.service;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.TccTransaction;
import io.night.testtmserv2.remote.RemoteApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created By Night 2019/7/29
 */
@Service
public class Test2Service {

    public Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @Autowired
    public Test2Service(RemoteApi remoteApi) {
        this.remoteApi = remoteApi;
    }

    private RemoteApi remoteApi;


    @TccTransaction(propagation = DTXPropagation.SUPPORTS, confirmMethod = "doWorkConfirm", cancelMethod = "doWorkCancel")
    @Transactional
    public boolean doWork(String val) {
        logger.info("<======serv2--doWork======>");

        String s = remoteApi.postInfo();
//        throw new RuntimeException("");
        logger.info("<======serv2--finishwork======>");
        return false;
    }


    public void doWorkConfirm(String val) {
        logger.info("<======serv2--confirm======>" + val);
    }

    public void doWorkCancel(String val) {
        logger.info("<======serv2--cancel======>" + val);
    }
}
