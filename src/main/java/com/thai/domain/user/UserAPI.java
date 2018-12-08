package com.thai.domain.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thai.domain.user.User;
import com.thai.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/user")
public class UserAPI {

    @Autowired
    UserService userService;

    @GetMapping
    public String list() {
        System.out.println("On request " + Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<User> all = userService.listAll();
        long end = System.currentTimeMillis();
        String s = String.format("Total process time %s : %d ms", Thread.currentThread().getName(), end - start);
        System.out.println(s);
        return "OK " + all.size();
    }

    @GetMapping("/org_uuid")
    public String listByOrg() {
        System.out.println("API   request " + Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        userService.listByOrgUuid("2");
        long end = System.currentTimeMillis();
        String s = String.format("Total     API %s : %d ms", Thread.currentThread().getName(), end - start);
        System.out.println(s);
        return "OK ";
    }


    @PostMapping
    public JsonNode create() {
        userService.create("Org 1");
        userService.create("Org 2");
        userService.create("Org 3");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("status", "ok");
        return objectNode;
    }

    @PutMapping
    public String update() {
        List<User> all = userService.listAll();

        Random random = new Random();
        List<User> collect = IntStream.range(1, 100).mapToObj(i -> {
            User user = all.get(random.nextInt(10000));
            user.orgUuid = "2";
            return user;
        }).collect(Collectors.toList());
        userService.save(collect);
        return "ok";
    }


}
