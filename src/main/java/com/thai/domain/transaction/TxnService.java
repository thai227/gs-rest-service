package com.thai.domain.transaction;

import com.thai.domain.user.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TxnService {

    static Logger logger = LoggerFactory.getLogger(TxnService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    TxnDiffClass txnDiffClass;


    @Transactional
    public void withUncheckedException(int id) {
        userRepo.deleteById(id);
        int i = 1 / 0;
    }

    @Transactional
    public void withCheckedException(int id) throws Exception {
        userRepo.deleteById(id);
        throw new Exception();
    }


    @Async
    @Transactional
    public void subTransactionRequire(int id) {
        logger.debug("Processing Id" + id);
        try {
            txnDiffClass.testSubTransactionRequire(id + 1);
        } catch (Exception e) {
            logger.error("Error on testSubTransaction REQUIRES");
        }
        userRepo.deleteById(id + 2);
    }

    @Async
    @Transactional
    public void subTransactionRequireNew(int id) {
        logger.debug("Processing Id" + id);
        userRepo.deleteById(id);
        try {
            txnDiffClass.testSubTransactionRequireNew(id + 1);
        } catch (Exception e) {
            logger.error("Error on testSubTransaction REQUIRED_NEW");
        }
        userRepo.deleteById(id + 2);
    }


}
