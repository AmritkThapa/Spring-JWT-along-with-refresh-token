package com.jwt.example.JwtExample.controllers;

import com.jwt.example.JwtExample.models.User;
import com.jwt.example.JwtExample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    //http://localhost:8081/home/users
    @GetMapping("/users")
    public List<User> getUser() {
        System.out.println("Got Users");
        return userService.getUsers();
    }

    @GetMapping("current-user")
    public String getLoggedInUser(Principal principal){
        return principal.getName();
    }


}
