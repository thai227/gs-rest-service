package com.thai.domain.transaction;

import com.thai.domain.user.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TxnDiffClass {
    static Logger logger = LoggerFactory.getLogger(TxnDiffClass.class);

    @Autowired
    UserRepo userRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testSubTransactionRequireNew(int id) {
        userRepo.deleteById(id);
        int i = 1 / 0;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void testSubTransactionRequire(int id) {
        userRepo.deleteById(id);
        int i = 1 / 0;
    }




}
