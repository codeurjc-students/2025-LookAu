package com.codeurjc.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType; 
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.repository.AccountRepository;
import com.codeurjc.backend.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.file.Path;


@Controller
public class ProfileController {

    private final AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    ProfileController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

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
                model.addAttribute("pendingFriends", acc.getPendingFriends());
                
                model.addAttribute("hasNotification", acc.getPendingFriends().size()>0); 
                

                model.addAttribute("moreMyFriends", accountService.getAllMyFriendsPage(acc.getNickName(), PageRequest.of(0, 2)));
                model.addAttribute("isEmptyMyFriends", accountService.getAllMyFriendsPage(acc.getNickName(), PageRequest.of(0, 2)).getSize()>0);
            
                model.addAttribute("morePendingFriends", accountService.getAllPendingFriendsPage(acc.getNickName(), PageRequest.of(0, 2)));    
                model.addAttribute("isEmptyPendingFriends", accountService.getAllPendingFriendsPage(acc.getNickName(), PageRequest.of(0, 2)).getTotalElements()>0.0);
            }   

        } else {
            model.addAttribute("logged", false); 
        }
    }



    /** PROFILE **/
    
    @GetMapping("/profile")
    public ModelAndView showProfile(HttpServletRequest request) {
        
        ModelAndView modelAndView = new ModelAndView();

        Principal principal = request.getUserPrincipal();
        if (principal != null){
            modelAndView.addObject("isProfile", true);
            modelAndView.addObject("isProfileEdit", false);
            modelAndView.setViewName("ticket");

            //get and show account
            Optional<Account> accOpp = accountService.getByEmail(request.getUserPrincipal().getName());
            if (accOpp.isPresent()){
                Account acc = accOpp.get();
                modelAndView.addObject("firstName", acc.getFirstName()); 
                modelAndView.addObject("lastName", acc.getLastName()); 
                modelAndView.addObject("nickName", acc.getNickName()); 
                
                

                
            }else{
                modelAndView.setViewName("error");
            }


        }else{
            modelAndView.setViewName("login");
        }
        
        return modelAndView;
    }



    @GetMapping("/searchAccounts")
    public ModelAndView showSearchAccounts(HttpServletRequest request, HttpServletResponse response, Model model,
        @RequestParam String stringToFind) {
        
        ModelAndView modelAndView = new ModelAndView();

        Principal principal = request.getUserPrincipal();
        if (principal != null){

            //get and show account
            Optional<Account> accOpp = accountService.getByEmail(request.getUserPrincipal().getName());
            if (accOpp.isPresent()){
                Account acc = accOpp.get();
                modelAndView.addObject("firstName", acc.getFirstName()); 
                modelAndView.addObject("lastName", acc.getLastName()); 
                modelAndView.addObject("nickName", acc.getNickName()); 

                //if list is empty dont show accounts
                if(stringToFind==""){
                    modelAndView.addObject("searching", false);
                }else{

                    modelAndView.addObject("searching", true);

                    //search account containing the string
                    List<Account> lAccount = accountService.getSearchingAccounts(stringToFind, acc.getNickName());

                    modelAndView.addObject("isEmptySearchAccounts", lAccount.isEmpty());
                    modelAndView.addObject("searchAccounts", lAccount);
                }

                modelAndView.addObject("isProfile", true);
                modelAndView.addObject("isProfileEdit", false);
                
                modelAndView.setViewName("ticket");

            }else{
                modelAndView.setViewName("error");
            }
            
            
        }else{
            modelAndView.setViewName("login");
        }
        
        return modelAndView;
    }   


    @GetMapping("/sendPendingFriend/{nickName}")
    public ModelAndView sendPendingFriend(HttpServletRequest request, @PathVariable String nickName) {

        ModelAndView modelAndView = new ModelAndView();

        Principal principal = request.getUserPrincipal();
        if (principal != null){

            //get and show account
            Optional<Account> accOpp = accountService.getByEmail(request.getUserPrincipal().getName());
            if (accOpp.isPresent()){
                Account acc = accOpp.get();
                accountService.sendFriendRequest(acc, nickName);
                modelAndView.setViewName("redirect:/profile");
            }else{
                modelAndView.setViewName("error");
            }

            
        }else{
            modelAndView.setViewName("login");
        }
        

        return modelAndView;
    }



    /** EDIT PROFILE **/

    @GetMapping("/editProfile")
    public ModelAndView showEditProfile(HttpServletRequest request) {
        
        ModelAndView modelAndView = new ModelAndView();

        Principal principal = request.getUserPrincipal();
        if (principal != null){
            modelAndView.addObject("isProfile", false);
            modelAndView.addObject("isProfileEdit", true);
            modelAndView.setViewName("ticket");
        }else{
            modelAndView.setViewName("login");
        }
        
        return modelAndView;
    }   


    /** PHOTO **/

    @GetMapping("/acc/photo")
    public ResponseEntity<byte[]> getCurrentUserPhoto(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            Optional<Account> accOptional = accountService.getByEmail(principal.getName());
            if (accOptional.isPresent()) {
                byte[] imageBytes = accOptional.get().getProfilePicture();
                if (imageBytes != null && imageBytes.length > 0) {
                    return ResponseEntity
                            .ok()
                            .contentType(MediaType.IMAGE_JPEG) // adjust immage photo
                            .body(imageBytes);
                }
            }
        }
        
        return ResponseEntity       // if the user doesnt have photo or isnt logged, return the default
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(loadDefaultImage());
    }


    @GetMapping("/acc/photoFriend/{nickName}")
    public ResponseEntity<byte[]> getAccountPhoto(HttpServletRequest request, @PathVariable String nickName) {
        
        Account acc = accountService.getByNickName(nickName);

        if (acc!=null) {
            
            byte[] imageBytes = acc.getProfilePicture();
            if (imageBytes != null && imageBytes.length > 0) {
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_JPEG) // adjust immage photo
                        .body(imageBytes);
            }
            
        }
        
        return ResponseEntity       // if the user doesnt have photo or isnt logged, return the default
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(loadDefaultImage());
    }


    private byte[] loadDefaultImage() {
        try {
            Path path = Paths.get("src\\main\\resources\\static\\images\\imagenFotoPerfil.jpg");
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
    
}
