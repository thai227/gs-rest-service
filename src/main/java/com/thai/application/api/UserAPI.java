package com.thai.application.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thai.domain.user.User;
import com.thai.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserAPI {

    @Autowired
    UserService userService;

    @GetMapping
    public List<User> list() {
        long start = System.currentTimeMillis();
        List<User> all = userService.listAll();
        long end = System.currentTimeMillis();
        String s= String.format("%s : %d", Thread.currentThread().getName(), end - start);
        System.out.println(s);
        return all;
    }

    @PostMapping
    public JsonNode create(){
        userService.create("Org 1");
        userService.create("Org 2");
        userService.create("Org 3");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("status", "ok");
        return objectNode;
    }


}
