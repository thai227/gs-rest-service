package com.thai.domain.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class AsyncService {
    static Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Async
    public Future<String> testFutureReturn() {
        try {
            long startTime = System.currentTimeMillis();
            logger.debug(String.format("Return function - Start async %s, thread name : %s ", startTime, Thread.currentThread().getName()));
            Thread.sleep(3000);
            logger.debug(String.format("Return function - End async. Total time: %s, thread name: %s", System.currentTimeMillis() - startTime, Thread.currentThread().getName()));
        } catch (Exception e) {
            logger.error("Error on aysnc", e);
        }
        return new AsyncResult<String>("I'm done");
    }


    @Async
    public void testNoReturn() {
        try {
            long startTime = System.currentTimeMillis();
            logger.debug("No return function -- Start async " + startTime + " " + Thread.currentThread().getName());
            Thread.sleep(5000);
            logger.debug("No return function -- End async and return " + (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            logger.error("Error on aysnc", e);
        }
    }

}
