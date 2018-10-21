package com.thai.application.api;

import com.thai.domain.async.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/async")
public class AsyncTest {
    static Logger logger = LoggerFactory.getLogger(AsyncTest.class);
    @Autowired
    AsyncService asyncService;

    @GetMapping
    public String test() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        logger.debug("I'm calling a long time process");
        Future<String> stringFuture = asyncService.testFutureReturn();
        logger.debug("I'm waiting for result - In that time, I do something ");
        Thread.sleep(2000);
        String s = stringFuture.get();
        logger.debug("Finally I can get the result: " + s);
        logger.debug("Total time: " + (System.currentTimeMillis() - startTime));
        return s;
    }

    @GetMapping("/threadpool")
    public String testThreadPool() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        logger.debug("I'm calling a long time process");
        for (int i = 0; i < 10; i++) {
            asyncService.testFutureReturn();
        }

        for (int i = 0; i < 10; i++) {
            asyncService.testNoReturn();
        }

        logger.debug("I'm waiting for result - In that time, I do something ");
        logger.debug("Total time: " + (System.currentTimeMillis() - startTime));
        return "ok";
    }

}
