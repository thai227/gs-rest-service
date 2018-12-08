package com.thai.domain.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retry")
public class RetryAPI {
    static Logger logger = LoggerFactory.getLogger(RetryAPI.class);
    @Autowired
    RetryService retryService;

    @GetMapping
    public String test() {
        logger.debug("On retry api");
        try {
            String s = retryService.testRetry();
            logger.debug("Function return: " + s);
            String ss = retryService.withRetryTemplate("parameters");
            logger.debug("Function return: " + ss);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
