package com.StudyPal.Users.User;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("hello")
    public String sayHello(){
        return "hello";
    }
    @PostMapping("/register")
    public User register(@RequestBody RegisterationRequest user){
        return userService.registerUser(user);
    }
    @PostMapping("/login")
    public String login(@RequestBody User user){
        System.out.println("Login attempt for user: " + user.getName());
                return userService.verify(user);
    }

}
