package com.scheduler;

import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.TicketType;
import com.codeurjc.backend.scheduler.LotteryScheduler;
import com.codeurjc.backend.service.LotteryService;
import com.codeurjc.backend.service.ScraperService;
import com.codeurjc.backend.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LotterySchedulerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private LotteryService lotteryService;

    @Mock
    private ScraperService scraperService;

    @InjectMocks
    private LotteryScheduler lotteryScheduler;

    private Ticket pendingTicket;
    private Ticket nonPendingTicket;

    @BeforeEach
    void setUp() {

        TicketType ticketType = new TicketType();
        ticketType.setStringTickectType("Bonoloto");

        pendingTicket = new Ticket();
        pendingTicket.setId(1L);
        pendingTicket.setDate(LocalDate.now().minusDays(1));
        pendingTicket.setStatusName("Pending");
        pendingTicket.setTicketType(ticketType);

        nonPendingTicket = new Ticket();
        nonPendingTicket.setId(2L);
        nonPendingTicket.setDate(LocalDate.now());
        nonPendingTicket.setStatusName("Winning");
        nonPendingTicket.setTicketType(ticketType);
    }

    @Test
    void testRunTickets() throws Exception {

        //without pending tickets
        when(ticketService.getAllPendingTicket())
        .thenReturn(Collections.emptyList());

        lotteryScheduler.run();

        verify(ticketService, times(1)).getAllPendingTicket();
        verifyNoMoreInteractions(scraperService, lotteryService);

        reset(ticketService, scraperService, lotteryService);

        //with pending ticket for all types
        List<Ticket> multipleTickets = createTicketsForMultipleTypes();
        when(ticketService.getAllPendingTicket())
        .thenReturn(multipleTickets);
        
        mockAllLotteryTypes();  //mock for all lottery types
        
        lotteryScheduler.run();
        
        verify(ticketService, times(1)).getAllPendingTicket();
        verify(scraperService, times(1)).getResults(eq("BONO"), anyString(), anyString());
        verify(scraperService, times(1)).getResults(eq("EMIL"), anyString(), anyString());
        verify(scraperService, times(1)).getResults(eq("LAPR"), anyString(), anyString());
        verify(lotteryService, times(1)).checkAndChangeBonolotoStatus(anyList(), anyMap());
        verify(lotteryService, times(1)).checkAndChangeEuromillonesStatus(anyList(), anyMap());
        verify(lotteryService, times(1)).checkAndChangePrimitivaStatus(anyList(), anyMap());
        verify(ticketService, times(1)).setTickets(anyList());

        reset(ticketService, scraperService, lotteryService);
    }

   @Test
    void testRunErrorConcurrent() throws Exception {
    
        when(ticketService.getAllPendingTicket()).thenReturn(Arrays.asList(pendingTicket));
        mockScraper();
    
        lotteryScheduler.run();
        
        verify(scraperService, times(1)).getResults(anyString(), anyString(), anyString());
        
        lotteryScheduler.run();
        
        verify(scraperService, times(2)).getResults(anyString(), anyString(), anyString());
        verify(ticketService, times(2)).getAllPendingTicket();
        
        reset(ticketService, scraperService, lotteryService);
    }

    // HELPERS //
    //create tickets with any types
    private List<Ticket> createTicketsForMultipleTypes() {
        List<Ticket> tickets = new ArrayList<>();
        String[] types = {"Bonoloto", "Euromillones", "La Primitiva"};
        
        for (int i = 0; i < types.length; i++) {
            TicketType ticketType = new TicketType();
            ticketType.setStringTickectType(types[i]);
            
            Ticket ticket = new Ticket();
            ticket.setId((long) i);
            ticket.setDate(LocalDate.now().minusDays(i));
            ticket.setStatusName("Pending");
            ticket.setTicketType(ticketType);
            
            tickets.add(ticket);
        }
        return tickets;
    }

    //create mock for lottery types
    /** @throws Exception  */
    private void mockAllLotteryTypes() throws Exception {
        List<Object> mockResults = new ArrayList<>();
        mockResults.add(new Object());
        
        List<Ticket> mockTickets = new ArrayList<>();
        mockTickets.add(new Ticket());
        
        doReturn(mockResults).when(scraperService).getResults(eq("BONO"), anyString(), anyString());
        doReturn(mockResults).when(scraperService).getResults(eq("EMIL"), anyString(), anyString());
        doReturn(mockResults).when(scraperService).getResults(eq("LAPR"), anyString(), anyString());
        
        doReturn(mockTickets).when(lotteryService).checkAndChangeBonolotoStatus(anyList(), anyMap());
        doReturn(mockTickets).when(lotteryService).checkAndChangeEuromillonesStatus(anyList(), anyMap());
        doReturn(mockTickets).when(lotteryService).checkAndChangePrimitivaStatus(anyList(), anyMap());
    }

    //mock for scraper
    private void mockScraper() throws Exception {
        List<?> mockResults = Arrays.asList(new Object());
        List<Ticket> mockUpdatedTickets = Arrays.asList(new Ticket());
        
        doReturn(mockResults)
            .when(scraperService)
            .getResults(anyString(), anyString(), anyString());

        doReturn(mockUpdatedTickets)
            .when(lotteryService)
            .checkAndChangeBonolotoStatus(anyList(), anyMap());
    }
}