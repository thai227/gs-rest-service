package com.thai.domain.transaction;

import com.thai.domain.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TxnService {

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

    //@Async
    @Transactional
    public void subTransaction(int id) {
        try {
            userRepo.deleteById(id);
            txnDiffClass.testSubTransaction(id);
            userRepo.deleteById(id + 2);
        } catch (Exception e) {

        }
    }



}
