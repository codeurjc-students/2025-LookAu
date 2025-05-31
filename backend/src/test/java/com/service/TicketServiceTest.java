package com.service;

import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.repository.TicketRepository;
import com.codeurjc.backend.service.TicketService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Optional<Ticket> result = ticketService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testSetTicket() {
        Ticket ticket = new Ticket();
        ticketService.setTicket(ticket);

        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testSetTickets() {
        List<Ticket> tickets = List.of(new Ticket(), new Ticket());
        ticketService.setTickets(tickets);

        verify(ticketRepository, times(1)).saveAll(tickets);
    }

    @Test
    void testDeleteTicket() {
        Ticket ticket = new Ticket();
        ticketService.deleteTicket(ticket);

        verify(ticketRepository, times(1)).delete(ticket);
    }

    @Test
    void testGetOne() {
        Ticket ticket = new Ticket();
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));

        Ticket result = ticketService.getOne();

        assertNotNull(result);
        assertEquals(ticket, result);
    }
}
