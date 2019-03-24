package com.thai.domain.greeting;

import com.thai.domain.greeting.Greeting;
import com.thai.domain.user.EntityStatus;
import com.thai.domain.user.User;
import com.thai.domain.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/greeting")
public class GreetingAPI {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @PostMapping("/user")
    public User greeting(@RequestBody Map<String, Object> user) {
        System.out.println(user);
        //user.status = EntityStatus.ACTIVE;
        //User save = userRepo.save(user);
        return new User("Thai", "123456");
    }
}
