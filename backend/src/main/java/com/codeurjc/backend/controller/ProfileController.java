package com.codeurjc.backend.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private AccountService accountService;

    
    @GetMapping("/profile")
    public ModelAndView showProfile(HttpServletRequest request) {
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("isProfile", true);
        modelAndView.addObject("isProfileEdit", false);
        modelAndView.addObject("logged", true); 


        modelAndView.setViewName("ticket");
        return modelAndView;
    }

    @GetMapping("/editProfile")
    public ModelAndView showEditProfile(HttpServletRequest request) {
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("isProfileEdit", true);
        modelAndView.addObject("isProfile", false);
        modelAndView.addObject("logged", true); 


        modelAndView.setViewName("ticket");
        return modelAndView;
    }
    
}
