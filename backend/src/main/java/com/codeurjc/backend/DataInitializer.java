package com.codeurjc.backend;

import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeurjc.backend.model.*;
import com.codeurjc.backend.model.types.*;
import com.codeurjc.backend.repository.*;
import com.codeurjc.backend.repository.types.*;
import com.codeurjc.backend.service.AccountService;

import jakarta.annotation.PostConstruct;

@Service
public class DataInitializer {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TicketTypeRepository ticketTypeRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BonolotoRepository bonolotoRepository;
    @Autowired
    private EurodreamsRepository eurodreamsRepository;
    @Autowired
    private EuromillonesRepository euromillonesRepository;
    @Autowired
    private GordoRepository gordoRepository;
    @Autowired
    private LoteriaRepository loteriaRepository;
    @Autowired
    private LototurfRepository lototurfRepository;
    @Autowired
    private PrimitivaRepository primitivaRepository;
    @Autowired
    private QuinielaRepository quinielaRepository;
    @Autowired
    private QuinigolRepository quinigolRepository;
    @Autowired
    private QuintupleRepository quintupleRepository;



    @PostConstruct
    public void init() throws Exception {


        /****************************/
        /****************************/ 
        /********** CREATE **********/
        /****************************/
        /****************************/
        
        /*********************/ 
        /*** TICKET TYPES ****/
        /*********************/

        Bonoloto b1 = new Bonoloto(new ArrayList<>(Arrays.asList(1, 14, 34, 22, 23, 33)));
        Bonoloto b2 = new Bonoloto(new ArrayList<>(Arrays.asList(5, 12, 25, 30, 35, 40)));
        Bonoloto b3 = new Bonoloto(new ArrayList<>(Arrays.asList(10, 15, 20, 25, 30, 35)));

        Eurodreams ed1 = new Eurodreams(new ArrayList<>(Arrays.asList(44, 7, 23, 14, 6, 19, 10)));
        Eurodreams ed2 = new Eurodreams(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7)));
        Eurodreams ed3 = new Eurodreams(new ArrayList<>(Arrays.asList(8, 9, 10, 11, 12, 13, 14)));

        Euromillones em1 = new Euromillones(new ArrayList<>(Arrays.asList(43, 7, 23, 4, 46, 19, 18)));
        Euromillones em2 = new Euromillones(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7)));
        Euromillones em3 = new Euromillones(new ArrayList<>(Arrays.asList(8, 9, 10, 11, 12, 13, 14)));

        Gordo g1 = new Gordo(new ArrayList<>(Arrays.asList(17,47, 31, 27, 3, 8)));
        Gordo g2 = new Gordo(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6)));
        Gordo g3 = new Gordo(new ArrayList<>(Arrays.asList(7, 8, 9, 10, 11, 12)));

        Loteria l1 = new Loteria(51224, 20, 1, 2);
        Loteria l2 = new Loteria(12345, 10, 3, 4);
        Loteria l3 = new Loteria(67890, 15, 5, 6);

        Lototurf lt1 = new Lototurf(new ArrayList<>(Arrays.asList(35, 27, 2, 49, 46, 19, 1)));
        Lototurf lt2 = new Lototurf(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7)));
        Lototurf lt3 = new Lototurf(new ArrayList<>(Arrays.asList(8, 9, 10, 11, 12, 13, 14)));

        Primitiva p1 = new Primitiva(new ArrayList<>(Arrays.asList(3, 27, 2, 4, 6, 19, 8)));
        Primitiva p2 = new Primitiva(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7)));
        Primitiva p3 = new Primitiva(new ArrayList<>(Arrays.asList(8, 9, 10, 11, 12, 13, 14)));

        Quintuple qt1 = new Quintuple(new ArrayList<>(Arrays.asList(1, 4, 3, 42, 23, 7)));
        Quintuple qt2 = new Quintuple(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6)));
        Quintuple qt3 = new Quintuple(new ArrayList<>(Arrays.asList(7, 8, 9, 10, 11, 12)));

        Quiniela q1 = new Quiniela(  
            new ArrayList<>(Arrays.asList("1", "X", "2", "1", "X", "2", "1", "X", "2", "1", "X", "2", "1", "X", "2")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"))
        );
        Quiniela q2 = new Quiniela(
            new ArrayList<>(Arrays.asList("X", "1", "2", "X", "1", "2", "X", "1", "2", "X", "1", "2", "X", "1", "2")), 
            new ArrayList<>(Arrays.asList("1", "X", "2", "1", "X", "2", "1", "X", "2", "1", "X", "2", "1", "X", "2")), 
            new ArrayList<>(Arrays.asList("2", "1", "X", "2", "1", "X", "2", "1", "X", "2", "1", "X", "2", "1", "X")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"))
        );
        Quiniela q3 = new Quiniela(
            new ArrayList<>(Arrays.asList("2", "1", "X", "2", "1", "X", "2", "1", "X", "2", "1", "X", "2", "1", "X")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"))
        );

        Quinigol qg1 = new Quinigol( 
            new ArrayList<>(Arrays.asList("1", "3", "2", "1", "3", "2", "1", "3", "2", "1", "3", "2", "1", "3", "2")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"))
        );
        Quinigol qg2 = new Quinigol(
            new ArrayList<>(Arrays.asList("2", "1", "3", "2", "1", "3", "2", "1", "3", "2", "1", "3", "2", "1", "3")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"))
        );
        Quinigol qg3 = new Quinigol(
            new ArrayList<>(Arrays.asList("3", "2", "1", "3", "2", "1", "3", "2", "1", "3", "2", "1", "3", "2", "1")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")), 
            new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"))
        );



        /******************/ 
        /**** ACCOUNTS ****/
        /******************/

        byte[] photo = this.getFile("C:\\Users\\amand\\Desktop\\2025-LookAu\\backend\\src\\main\\resources\\static\\images\\others\\flork_noprofile.jpg");

        Account account1 = new Account("LaTinyLoco", "Amanda", "Castro López", "amanda.cl@gmail.com", "password1", this.getFile("C:\\Users\\amand\\Desktop\\2025-LookAu\\backend\\src\\main\\resources\\static\\images\\others\\flork3_profile.png"));
        Account account2 = new Account("Pepiflor23", "Alberto", "Limón Carmona", "alberto.lc@gmail.com", "password2", this.getFile("C:\\Users\\amand\\Desktop\\2025-LookAu\\backend\\src\\main\\resources\\static\\images\\others\\flork2_profile.png"));
        Account account3 = new Account("Akalpaca", "Diego", "Fernandez del Álamo", "diego.fa@gmail.com", "password3", this.getFile("C:\\Users\\amand\\Desktop\\2025-LookAu\\backend\\src\\main\\resources\\static\\images\\others\\flork1_profile.jpeg"));
        Account account4 = new Account("Alvarochi", "Álvaro", "Fernandez del Álamo", "alvaro.fa@gmail.com", "password4", photo);
        Account account5 = new Account("DaniDu", "Daniel", "Esteban de la Cruz", "daniel.ec@gmail.com", "password5", photo);
        Account account6 = new Account("Miguelog", "Miguel Ángel", "Osuna Galindo", "miguel.og@gmail.com", "password6", photo);
        Account account7 = new Account("Aderudo", "Eduardo", "Diez Blanco", "eduardo.db@gmail.com", "password7", photo);



        /***************/ 
        /**** TEAMS ****/
        /***************/

        Team team1 = new Team("Decramados Team", this.getFile("C:\\Users\\amand\\Desktop\\2025-LookAu\\backend\\src\\main\\resources\\static\\images\\others\\flork_team.jpg"));
        Team team2 = new Team("Rarw", photo);
        Team team3 = new Team("All for one", photo);
        Team team4 = new Team("Motogoat", photo);
        Team team5 = new Team("Empty Sadness", photo);



        /*****************/
        /**** TICKETS ****/    
        /*****************/

        //aderudo - acc7
        Ticket ticket1 = new Ticket("Winning", null, 1.50, null, LocalDate.of(2025, 3, 27));
        ticket1.setAccount(account7);
        ticket1.setTicketType(b1);
        Ticket ticket2 = new Ticket("Not Winning", null, 1.50, null, LocalDate.of(2024, 9, 19));
        ticket2.setAccount(account7);
        ticket2.setTicketType(ed1);
        Ticket ticket3 = new Ticket("Pending", null, 1.50, null, LocalDate.of(2025, 1, 3));
        ticket3.setAccount(account7);
        ticket3.setTicketType(em1);
        Ticket ticket4 = new Ticket("Winning", null, 1.50, null, LocalDate.of(2025, 3, 27));
        ticket4.setAccount(account7);
        ticket4.setTicketType(g1);
        Ticket ticket5 = new Ticket("Not Winning", null, 1.50, null, LocalDate.of(2024, 9, 19));
        ticket5.setAccount(account7);
        ticket5.setTicketType(l1);
        Ticket ticket6 = new Ticket("Pending", null, 1.50, null, LocalDate.of(2025, 1, 3));
        ticket6.setAccount(account7);
        ticket6.setTicketType(lt1);

        //tiny - acc1
        Ticket ticket7 = new Ticket("Winning", null, 1.50, null, LocalDate.of(2025, 3, 27));
        ticket7.setAccount(account1);
        ticket7.setTicketType(p1);
        Ticket ticket8 = new Ticket("Not Winning", null, 1.50, null, LocalDate.of(2024, 9, 19));
        ticket8.setAccount(account1);
        ticket8.setTicketType(qt1);
        Ticket ticket9 = new Ticket("Pending", null, 1.50, null, LocalDate.of(2025, 1, 3));
        ticket9.setAccount(account1);
        ticket9.setTicketType(q1);
        Ticket ticket10 = new Ticket("Winning", null, 1.50, null, LocalDate.of(2025, 3, 27));
        ticket10.setAccount(account1);
        ticket10.setTicketType(qg1);

        //team1
        Ticket ticket11 = new Ticket("Winning", "LaTinyLoco", 1.50, "LaTinyLoco", LocalDate.of(2025, 3, 27));
        ticket11.setTeam(team1);
        ticket11.setTicketType(b2);
        Ticket ticket12 = new Ticket("Not Winning", "LaTinyLoco", 1.50, "LaTinyLoco", LocalDate.of(2024, 9, 19));
        ticket12.setTeam(team1);
        ticket12.setTicketType(ed2);
        Ticket ticket13 = new Ticket("Pending", "LaTinyLoco", 1.50, "LaTinyLoco", LocalDate.of(2025, 1, 3));
        ticket13.setTeam(team1);
        ticket13.setTicketType(em2);
        Ticket ticket14 = new Ticket("Winning", "LaTinyLoco", 1.50, "LaTinyLoco", LocalDate.of(2025, 3, 27));
        ticket14.setTeam(team1);
        ticket14.setTicketType(g2);
        Ticket ticket15 = new Ticket("Not Winning", "Akalpaca", 1.50, "Akalpaca", LocalDate.of(2024, 9, 19));
        ticket15.setTeam(team1);
        ticket15.setTicketType(l2);
        Ticket ticket16 = new Ticket("Pending", "Akalpaca", 1.50, "Akalpaca", LocalDate.of(2025, 1, 3));
        ticket16.setTeam(team1);
        ticket16.setTicketType(lt2);
        Ticket ticket17 = new Ticket("Winning", "Akalpaca", 1.50, "Akalpaca", LocalDate.of(2025, 3, 27));
        ticket17.setTeam(team1);
        ticket17.setTicketType(p2);
        Ticket ticket18 = new Ticket("Not Winning", "Akalpaca", 1.50, "Akalpaca", LocalDate.of(2024, 9, 19));
        ticket18.setTeam(team1);
        ticket18.setTicketType(qt2);
        Ticket ticket19 = new Ticket("Pending", "Pepiflor23", 1.50, "Pepiflor23", LocalDate.of(2025, 1, 3));
        ticket19.setTeam(team1);
        ticket19.setTicketType(q2);
        Ticket ticket20 = new Ticket("Winning", "Pepiflor23", 1.50, "Pepiflor23", LocalDate.of(2025, 3, 27));
        ticket20.setTeam(team1);
        ticket20.setTicketType(qg2);

        //team2
        Ticket ticket21 = new Ticket("Winning", "Alvarochi", 1.50, "Alvarochi", LocalDate.of(2025, 3, 27));
        ticket21.setTeam(team2);
        ticket21.setTicketType(b3);
        Ticket ticket22 = new Ticket("Not Winning", "Alvarochi", 1.50, "Alvarochi", LocalDate.of(2024, 9, 19));
        ticket22.setTeam(team2);
        ticket22.setTicketType(ed3);
        Ticket ticket23 = new Ticket("Pending", "DaniDu", 1.50, "DaniDu", LocalDate.of(2025, 1, 3));
        ticket23.setTeam(team2);
        ticket23.setTicketType(em3);
        Ticket ticket24 = new Ticket("Winning", "DaniDu", 1.50, "DaniDu", LocalDate.of(2025, 3, 27));
        ticket24.setTeam(team2);
        ticket24.setTicketType(g3);
        Ticket ticket25 = new Ticket("Not Winning", "Miguelog", 1.50, "Miguelog", LocalDate.of(2024, 9, 19));
        ticket25.setTeam(team2);
        ticket25.setTicketType(l3);
        

        //team3
        Ticket ticket26 = new Ticket("Pending", "LaTinyLoco", 1.50, "LaTinyLoco", LocalDate.of(2025, 1, 3));
        ticket26.setTeam(team3);
        ticket26.setTicketType(lt3);
        Ticket ticket27 = new Ticket("Winning", "Akalpaca", 1.50, "Akalpaca", LocalDate.of(2025, 3, 27));
        ticket27.setTeam(team3);
        ticket27.setTicketType(p3);
        Ticket ticket28 = new Ticket("Not Winning", "Alvarochi", 1.50, "Alvarochi", LocalDate.of(2024, 9, 19));
        ticket28.setTeam(team3);
        ticket28.setTicketType(qt3);
        Ticket ticket29 = new Ticket("Pending", "DaniDu", 1.50, "DaniDu", LocalDate.of(2025, 1, 3));
        ticket29.setTeam(team3);
        ticket29.setTicketType(q3);
        Ticket ticket30 = new Ticket("Winning", "DaniDu", 1.50, "DaniDu", LocalDate.of(2025, 3, 27));
        ticket30.setTeam(team3);
        ticket30.setTicketType(qg3);



        




        /**************************/
        /**************************/ 
        /********** LINK **********/
        /**************************/
        /**************************/

        //ACCOUNTS - FRIENDS(Accounts)
        account1.setMyFriends(new ArrayList<>(Arrays.asList(account2, account3, account4, account5, account7)));
        account2.setMyFriends(new ArrayList<>(Arrays.asList(account1, account3)));
        account3.setMyFriends(new ArrayList<>(Arrays.asList(account1, account2, account4, account5)));
        account4.setMyFriends(new ArrayList<>(Arrays.asList(account1, account3, account5, account6)));
        account5.setMyFriends(new ArrayList<>(Arrays.asList(account1, account3, account4, account6)));
        account6.setMyFriends(new ArrayList<>(Arrays.asList(account4, account5)));
        account7.setMyFriends(new ArrayList<>(Arrays.asList(account1)));

        //ACCOUNTS - PENDING FRIENDS(Accounts)
        account7.setPendingFriends(new ArrayList<>(Arrays.asList(account2, account3, account4, account5, account6)));

        //ACCOUNTS - REQUEST FRIENDS(Accounts)
        account2.setRequestFriends(new ArrayList<>(Arrays.asList(account7)));
        account3.setRequestFriends(new ArrayList<>(Arrays.asList(account7)));
        account4.setRequestFriends(new ArrayList<>(Arrays.asList(account7)));
        account5.setRequestFriends(new ArrayList<>(Arrays.asList(account7)));
        account6.setRequestFriends(new ArrayList<>(Arrays.asList(account7)));
        

        //ACCOUNTS - TICKETS
        account7.setTickets(new ArrayList<>(Arrays.asList(ticket1, ticket2, ticket3, ticket4, ticket5, ticket6)));
        account1.setTickets(new ArrayList<>(Arrays.asList(ticket7, ticket8, ticket9, ticket10)));

        //TEAMS - TICKECTS
        team1.setTickets(new ArrayList<>(Arrays.asList(ticket11, ticket12, ticket13, ticket14, ticket15, ticket16, ticket17, ticket18, ticket19, ticket20)));
        team2.setTickets(new ArrayList<>(Arrays.asList(ticket21, ticket22, ticket23, ticket24, ticket25)));
        team3.setTickets(new ArrayList<>(Arrays.asList(ticket26, ticket27, ticket28, ticket29, ticket30)));

        //TEAMS - ACCOUNTS
        team1.setAccounts(new ArrayList<>(Arrays.asList(account1, account2, account3)));
        team2.setAccounts(new ArrayList<>(Arrays.asList(account4, account5, account6)));
        team3.setAccounts(new ArrayList<>(Arrays.asList(account1, account3, account4, account5)));
        team4.setAccounts(new ArrayList<>(Arrays.asList(account1, account7)));       
        team5.setAccounts(new ArrayList<>(Arrays.asList()));       //empty


        




        /**************************/
        /**************************/ 
        /********** SAVE **********/
        /**************************/
        /**************************/

        ticketRepository.deleteAll();
        accountRepository.deleteAll();
        teamRepository.deleteAll();
        bonolotoRepository.deleteAll();
        eurodreamsRepository.deleteAll();
        euromillonesRepository.deleteAll();
        gordoRepository.deleteAll();
        loteriaRepository.deleteAll();
        lototurfRepository.deleteAll();
        primitivaRepository.deleteAll();
        quinielaRepository.deleteAll();
        quinigolRepository.deleteAll();
        quintupleRepository.deleteAll();

        bonolotoRepository.saveAll(new ArrayList<>(Arrays.asList(b1, b2, b3)));
        eurodreamsRepository.saveAll(new ArrayList<>(Arrays.asList(ed1, ed2, ed3)));
        euromillonesRepository.saveAll(new ArrayList<>(Arrays.asList(em1, em2, em3)));
        gordoRepository.saveAll(new ArrayList<>(Arrays.asList(g1, g2, g3)));
        loteriaRepository.saveAll(new ArrayList<>(Arrays.asList(l1, l2, l3)));
        lototurfRepository.saveAll(new ArrayList<>(Arrays.asList(lt1, lt2, lt3)));
        primitivaRepository.saveAll(new ArrayList<>(Arrays.asList(p1, p2, p3)));
        quintupleRepository.saveAll(new ArrayList<>(Arrays.asList(qt1, qt2, qt3)));
        quinielaRepository.saveAll(new ArrayList<>(Arrays.asList(q1, q2, q3)));
        quinigolRepository.saveAll(new ArrayList<>(Arrays.asList(qg1, qg2, qg3)));
          
        teamRepository.saveAll(new ArrayList<>(Arrays.asList(team1, team2, team3, team4)));

        ticketRepository.saveAll(new ArrayList<>(Arrays.asList(ticket1, ticket2, ticket3, ticket4, ticket5, ticket6, ticket7, ticket8, ticket9, ticket10, ticket11, ticket12, ticket13, ticket14, ticket15, ticket16, ticket17, ticket18, ticket19, ticket20, ticket21, ticket22, ticket23, ticket24, ticket25, ticket26, ticket27, ticket28, ticket29, ticket30)));
    }



    private byte[] getFile(String url) throws IOException {

        Path path = Paths.get(url);

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
