package com.thai.domain.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/txn")
public class TxnAPI {


    @Autowired
    TxnService txnService;

    // https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/transaction.html
    // Multiple Transaction Managers with @Transactional


    @DeleteMapping("/{id}")
    public String delele(@PathVariable int id) throws Exception {
        Logger logger = LoggerFactory.getLogger(TxnAPI.class);

        /*try {
            logger.debug("With CHECKED exception");
            txnService.withCheckedException(id); // => No transaction effect => Delete successfully
        }catch (Exception e){} */

        /*try {
            logger.debug("With UNCHECKED exception");
            txnService.withUncheckedException(id); // Transaction effect => roll back => kept
        } catch (Exception e) {
        }*/

        logger.debug("SubTransaction");
        txnService.subTransactionRequire(id + 3);
        txnService.subTransactionRequireNew(id);
        return "ok";
    }

}
