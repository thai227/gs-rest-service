package com.thai.domain.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class RetryService {
    static Logger logger = LoggerFactory.getLogger(RetryService.class);

    @Retryable(
            value = {Exception.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000))
    public void testRetry() throws Exception {
        logger.debug("On Retry service " + System.currentTimeMillis());
        int k = 1 / 0;

    }
}
