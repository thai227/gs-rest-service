package com.thai.domain.transaction;

import com.thai.domain.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TxnDiffClass {
    @Autowired
    UserRepo userRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testSubTransaction(int id) {
        userRepo.deleteById(id + 1);
        int i = 1 / 0;
    }

}
