package com.dugq.controller;

import com.dugq.cache.jpa.UserRepository;
import com.dugq.cache.jpa.model.ImUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("userInfo")
    public ImUser getUserInfo(Long userId){
        return userRepository.findById(userId);
    }

    @GetMapping("login")
    public ImUser login(String account, String password){
        return userRepository.findByAccountAndPassword(account,password);
    }

    @GetMapping("search")
    public ImUser search(String nickname){
        return userRepository.findByNickNameLike(nickname);
    }
}
