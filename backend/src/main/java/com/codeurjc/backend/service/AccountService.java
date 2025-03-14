package com.codeurjc.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

import javax.security.auth.Subject;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository; 

    public Optional<Account> getByEmail(String email){
        return accountRepository.findByEmail(email);
    }

    public Account getByNickName(String email){
        return accountRepository.findByNickName(email).get(0);
    }

    public boolean emailRepeat(String email){
        List<Account> accs = accountRepository.findAll();
        
        for(Account acc: accs){
            if(acc.getEmail().equals(email)){
                return false;
            }
        }
        return true;
    }

    public boolean nickNameRepeat(String nickName){
        List<Account> accs = accountRepository.findAll();
        
        for(Account acc: accs){
            if(acc.getNickName().equals(nickName)){
                return false;
            }
        }
        return true;
    }

    

    public void setAccount(Account acc){
        accountRepository.save(acc);
    }


    //accounts that arent friends to send friend request 
    public List<Account> getSearchingAccounts(String nickName, String myNickName){

        List<Account> lAccountsFiltered = accountRepository.findByNickNameContainingIgnoreCase(nickName);
        List<Account> lAccMy = accountRepository.findByNickName(myNickName);

        lAccountsFiltered.removeAll(lAccMy.get(0).getMyFriends());
        lAccountsFiltered.removeAll(lAccMy.get(0).getPendingFriends());
        lAccountsFiltered.removeAll(lAccMy.get(0).getRequestFriends());
        lAccountsFiltered.remove(lAccMy.get(0));

        return lAccountsFiltered;
    }

    //set friends request to account
    public void sendFriendRequest(Account myAcc, String nickNameToSend){

        List<Account> lAccToSend = accountRepository.findByNickName(nickNameToSend);

        
        lAccToSend.get(0).getPendingFriends().add(myAcc);
        myAcc.getRequestFriends().add(lAccToSend.get(0));

        lAccToSend.add(myAcc);

        accountRepository.saveAll(lAccToSend);
    }


    public Page<Account> getAllMyFriendsPage(String nickName, Pageable pageable) {
        return accountRepository.findAllMyFriends(nickName, pageable);
    }

    public Page<Account> getAllPendingFriendsPage(String nickName, Pageable pageable) {
        return accountRepository.findAllPendingFriends(nickName, pageable);
    }

    
}
