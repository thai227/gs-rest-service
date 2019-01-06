package com.thai.domain.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/txn")
public class TxnAPI {


    @Autowired
    TxnService txnService;

    @Autowired
    TxnDiffClass diffClass;

    // https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/transaction.html
    // Multiple Transaction Managers with @Transactional


    @DeleteMapping("/checked-ex/{id}")
    public String deleleWithCheckedException(@PathVariable int id) throws Exception {
        Logger logger = LoggerFactory.getLogger(TxnAPI.class);

        try {
            logger.debug("With CHECKED exception");
            txnService.withCheckedException(id); // => No transaction effect => Delete successfully
        } catch (Exception e) {
        }

        try {
            logger.debug("With UNCHECKED exception");
            txnService.withUncheckedException(id + 1); // Transaction effect => roll back => kept
        } catch (Exception e) {
        }
        return "ok";
    }

    // curl -X DELETE http://localhost:8080/txn/propagation/68
    @DeleteMapping("/propagation/{id}")
    public String deleleWithPropagation(@PathVariable int id) throws Exception {
        Logger logger = LoggerFactory.getLogger(TxnAPI.class);
        logger.debug("SubTransaction - Propagation");
        txnService.subTransactionRequire(id + 3);
        txnService.subTransactionRequireNew(id);
        return "ok";
    }

    // curl -X GET http://localhost:8080/txn/isolation-commited/69
    @GetMapping("/isolation-commited/{id}")
    public String deleleWithIsolationReadCommited(@PathVariable int id) throws Exception {
        Logger logger = LoggerFactory.getLogger(TxnAPI.class);
        logger.debug("Isolation - ReadCommited");
        txnService.isolationReadCommitedTxn1(id);
        txnService.isolationReadCommitedTxn2(id);
        return "ok";
    }

    // curl -X GET http://localhost:8080/txn/isolation-uncommited/69
    @GetMapping("/isolation-uncommited/{id}")
    public String deleleWithIsolationReadUncommited(@PathVariable int id) throws Exception {
        Logger logger = LoggerFactory.getLogger(TxnAPI.class);
        logger.debug("Isolation - Read uncommited");
        txnService.isolationReadUnCommitedTxn1(id);
        txnService.isolationReadUnCommitedTxn2(id);
        return "ok";
    }


}
