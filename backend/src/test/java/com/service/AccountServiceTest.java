package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.Team;
import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.DTO.TeamDTO;
import com.codeurjc.backend.model.DTO.TicketTeamDTO;
import com.codeurjc.backend.repository.AccountRepository;
import com.codeurjc.backend.service.AccountService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Spy
    @InjectMocks
    private AccountService accountServiceSpy;


    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void testGetByEmail() {
        Account acc = new Account();
        acc.setEmail("test@gmail.com");

        when(accountRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(acc));

        Optional<Account> result = accountService.getByEmail("test@gmail.com");

        assertTrue(result.isPresent());
        assertEquals("test@gmail.com", result.get().getEmail());
    }


    @Test
    void testGetByNickName() {
        Account acc = new Account();
        acc.setNickName("test");

        when(accountRepository.findByNickName("test")).thenReturn(List.of(acc));

        Account result = accountService.getByNickName("test");

        assertEquals("test", result.getNickName());
    }

    @Test
    void testSetAccount() throws IOException {
        Account acc = new Account();
        acc.setNickName("test");

        accountService.setAccount(acc);

        verify(accountRepository, times(1)).save(acc);
    }

    @Test
    void testSetAccounts() {
        List<Account> accounts = List.of(new Account(), new Account());

        accountService.setAccounts(accounts);

        verify(accountRepository, times(1)).saveAll(accounts);
    }


    @Test
    void testEmailRepeat() {
        Account acc = new Account();
        acc.setEmail("test@example.com");
        
        //is repeat
        when(accountRepository.findAll()).thenReturn(List.of(acc));

        boolean result = accountService.emailRepeat("test@example.com");

        assertTrue(result);

        //is not repeat
        when(accountRepository.findAll()).thenReturn(List.of());

        result = accountService.emailRepeat("test@example.com");

        assertFalse(result);
    }



    @Test
    void testNickNameRepeat() {
        Account acc = new Account();
        acc.setNickName("nick");

        //is repeat
        when(accountRepository.findAll()).thenReturn(List.of(acc));

        boolean result = accountService.nickNameRepeat("nick");

        assertTrue(result);

        //is not repeat
        when(accountRepository.findAll()).thenReturn(List.of());

        result = accountService.nickNameRepeat("nick");

        assertFalse(result);
    }


    @Test
    void testGetAllByNicknames() {
        List<Account> accounts = List.of(new Account(), new Account());

        when(accountRepository.findAllByNickname(List.of("nick1", "nick2"))).thenReturn(accounts);

        List<Account> result = accountService.getAllByNicknames(List.of("nick1", "nick2"));

        assertEquals(2, result.size());
    }


    /*********************************/
    /******* FRIENDS (Profile) *******/
    /*********************************/

    @Test
    void testIsInPendingAndRequestFriend() {

        //both lists contain eachOther
        Account currentAccount = new Account();
        currentAccount.setNickName("account1");

        Account requestAccount = new Account();
        requestAccount.setNickName("account2");

        currentAccount.setPendingFriends(List.of(requestAccount));
        requestAccount.setRequestFriends(List.of(currentAccount));

        when(accountRepository.findByNickName("account2")).thenReturn(List.of(requestAccount));

        boolean result = accountService.isInPendingAndRequestFriends(currentAccount, "account2");

        assertTrue(result);
    }


    @Test
    void testIsInMyFriends() {
        Account currentAccount = new Account();
        currentAccount.setNickName("account1");

        Account friendAccount = new Account();
        friendAccount.setNickName("account2");

        currentAccount.setMyFriends(List.of(friendAccount));
        friendAccount.setMyFriends(List.of(currentAccount));

        when(accountRepository.findByNickName("account2")).thenReturn(List.of(friendAccount));

        boolean result = accountService.isInMyFriends(currentAccount, "account2");

        assertTrue(result);
    }

    @Test
    void testGetSearchingAccounts() {
        Account myAccount = new Account();
        myAccount.setNickName("myAccount");
        Account friend = new Account();
        friend.setNickName("friend");
        Account pending = new Account();
        pending.setNickName("pending");
        Account request = new Account();
        request.setNickName("request");
        Account other = new Account();
        other.setNickName("other");

        myAccount.setMyFriends(new ArrayList<>(List.of(friend)));
        myAccount.setPendingFriends(new ArrayList<>(List.of(pending)));
        myAccount.setRequestFriends(new ArrayList<>(List.of(request)));

        List<Account> foundAccounts = new ArrayList<>(List.of(friend, pending, request, other, myAccount));

        when(accountRepository.findByNickNameContainingIgnoreCase("my")).thenReturn(foundAccounts);
        when(accountRepository.findByNickName("myNick")).thenReturn(List.of(myAccount));

        List<String> result = accountService.getSearchingAccounts("my", "myNick");

        assertEquals(1, result.size());
        assertTrue(result.contains("other"));  
    }

    @Test
    void testSendFriendRequest() {
        Account myAcc = new Account();
        myAcc.setNickName("myNick");
        myAcc.setRequestFriends(new ArrayList<>());

        Account reqAcc = new Account();
        reqAcc.setNickName("requestFriend");

        reqAcc.setPendingFriends(new ArrayList<>());

        when(accountRepository.findByNickName("requestFriend")).thenReturn(new ArrayList<>(List.of(reqAcc)));
        when(accountRepository.saveAll(any())).thenReturn(null);

        accountService.sendFriendRequest(myAcc, "requestFriend");

        assertTrue(reqAcc.getPendingFriends().contains(myAcc));
        assertTrue(myAcc.getRequestFriends().contains(reqAcc));

        verify(accountRepository, times(1)).saveAll(any());
    }


    @Test
    void testAceptPendingFriend() {
        Account myAcc = new Account();
        myAcc.setNickName("myNick");
        myAcc.setPendingFriends(new ArrayList<>());
        myAcc.setMyFriends(new ArrayList<>());

        Account pendingAcc = new Account();
        pendingAcc.setNickName("pendingAcc");
        pendingAcc.setRequestFriends(new ArrayList<>());
        pendingAcc.setMyFriends(new ArrayList<>());

        pendingAcc.getRequestFriends().add(myAcc);
        myAcc.getPendingFriends().add(pendingAcc);

        when(accountRepository.findByNickName("pendingAcc")).thenReturn(new ArrayList<>(List.of(pendingAcc)));

        when(accountRepository.saveAll(any())).thenReturn(null);

        accountService.aceptPendingFriend(myAcc, "pendingAcc");

        assertFalse(pendingAcc.getRequestFriends().contains(myAcc));
        assertTrue(pendingAcc.getMyFriends().contains(myAcc));

        assertFalse(myAcc.getPendingFriends().contains(pendingAcc));
        assertTrue(myAcc.getMyFriends().contains(pendingAcc));

        verify(accountRepository, times(1)).saveAll(any());
    }

    @Test
    void testDenyPendingFriend() {
        Account myAcc = new Account();
        myAcc.setNickName("myNick");
        myAcc.setPendingFriends(new ArrayList<>());

        Account reqAcc = new Account();
        reqAcc.setNickName("reqAcc");
        reqAcc.setRequestFriends(new ArrayList<>());

        reqAcc.getRequestFriends().add(myAcc);
        myAcc.getPendingFriends().add(reqAcc);

        when(accountRepository.findByNickName("reqAcc")).thenReturn(new ArrayList<>(List.of(reqAcc)));
        when(accountRepository.saveAll(any())).thenReturn(null);

        accountService.denyPendingFriend(myAcc, "reqAcc");

        assertFalse(reqAcc.getRequestFriends().contains(myAcc));
        assertFalse(myAcc.getPendingFriends().contains(reqAcc));

        verify(accountRepository, times(1)).saveAll(any());
    }

    @Test
    void testDeleteMyFriend() {
        Account myAcc = new Account();
        myAcc.setNickName("myNick");
        myAcc.setMyFriends(new ArrayList<>());

        Account delAcc = new Account();
        delAcc.setNickName("targetNick");
        delAcc.setMyFriends(new ArrayList<>());

        delAcc.getMyFriends().add(delAcc);
        delAcc.getMyFriends().add(myAcc);

        when(accountRepository.findByNickName("targetNick")).thenReturn(new ArrayList<>(List.of(delAcc)));

        when(accountRepository.saveAll(any())).thenReturn(null);

        accountService.deleteMyFriend(myAcc, "targetNick");

        assertFalse(myAcc.getMyFriends().contains(delAcc));
        assertFalse(delAcc.getMyFriends().contains(myAcc));

        verify(accountRepository, times(1)).saveAll(any());
    }


    /***********************/
    /******* TICKETS *******/
    /***********************/

    @Test
    void testGetAccountTicketsPaged() {
        Ticket ticket1 = new Ticket();
        ticket1.setDate(LocalDate.of(2023, 1, 10));

        Ticket ticket2 = new Ticket();
        ticket2.setDate(LocalDate.of(2023, 3, 15));

        Ticket ticket3 = new Ticket();
        ticket3.setDate(LocalDate.of(2022, 12, 5));

        List<Ticket> tickets = List.of(ticket1, ticket2, ticket3);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        doAnswer(invocation -> {
            Ticket t = invocation.getArgument(0);
            TicketTeamDTO dto = new TicketTeamDTO();
            dto.setDate(t.getDate().format(formatter));
            return dto;
        }).when(accountServiceSpy).convertToTicketTeamDTO(any(Ticket.class));

        Page<TicketTeamDTO> page = accountServiceSpy.getAccountTicketsPaged(tickets, PageRequest.of(0, 2));

        assertEquals(2, page.getContent().size());
        assertEquals("20230315", page.getContent().get(0).getDate());
        assertEquals("20230110", page.getContent().get(1).getDate());
        assertEquals(3, page.getTotalElements());
    }


    /********************/
    /******* AJAX *******/
    /********************/

    @Test
    void testGetAllMyFriendsPage() {
        Account acc1 = new Account();
        acc1.setNickName("alice");

        Account acc2 = new Account();
        acc2.setNickName("bob");

        List<Account> accounts = List.of(acc1, acc2);
        Page<Account> accountPage = new PageImpl<>(accounts, PageRequest.of(0, 2), accounts.size());

        when(accountRepository.findAllMyFriends("myNick", PageRequest.of(0, 2))).thenReturn(accountPage);

        doReturn("alice").when(accountServiceSpy).convertToString(acc1);
        doReturn("bob").when(accountServiceSpy).convertToString(acc2);

        Page<String> result = accountServiceSpy.getAllMyFriendsPage("myNick", PageRequest.of(0, 2));

        assertEquals(2, result.getContent().size());
        assertEquals("alice", result.getContent().get(0));
        assertEquals("bob", result.getContent().get(1));
    }



    @Test
    void testGetAllPendingFriendsPage() {
        Account acc = new Account();
        acc.setNickName("charlie");

        Page<Account> accountPage = new PageImpl<>(List.of(acc), PageRequest.of(0, 1), 1);
        when(accountRepository.findAllPendingFriends("myNick", PageRequest.of(0, 1)))
            .thenReturn(accountPage);

        Page<String> result = accountService.getAllPendingFriendsPage("myNick", PageRequest.of(0, 1));

        assertEquals(1, result.getContent().size());
        assertEquals("charlie", result.getContent().get(0));
    }



    @Test
    void testGetAllRequestFriendsPage() {
        Account acc = new Account(); acc.setNickName("david");
        Page<Account> pageAcc = new PageImpl<>(List.of(acc), PageRequest.of(0, 1), 1);
        when(accountRepository.findAllRequestFriends("user", PageRequest.of(0, 1))).thenReturn(pageAcc);

        Page<String> result = accountService.getAllRequestFriendsPage("user", PageRequest.of(0, 1));

        assertEquals(List.of("david"), result.getContent());
    }

    @Test
    void testGetAllTeamsPage() {
        Team t1 = new Team(); t1.setId(10L); t1.setName("TeamA");
        Team t2 = new Team(); t2.setId(20L); t2.setName("TeamB");
        Page<Team> pageTeams = new PageImpl<>(List.of(t1, t2), PageRequest.of(0, 1), 2);

        when(accountRepository.findAllTeams("user", PageRequest.of(0, 1))).thenReturn(pageTeams);

        Page<TeamDTO> result = accountService.getAllTeamsPage("user", PageRequest.of(0, 1));

        assertEquals(2, result.getContent().size());
        assertEquals(10L, result.getContent().get(0).getId());
        assertEquals("TeamA", result.getContent().get(0).getName());
        assertEquals(20L, result.getContent().get(1).getId());
        assertEquals("TeamB", result.getContent().get(1).getName());
    }
}
