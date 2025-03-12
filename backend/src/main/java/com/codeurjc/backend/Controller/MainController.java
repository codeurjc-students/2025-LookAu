package com.codeurjc.backend.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.service.AccountService;

import org.springframework.ui.Model; 
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @Autowired
    private AccountService accountService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            model.addAttribute("logged", true); 
        } else {
            model.addAttribute("logged", false); 
        }
    }


    @GetMapping("/logout")
    public ModelAndView showGLogout() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }

    @PostMapping("/logout")
    public ModelAndView showLogout() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView showLogin(@RequestParam(value = "error", required = false) String error) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("showError", false);
        modelAndView.addObject("error", "");

        if(error==null || "false".equals(error)){
            modelAndView.addObject("showError", false);        
            modelAndView.setViewName("login");
        }else{
            modelAndView.addObject("showError", true);
            modelAndView.addObject("error", "Wrong user or password");
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    

    @GetMapping("/signup")
    public ModelAndView showSignup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("showError", false);
        modelAndView.addObject("error", "");
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView setSignup(HttpServletRequest request, HttpServletResponse response, Model model,
        @RequestParam String firstName,
        @RequestParam String lastName,
        @RequestParam String email,
        @RequestParam String rpassword,
        @RequestParam String password,
        @RequestParam String nickName){
        

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("showError", false);
        modelAndView.addObject("error", "");

        if(!firstName.equals("") && !lastName.equals("") && !email.equals("") && !rpassword.equals("") && !password.equals("") && !nickName.equals("")){
            if (accountService.emailRepeat(email)){

                if (accountService.nickNameRepeat(nickName)){

                    if (!password.equals(rpassword)) { 
                        modelAndView.addObject("showError", true);
                        modelAndView.addObject("error", "Passwords are not the same");
                        modelAndView.setViewName("signup");
                    }else{
                        Account acc = new Account(nickName, firstName, lastName, email, password);
                        accountService.setAccount(acc);
                        modelAndView.setViewName("login");
                    }

                }else{
                    modelAndView.addObject("showError", true);
                    modelAndView.addObject("error", "There is already an account with this nick name");
                    modelAndView.setViewName("signup");
                }

                
                

            } else {
                modelAndView.addObject("showError", true);
                modelAndView.addObject("error", "There is already an account with this email");
                modelAndView.setViewName("signup");
            }
        }else{
            modelAndView.addObject("showError", true);
            modelAndView.addObject("error", "Complete all the fields");
            modelAndView.setViewName("signup");
        }
        return modelAndView;
    }
}
