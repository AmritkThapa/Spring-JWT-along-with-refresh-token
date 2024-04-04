package com.jwt.example.JwtExample.service;

import com.jwt.example.JwtExample.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    List<User> store=new ArrayList<>();

    public UserService(){
        store.add(new User(UUID.randomUUID().toString(),"Amrit Thapa","amrit@mail"));
        store.add(new User(UUID.randomUUID().toString(),"Shiroe Thapa","shiroe@mail"));
        store.add(new User(UUID.randomUUID().toString(),"Amrit Magar","amrit@mail"));
    }

    public List<User> getUsers(){
        return this.store;
    }
}
