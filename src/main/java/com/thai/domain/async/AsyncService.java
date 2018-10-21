package com.thai.domain.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.Future;

@Service
public class AsyncService {
    static Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Async
    public Future<String> testFutureReturn(long invokeTime) {
        try {
            long startTime = System.currentTimeMillis();
            logger.debug(String.format("Return function - Start async %s, thread name : %s ", startTime, Thread.currentThread().getName()));
            Thread.sleep(3000);
            logger.debug(String.format("Return function - End async. Total time from invoke: %s, thread name: %s", System.currentTimeMillis() - invokeTime, Thread.currentThread().getName()));
        } catch (Exception e) {
            logger.error("Error on aysnc", e);
        }
        return new AsyncResult<String>("I'm done");
    }


    @Async
    @Transactional
    public void testNoReturn(long invokeTime) {
        try {
            long startTime = System.currentTimeMillis();
            logger.debug(String.format("No return function -- Start async: %s, thread name: %s ", startTime, Thread.currentThread().getName()));
            Thread.sleep(5000);
            logger.debug(String.format("No return function -- End async. Total time from invoke time: %s, thread name: %s ", System.currentTimeMillis() - invokeTime, Thread.currentThread().getName()));
        } catch (Exception e) {
            logger.error("Error on aysnc", e);
        }
    }

}
