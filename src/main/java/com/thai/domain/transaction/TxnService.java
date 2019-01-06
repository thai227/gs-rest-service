package com.thai.domain.transaction;

import com.thai.domain.user.User;
import com.thai.domain.user.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

// https://sqlwithmanoj.com/2014/09/28/db-basics-what-are-acid-properties-of-a-transaction-in-an-rdbms/

@Service
public class TxnService {

    static Logger logger = LoggerFactory.getLogger(TxnService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    TxnDiffClass txnDiffClass;


    @Transactional
    public void withUncheckedException(int id) {
        //userRepo.deleteById(id);
        User user = userRepo.findById(id).get();
        user.orgUuid = "bac";
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


    @Async
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void isolationReadCommitedTxn1(int id) {
        logger.debug("Txn1 Processing Id: " + id);
        try {
            Thread.sleep(1000l);
            User user = userRepo.findById(id).get();
            user.orgUuid = String.valueOf(Integer.parseInt(user.orgUuid) + 1);
            userRepo.save(user);
            logger.debug("Commit thread 1");
            logger.debug("New OrgUuid in thread 1: " + user.orgUuid);
        } catch (Exception e) {
            logger.error("Error on testSubTransaction REQUIRES");
        }
    }

    @Async
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void isolationReadCommitedTxn2(int id) {
        logger.debug("Txn2 Processing Id: " + id);
        try {
            // logger.debug("OrgUuid in thread 2, before another txn update data: " + userRepo.findByIdManually(id).orgUuid); // notice that this function will enable local cache
            Thread.sleep(3000l);
            logger.debug("OrgUuid in thread 2, after another tnx update data: " + userRepo.findById(id).get().orgUuid);
        } catch (Exception e) {
            logger.error("Error on testSubTransaction REQUIRES");
        }
    }


    @Async
    @Transactional
    public void isolationReadUnCommitedTxn1(int id) {
        logger.debug("Txn1 Processing Id: " + id);
        try {
            User user = userRepo.findById(id).get();
            user.orgUuid = String.valueOf(Integer.parseInt(user.orgUuid) + 1);
            //userRepo.save(user);
            logger.debug("New OrgUuid in thread 1: " + user.orgUuid);

            //Thread.sleep(3000l);
            logger.debug("Rollback thread 1");
            int i = 1 / 0;

        } catch (Exception e) {
            logger.error("Error on isolationReadUnCommitedTxn1 REQUIRES");
        }
    }

    @Async
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void isolationReadUnCommitedTxn2(int id) {
        logger.debug("Txn2 Processing Id: " + id);
        try {
            // logger.debug("OrgUuid in thread 2, before another txn update data: " + userRepo.findByIdManually(id).orgUuid); // notice that this function will enable local cache
            Thread.sleep(2000l);
            logger.debug("OrgUuid in thread 2, after another tnx update data: " + userRepo.findById(id).get().orgUuid);
        } catch (Exception e) {
            logger.error("Error on testSubTransaction REQUIRES");
        }
    }

}
