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
import org.springframework.web.bind.annotation.RequestParam;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AjaxController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/moreMyFriends")
	public String getMoreAccounts(Model model, HttpServletRequest request, @RequestParam int page) {

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
	}


    @GetMapping("/morePendingFriends")
	public String getPendingAccounts(Model model, HttpServletRequest request, @RequestParam int page) {

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

}