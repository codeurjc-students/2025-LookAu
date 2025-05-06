package com.codeurjc.backend.service;
import com.codeurjc.backend.security.CSRFHandlerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.Team;
import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.DTO.AccountDTO;
import com.codeurjc.backend.model.DTO.TeamDTO;
import com.codeurjc.backend.repository.AccountRepository;
import com.codeurjc.backend.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final CSRFHandlerConfiguration CSRFHandlerConfiguration;

    @Autowired
    private TicketRepository tickectRepository;

    TicketService(CSRFHandlerConfiguration CSRFHandlerConfiguration) {
        this.CSRFHandlerConfiguration = CSRFHandlerConfiguration;
    } 

    public Optional<Ticket> getById(Long id){
        return tickectRepository.findById(id);
    }

    public void setTickect(Ticket acc){
        tickectRepository.save(acc);
    }

    public void setTickects(List<Ticket> lTickets){
        tickectRepository.saveAll(lTickets);
    }

//     public List<Account> getAllByNicknames(List<String> nicknames) {
//         return tickectRepository.findAllByNickname(nicknames);
//     }

//     public List<String> getSearchingMyFriends(String searchTerm, List<Tickect> lMyFriends) {

//         if(!lMyFriends.isEmpty()){
//             return lMyFriends.stream()
//             .filter(item -> item.getNickName() != null && item.getNickName().toLowerCase().contains(searchTerm.toLowerCase()))
//             .map(this::convertToString)
//             .collect(Collectors.toList());
//         }else{
//             return new ArrayList<String>();
//         }
//     }



//     /************************/
//     /******* REGISTER *******/
//     /************************/

//     public boolean emailRepeat(String email){
//         List<Account> accs = accountRepository.findAll();
        
//         for(Account acc: accs){
//             if(acc.getEmail().equals(email)){
//                 return true;
//             }
//         }
//         return false;
//     }

//     public boolean nickNameRepeat(String nickName){
//         return isAccountInList(accountRepository.findAll(), nickName);
//     }

    

//     /*********************************/
//     /******* FRIENDS (Profile) *******/
//     /*********************************/


//     /// CONSULT /////
     
//     seach if the account are in pending currentUser friends and if the currentUser are in the request account list
//     public Boolean isInPendingAndRequestFriends(Account acc, String nickNameRequest){

//         List<Account> lAccRquest = accountRepository.findByNickName(nickNameRequest);

//         if(!lAccRquest.isEmpty()){
//             return isAccountInList(acc.getPendingFriends(), nickNameRequest) && isAccountInList(lAccRquest.get(0).getRequestFriends(), acc.getNickName());
//         }else{
//             return false;
//         } 
//     }

//     seach if the account are in myfriends currentUser and if the currentUser are in myfriends account list
//     public Boolean isInMyFriends(Account acc, String nickNameRequest){

//         List<Account> lAccRquest = accountRepository.findByNickName(nickNameRequest);

//         if(!lAccRquest.isEmpty()){
//             return isAccountInList(acc.getMyFriends(), nickNameRequest) && isAccountInList(lAccRquest.get(0).getMyFriends(), acc.getNickName());
//         }else{
//             return false;
//         } 
//     }

    



//     /// ACCTIONS /////    

//     accounts that arent friends to send friend request 
//     public List<String> getSearchingAccounts(String nickName, String myNickName) {

//         List<Account> lAccountsFiltered = accountRepository.findByNickNameContainingIgnoreCase(nickName);
//         List<Account> lAccMy = accountRepository.findByNickName(myNickName);

//         if (!lAccMy.isEmpty()) {
//             Account myAccount = lAccMy.get(0);
//             lAccountsFiltered.removeAll(myAccount.getMyFriends());
//             lAccountsFiltered.removeAll(myAccount.getPendingFriends());
//             lAccountsFiltered.removeAll(myAccount.getRequestFriends());
//             lAccountsFiltered.remove(myAccount);
//         }

