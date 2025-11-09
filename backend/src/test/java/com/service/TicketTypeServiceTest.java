package com.service;

import com.codeurjc.backend.model.TicketType;
import com.codeurjc.backend.repository.TicketTypeRepository;
import com.codeurjc.backend.service.TicketTypeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TicketTypeServiceTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Spy
    @InjectMocks
    private TicketTypeService ticketTypeServiceSpy;

    private TicketType ticketType;

    @BeforeEach
    void setUp() {
        ticketType = new TicketType();
        ticketType.setId(1L);
    }

    @Test
    void testGetById() {
        // found
        when(ticketTypeRepository.findById(1L)).thenReturn(Optional.of(ticketType));

        Optional<TicketType> result = ticketTypeServiceSpy.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(ticketType, result.get());

        // not found
        when(ticketTypeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<TicketType> resultNot = ticketTypeServiceSpy.getById(99L);

        assertFalse(resultNot.isPresent());
    }

    @Test
    void testSetTicketType() {
        ticketTypeServiceSpy.setTicketType(ticketType);

        verify(ticketTypeRepository).save(ticketType);
    }

    @Test
    void testSetTicketTypes() {
        List<TicketType> ticketTypes = List.of(new TicketType(), new TicketType());

        ticketTypeServiceSpy.setTicketTypes(ticketTypes);

        verify(ticketTypeRepository).saveAll(ticketTypes);
    }

    @Test
    void testDeleteTicketType() {
        ticketTypeServiceSpy.deleteTicketType(ticketType);

        verify(ticketTypeRepository).delete(ticketType);
    }

    @Test
    void testGetOne() {
        List<TicketType> ticketTypes = List.of(ticketType);
        when(ticketTypeRepository.findAll()).thenReturn(ticketTypes);

        TicketType result = ticketTypeServiceSpy.getOne();

        assertEquals(ticketType, result);
    }

    @Test
    void testGetOneEmptyList() {
        when(ticketTypeRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(IndexOutOfBoundsException.class, () -> ticketTypeServiceSpy.getOne());
    }
}
