package io.night.testtmserv3.service;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.TccTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created By Night 2019/7/29
 */
@Service
public class Test2Service {

    public Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @TccTransaction(propagation = DTXPropagation.SUPPORTS, confirmMethod = "doWorkConfirm", cancelMethod = "doWorkCancel")
    @Transactional
    public boolean doWork(String val) {
        logger.info("<======serv3--doWork======>");
//        throw new RuntimeException("");
        logger.info("<======serv3--finishwork======>");
        return false;
    }


    public void doWorkConfirm(String val) {
        logger.info("<======serv3--confirm======>" + val);
    }

    public void doWorkCancel(String val) {
        logger.info("<======serv3--cancel======>" + val);
    }
}
