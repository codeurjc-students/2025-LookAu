package com.codeurjc.backend.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PersonalController {

    @Autowired
    private AccountService accountService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {

            //LOGGED
            model.addAttribute("logged", true); 

            //NOTIFICATION
            Optional<Account> accOpp = accountService.getByEmail(request.getUserPrincipal().getName());
            model.addAttribute("hasNotification", true);
            if (accOpp.isPresent()){
                Account acc = accOpp.get();
                model.addAttribute("hasNotification", acc.getPendingFriends().size()>0); 
            }

        } else {
            model.addAttribute("logged", false); 
        }
    }




    @GetMapping("/personal")
    public ModelAndView home(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        Principal principal = request.getUserPrincipal();
        if (principal != null){
            modelAndView.setViewName("ticket");
        }else{
            modelAndView.setViewName("login");
        }
        
        return modelAndView;
    }
    
}
