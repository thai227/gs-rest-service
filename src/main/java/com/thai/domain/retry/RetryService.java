package com.thai.domain.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
public class RetryService {
    static Logger logger = LoggerFactory.getLogger(RetryService.class);

    @Autowired
    RetryTemplate retryTemplate;

    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000))
    public String testRetry() throws Exception {
        logger.debug("On Retry service " + System.currentTimeMillis());
        int k = 1 / 0;
        return "xyz";
    }

    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000))
    public String testRetry2(String abc) throws Exception {
        logger.debug("On Retry2 service " + System.currentTimeMillis());
        int k = 1 / 0;
        return "xyz";
    }


    @Recover
    public String recoverTest(Exception e) {
        logger.debug("On recover ... ");
        return "From recover";
    }


    public String withRetryTemplate(String s) {
        String execute = retryTemplate.execute(arg0 -> {
            logger.debug("On retry template " + System.currentTimeMillis());
            logger.debug("retry count " + arg0.getRetryCount());
            logger.debug("function parameters " + s);
            int k = 1 / 0;
            return "xyz";
        }, arg1 -> {
            return "abc";
        });
        return execute;
    }

}
