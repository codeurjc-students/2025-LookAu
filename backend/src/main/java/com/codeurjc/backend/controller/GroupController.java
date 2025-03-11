package com.codeurjc.backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GroupController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/groups")
    public ModelAndView home(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Account> accOpp = accountService.getByEmail(request.getUserPrincipal().getName());

        modelAndView.addObject("hasNotification", true);

		if (accOpp.isPresent()){

            Account acc = accOpp.get();

            modelAndView.addObject("hasNotification", acc.getPendingFriends().size()>0); 
        }

    

        modelAndView.addObject("logged", true); 
        modelAndView.setViewName("groups");
        return modelAndView;
    }
    
}
