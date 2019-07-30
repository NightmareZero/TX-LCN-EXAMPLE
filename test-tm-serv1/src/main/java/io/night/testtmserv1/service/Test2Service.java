package io.night.testtmserv1.service;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.TccTransaction;
import io.night.testtmserv1.remote.RemoteApi;
import io.night.testtmserv1.remote.RemoteApi3;
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
    public Test2Service(RemoteApi remoteApi, RemoteApi3 remoteApi3) {
        this.remoteApi = remoteApi;
        this.remoteApi3 = remoteApi3;
    }

    private RemoteApi remoteApi;

    private RemoteApi3 remoteApi3;


    @TccTransaction(propagation = DTXPropagation.REQUIRED, confirmMethod = "doWorkConfirm", cancelMethod = "doWorkCancel")
    @Transactional
    public boolean doWork(String val) {
        logger.info("doWork");
        String s = remoteApi.postInfo();
        if (true) {
            throw new RuntimeException("");
        }
        logger.info("finishwork");

        return false;

    }


    public void doWorkConfirm(String val) {
        logger.info("------serv1--confirm--" + val);
    }

    public void doWorkCancel(String val) {
        logger.info("------serv1--cancel--" + val);
    }
}
