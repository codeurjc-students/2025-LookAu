package com.codeurjc.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.MediaType; 
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.repository.AccountRepository;
import com.codeurjc.backend.security.CSRFHandlerConfiguration;
import com.codeurjc.backend.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.file.Path;


@Controller
public class ProfileController {

    private final CSRFHandlerConfiguration CSRFHandlerConfiguration;

    private final AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    ProfileController(AccountRepository accountRepository, CSRFHandlerConfiguration CSRFHandlerConfiguration) {
        this.accountRepository = accountRepository;
        this.CSRFHandlerConfiguration = CSRFHandlerConfiguration;
    }

    /*@ModelAttribute
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
                
                Page<Account> myFriends = accountService.getAllMyFriendsPage(acc.getNickName(), PageRequest.of(0, 2));
                model.addAttribute("moreMyFriends", myFriends);    
                model.addAttribute("isEmptyMyFriends", myFriends.getTotalElements()>0.0);
                model.addAttribute("isLastPageMy", myFriends.isLast());
                
                Page<Account> pendingFriends = accountService.getAllPendingFriendsPage(acc.getNickName(), PageRequest.of(0, 2));
                model.addAttribute("morePendingFriends", pendingFriends);    
                model.addAttribute("isEmptyPendingFriends", pendingFriends.getTotalElements()>0.0);
                model.addAttribute("isLastPagePending", pendingFriends.isLast());
                
                Page<Account> requestFriends = accountService.getAllRequestFriendsPage(acc.getNickName(), PageRequest.of(0, 2));
                model.addAttribute("moreRequestFriends", requestFriends);    
                model.addAttribute("isEmptyRequestFriends", requestFriends.getTotalElements()>0.0);
                model.addAttribute("isLastPageRequest", requestFriends.isLast());

                Page<Account> deleteFriends = accountService.getAllMyFriendsPage(acc.getNickName(), PageRequest.of(0, 2));
                model.addAttribute("moreDeleteFriends", deleteFriends);    
                model.addAttribute("isEmptyDeleteFriends", deleteFriends.getTotalElements()>0.0);
                model.addAttribute("isLastPageDelete", deleteFriends.isLast());
            }   

        } else {
            model.addAttribute("logged", false); 
        }
    }*/



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
            modelAndView.setViewName("redirect:/login");
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
            modelAndView.setViewName("redirect:/login");
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
            modelAndView.setViewName("redirect:/login");
        }
        

        return modelAndView;
    }



    @GetMapping("/aceptPendingFriend/{nickName}")
    public ModelAndView aceptPendingFriend(HttpServletRequest request, @PathVariable String nickName) {

        ModelAndView modelAndView = new ModelAndView();

        Principal principal = request.getUserPrincipal();
        if (principal != null){

            //get and show account
            Optional<Account> accOpp = accountService.getByEmail(request.getUserPrincipal().getName());
            if (accOpp.isPresent()){
                Account acc = accOpp.get();
                accountService.aceptPendingFriend(acc, nickName);
                modelAndView.setViewName("redirect:/profile");
            }else{
                modelAndView.setViewName("error");
            }

            
        }else{
            modelAndView.setViewName("redirect:/login");
        }
        

        return modelAndView;
    }



    @GetMapping("/denyPendingFriend/{nickName}")
    public ModelAndView denyPendingFriend(HttpServletRequest request, @PathVariable String nickName) {

        ModelAndView modelAndView = new ModelAndView();

        Principal principal = request.getUserPrincipal();
        if (principal != null){

            //get and show account
            Optional<Account> accOpp = accountService.getByEmail(request.getUserPrincipal().getName());
            if (accOpp.isPresent()){
                Account acc = accOpp.get();
                accountService.denyPendingFriend(acc, nickName);
                modelAndView.setViewName("redirect:/profile");
            }else{
                modelAndView.setViewName("error");
            }

            
        }else{
            modelAndView.setViewName("redirect:/login");
        }
        

        return modelAndView;
    }



    /** EDIT PROFILE **/

    @GetMapping("/editProfile")
    public ModelAndView showEditProfile(HttpServletRequest request) {
        
        ModelAndView modelAndView = new ModelAndView();

        Principal principal = request.getUserPrincipal();
        if (principal != null){

            //get and show account
            Optional<Account> accOpp = accountService.getByEmail(request.getUserPrincipal().getName());
            if (accOpp.isPresent()){
                Account acc = accOpp.get();
                modelAndView.addObject("myAcc", acc);
                modelAndView.addObject("isProfile", false);
                modelAndView.addObject("isProfileEdit", true);

                modelAndView.setViewName("ticket");
            }else{
                modelAndView.setViewName("error");
            }

            
            
        }else{
            modelAndView.setViewName("redirect:/login");
        }
        
        return modelAndView;
    }   


    @PostMapping("/editProfile")
    public ModelAndView editarPerfil(HttpServletRequest request,
            Model model,
            @RequestParam("firstName") Optional<String> firstName,
            @RequestParam("lastName") Optional<String> lastName,
            @RequestParam("nickName") Optional<String> email,
            @RequestParam("password") Optional<String> password,
            @RequestParam("profilePicture") Optional<MultipartFile> file) throws ServletException, IOException {

        ModelAndView modelAndView = new ModelAndView();

        Principal principal = request.getUserPrincipal();
        if(principal!=null){

            Optional<Account> acc = accountService.getByEmail(principal.getName());
            if (acc.isPresent()) {
                Account myAcc = acc.get();
                
                myAcc.setFirstName(firstName.orElse(myAcc.getFirstName()));
                myAcc.setLastName(lastName.orElse(myAcc.getLastName()));
                myAcc.setEmail(email.orElse(myAcc.getEmail()));
                myAcc.setPassword(password.orElse(myAcc.getPassword()));
                
                if (file.isPresent() && file.get().getSize() > 0) {
                    try {
                        byte[] foto = file.get().getBytes();
                        myAcc.setProfilePicture(foto);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    byte[] fotoAnterior = myAcc.getProfilePicture();
                    myAcc.setProfilePicture(fotoAnterior);
                }
                accountService.setAccount(myAcc);
            }
            
            modelAndView.setViewName("redirect:/profile");

        }else{
            modelAndView.setViewName("redirect:/login");
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
            Path path = Paths.get("src\\main\\resources\\static\\images\\others\\flork_noprofile.jpg");
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
    
}
