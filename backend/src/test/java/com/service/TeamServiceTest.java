package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.Team;
import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.DTO.TeamDTO;
import com.codeurjc.backend.model.DTO.TicketTeamDTO;
import com.codeurjc.backend.repository.TeamRepository;
import com.codeurjc.backend.service.TeamService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Spy
    @InjectMocks
    private TeamService teamServiceSpy;

    private Team team;
    private Ticket t1, t2, t3;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setId(42L);

        t1 = new Ticket(); t1.setDate(LocalDate.of(2023, 1, 10));
        t2 = new Ticket(); t2.setDate(LocalDate.of(2023, 3, 15));
        t3 = new Ticket(); t3.setDate(LocalDate.of(2022, 12, 5));

        team.setTickets(new ArrayList<>(List.of(t1, t2, t3)));
    }

    @Test
    void testGetById() {

        //found
        when(teamRepository.findById(42L)).thenReturn(Optional.of(team));

        Optional<Team> result = teamServiceSpy.getById(42L);

        assertTrue(result.isPresent());
        assertEquals(team, result.get());

        //not found
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Team> resultNot = teamServiceSpy.getById(99L);

        assertFalse(resultNot.isPresent());
    }


    @Test
    void testSetAndDeleteTeam() {
        teamServiceSpy.setTeam(team);
        verify(teamRepository).save(team);

        teamServiceSpy.deleteTeam(team);
        verify(teamRepository).delete(team);
    }

    @Test
    void testGetAccountsTeam() {

        //get teams
        Account acc1 = new Account(); 
        acc1.setNickName("test1");

        Account acc2 = new Account(); 
        acc2.setNickName("test2");

        Account acc3 = new Account(); 
        acc3.setNickName("test3");

        Team team = new Team();
        team.setAccounts(new ArrayList<>(List.of(acc1, acc2, acc3)));

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        List<String> result = teamServiceSpy.getAccountsTeam(1L);

        assertEquals(List.of("test1", "test2", "test3"), result);

        //not found
        when(teamRepository.findById(5L)).thenReturn(Optional.empty());
        assertNull(teamServiceSpy.getAccountsTeam(5L));
    }

    @Test
    void testGetAccountsTeam_NotFound() {
        when(teamRepository.findById(5L)).thenReturn(Optional.empty());
        assertNull(teamServiceSpy.getAccountsTeam(5L));
    }

    @Test
    void testGetSearchingTeams() {
        Team t1 = new Team(); t1.setName("test1");
        Team t2 = new Team(); t2.setName("test2");
        Team t3 = new Team(); t3.setName("test3");

        List<Team> all = List.of(t1, t2, t3);
        List<TeamDTO> result = teamServiceSpy.getSearchingTeams("te", all);

        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getName().equals("test1")));
        assertTrue(result.stream().anyMatch(dto -> dto.getName().equals("test3")));
    }

    @Test
    void testGetAllTicketTeams() {        
        doReturn(new TicketTeamDTO(t1))
            .when(teamServiceSpy).convertToTicketTeamDTO(any(Ticket.class));

        List<TicketTeamDTO> result = teamServiceSpy.getAllTicketTeams(List.of(t1, t2, t3));
        assertEquals(3, result.size());
    }

    @Test
    void testGetTeamTicketsPaged() {
        when(teamRepository.findById(42L)).thenReturn(Optional.of(team));

        doAnswer(invocation -> new TicketTeamDTO(invocation.getArgument(0)))
            .when(teamServiceSpy).convertToTicketTeamDTO(any(Ticket.class));

        Page<TicketTeamDTO> page = teamServiceSpy.getTeamTicketsPaged("42", PageRequest.of(0, 2));

        assertEquals(2, page.getContent().size());
    }

    @Test
    void testGetTeamTicketsFilterPaged() {
        when(teamRepository.findById(42L)).thenReturn(Optional.of(team));

        doAnswer(invocation -> new TicketTeamDTO(invocation.getArgument(0)))
            .when(teamServiceSpy).convertToTicketTeamDTO(any(Ticket.class));

        Page<TicketTeamDTO> page = teamServiceSpy.getTeamTicketsFilterPaged("42", "2023", "", PageRequest.of(0, 2));

        assertEquals(2, page.getContent().size());
        page = teamServiceSpy.getTeamTicketsFilterPaged("42", "", "nope", PageRequest.of(0, 2));
        assertEquals(0, page.getContent().size());
    }

}
