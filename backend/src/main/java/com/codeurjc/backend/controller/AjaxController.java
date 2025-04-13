package com.codeurjc.backend.controller;

import java.security.Principal;
import java.util.Optional;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AjaxController {

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
                model.addAttribute("requestFriends", acc.getRequestFriends());      
                
                model.addAttribute("hasNotification", acc.getPendingFriends().size()>0); 
                

                model.addAttribute("moreMyFriends", accountService.getAllMyFriendsPage(acc.getNickName(), PageRequest.of(0, 2)));
                model.addAttribute("isEmptyMyFriends", accountService.getAllMyFriendsPage(acc.getNickName(), PageRequest.of(0, 2)).getSize()>0);
            
                model.addAttribute("morePendingFriends", accountService.getAllPendingFriendsPage(acc.getNickName(), PageRequest.of(0, 2)));    
                model.addAttribute("isEmptyPendingFriends", accountService.getAllPendingFriendsPage(acc.getNickName(), PageRequest.of(0, 2)).getTotalElements()>0.0);
            
                model.addAttribute("moreRequestFriends", accountService.getAllRequestFriendsPage(acc.getNickName(), PageRequest.of(0, 2)));    
                model.addAttribute("isEmptyRequestFriends", accountService.getAllRequestFriendsPage(acc.getNickName(), PageRequest.of(0, 2)).getTotalElements()>0.0);
                
                model.addAttribute("moreDeleteFriends", accountService.getAllMyFriendsPage(acc.getNickName(), PageRequest.of(0, 2)));    
                model.addAttribute("isEmptyDeleteFriends", accountService.getAllMyFriendsPage(acc.getNickName(), PageRequest.of(0, 2)).getTotalElements()>0.0);
            }   

        } else {
            model.addAttribute("logged", false); 
        }
    }

    

    /*@GetMapping("/moreMyFriends")
	public String getMoreAccountsPage(Model model, HttpServletRequest request, @RequestParam int page) {

        Principal principal = request.getUserPrincipal();
        if (principal != null){

            Optional<Account> accOpp = accountService.getByEmail(request.getUserPrincipal().getName());

            if(accOpp.isPresent()){
                Account acc = accOpp.get();
                Page<Account> moreSubjects = accountService.getAllMyFriendsPage(acc.getNickName(), PageRequest.of(page, 2));
                if (moreSubjects == null) {
                    return null;
                } else {
                    model.addAttribute("moreMyFriends", moreSubjects);
                    model.addAttribute("isLastPage", moreSubjects.isLast());
                    return "ajax/moreMyFriends";
                }
            }
        }
        return null;
	}*/

    /*@GetMapping("/morePendingFriends")
	public String getPendingAccountsPage(Model model, HttpServletRequest request, @RequestParam int page) {

        Principal principal = request.getUserPrincipal();
        if (principal != null){

            Optional<Account> accOpp = accountService.getByEmail(request.getUserPrincipal().getName());

            if(accOpp.isPresent()){
                Account acc = accOpp.get();
                Page<Account> moreSubjects = accountService.getAllPendingFriendsPage(acc.getNickName(), PageRequest.of(page, 2));
                if (moreSubjects == null) {
                    return null;
                } else {
                    model.addAttribute("morePendingFriends", moreSubjects);
                    model.addAttribute("isLastPage", moreSubjects.isLast());
                    return "ajax/morePendingFriends";
                }
            }
        }
        return null;
	}

    /*@GetMapping("/moreDeleteFriends")
	public String getMoreDeleteFriends(Model model, HttpServletRequest request, @RequestParam int page) {

        Principal principal = request.getUserPrincipal();
        if (principal != null){

            Optional<Account> accOpp = accountService.getByEmail(request.getUserPrincipal().getName());

            if(accOpp.isPresent()){
                Account acc = accOpp.get();
                Page<Account> moreSubjects = accountService.getAllMyFriendsPage(acc.getNickName(), PageRequest.of(page, 2));
                if (moreSubjects == null) {
                    return null;
                } else {
                    model.addAttribute("moreDeleteFriends", moreSubjects);
                    model.addAttribute("isLastPageDelete", moreSubjects.isLast());
                    return "ajax/moreDeleteFriends";
                }
            }
        }
        return null;
	}*/

}