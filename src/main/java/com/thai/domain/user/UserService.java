package com.thai.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserRepo userRepo;

    public List<User> listAll() {
        logger.debug("Just test message =====================> ");
        return userRepo.findAll();
    }

    @Async
    public void create(String orgUuid) {

        long start = System.currentTimeMillis();

        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            User user = new User(orgUuid, "Thai " + i, new UserConfig(), EntityStatus.ACTIVE);
            userList.add(user);
        }
        userRepo.saveAll(userList);
        long end = System.currentTimeMillis();
        logger.debug("Duration ==============> ", end - start);

    }
}
