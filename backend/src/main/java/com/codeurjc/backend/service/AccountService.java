package com.codeurjc.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

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
        lAccountsFiltered.remove(lAccMy.get(0));

        return lAccountsFiltered;
    }

    //set friends request to account
    public void sendFriendRequest(Account myAcc, String nickNameToSend){

        List<Account> lAccMy = accountRepository.findByNickName(nickNameToSend);
        
        lAccMy.get(0).getPendingFriends().add(myAcc);

        accountRepository.save(lAccMy.get(0));
    }
}