//         return lAccountsFiltered.stream().map(this::convertToString).collect(Collectors.toList());
//     }

//     set friends request to account
//     public void sendFriendRequest(Account myAcc, String nickNameToSend){

//         List<Account> lAccToSend = accountRepository.findByNickName(nickNameToSend);
        
//         lAccToSend.get(0).getPendingFriends().add(myAcc);
//         myAcc.getRequestFriends().add(lAccToSend.get(0));

//         lAccToSend.add(myAcc);

//         accountRepository.saveAll(lAccToSend);
//     }

//     set friends to login account and other account, remove from request and pending friends
//     public void aceptPendingFriend(Account myAcc, String nickNameToSend){

//         List<Account> lAccToSend = accountRepository.findByNickName(nickNameToSend);

//         lAccToSend.get(0).getRequestFriends().remove(myAcc);        //remove from other acc the request
//         lAccToSend.get(0).getMyFriends().add(myAcc);                //add to other account the login account as friend

//         myAcc.getPendingFriends().remove(lAccToSend.get(0));        //remove from login acc the pending friend
//         myAcc.getMyFriends().add(lAccToSend.get(0));                                  //add to login account the other account as friend

//         lAccToSend.add(myAcc);

//         accountRepository.saveAll(lAccToSend);
//     }

//     deny pending friend, remove from peding friend to login account and request friend to other account
//     public void denyPendingFriend(Account myAcc, String nickNameToSend){

//         List<Account> lAccToSend = accountRepository.findByNickName(nickNameToSend);

//         lAccToSend.get(0).getRequestFriends().remove(myAcc);        //remove from other acc the request

//         myAcc.getPendingFriends().remove(lAccToSend.get(0));        //remove from login acc the pending friend

//         lAccToSend.add(myAcc);

//         accountRepository.saveAll(lAccToSend);
//     }


//     delete my friend, remove from my friend to login account and from my friend to other account
//     public void deleteMyFriend(Account myAcc, String nickNameToSend){

//         List<Account> lAccToSend = accountRepository.findByNickName(nickNameToSend);

//         if (!lAccToSend.isEmpty()) {
            
//             lAccToSend.get(0).getMyFriends().remove(myAcc);        //remove from other acc my account

//             myAcc.getMyFriends().remove(lAccToSend.get(0));        //remove from login acc the friend

//             lAccToSend.add(myAcc);

//             accountRepository.saveAll(lAccToSend);
//         }
//     }

    

    
//     /********************/
//     /******* AJAX *******/
//     /********************/

//     public Page<String> getAllMyFriendsPage(String nickName, Pageable pageable) {
//         return accountRepository.findAllMyFriends(nickName, pageable).map(this::convertToString);
//     }

//     public Page<String> getAllPendingFriendsPage(String nickName, Pageable pageable) {
//         return accountRepository.findAllPendingFriends(nickName, pageable).map(this::convertToString);
//     }

//     public Page<String> getAllRequestFriendsPage(String nickName, Pageable pageable) {
//         return accountRepository.findAllRequestFriends(nickName, pageable).map(this::convertToString);
//     }

//     public Page<TeamDTO> getAllTeamsPage(String nickName, Pageable pageable) {
//         return accountRepository.findAllTeams(nickName, pageable).map(this::convertToTeamDTO);
//     }




//     /********************/
//     /******* HELP *******/
//     /********************/

//     private String convertToString(Account account) {
//         return account.getNickName();
//     }

//     private TeamDTO convertToTeamDTO(Team team) {
//         return new TeamDTO(team.getId(), team.getName());
//     }

//     private Boolean isAccountInList(List<Account> lAcc, String nickName){
//         for(Account acc: lAcc){
//             if(acc.getNickName().equals(nickName)){
//                 return true;
//             }
//         }
//         return false;
//     }
}