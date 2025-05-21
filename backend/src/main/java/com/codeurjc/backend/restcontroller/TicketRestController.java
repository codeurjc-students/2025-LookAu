package com.codeurjc.backend.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.DeleteExchange;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.TicketType;
import com.codeurjc.backend.model.Team;
import com.codeurjc.backend.model.DTO.AccountDTO;
import com.codeurjc.backend.model.DTO.RegisterAccountDTO;
import com.codeurjc.backend.model.DTO.TeamDTO;
import com.codeurjc.backend.model.DTO.TicketTeamDTO;
import com.codeurjc.backend.model.types.Bonoloto;
import com.codeurjc.backend.model.types.Eurodreams;
import com.codeurjc.backend.model.types.Euromillones;
import com.codeurjc.backend.model.types.Gordo;
import com.codeurjc.backend.model.types.Loteria;
import com.codeurjc.backend.model.types.Lototurf;
import com.codeurjc.backend.model.types.Primitiva;
import com.codeurjc.backend.model.types.Quiniela;
import com.codeurjc.backend.model.types.Quinigol;
import com.codeurjc.backend.model.types.Quintuple;
import com.codeurjc.backend.service.AccountService;
import com.codeurjc.backend.service.TeamService;
import com.codeurjc.backend.service.TicketService;
import com.codeurjc.backend.service.TicketTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;


@RestController
@RequestMapping("/api/tickets")
public class TicketRestController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private TeamService teamService;
	@Autowired
	private TicketService ticketService;
	@Autowired
	private TicketTypeService ticketTypeService;


	/*********************************/
	/******** TEAMS INTERFACE ********/
	/*********************************/

	@SuppressWarnings("null")
	@Operation(summary = "Get tickets")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/")
	public ResponseEntity<?> getTicketSave(HttpServletRequest request)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()){

			return new ResponseEntity<>(null, HttpStatus.OK);
			
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketId}")
	public ResponseEntity<?> getTicket(HttpServletRequest request, @PathVariable String ticketId) throws IOException, SQLException {

		Optional<Account> currentUser = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (currentUser.isPresent()) {
		
			Optional<Ticket> ticketOpp = ticketService.getById(Long.valueOf(ticketId));
			
			if (ticketOpp.isPresent()) {
				return ResponseEntity.ok(new TicketTeamDTO(ticketOpp.get()));
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}


	@Operation(summary = "Get the ticketType of a ticket")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Searching friends", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
		@ApiResponse(responseCode = "403", description = "Forbidden or don't have permissions", content = @Content)
	})
	@GetMapping("/{ticketId}/ticketType")
	public ResponseEntity<?> getTicketType(HttpServletRequest request, @PathVariable String ticketId) throws IOException, SQLException {

		Optional<Account> currentUser = accountService.getByEmail(request.getUserPrincipal().getName());

		if (currentUser.isPresent()) {
			Optional<Ticket> ticketOpp = ticketService.getById(Long.valueOf(ticketId));
			
			if (ticketOpp.isPresent()) {
				Ticket ticket = ticketOpp.get();

				if (ticket.getTicketType() != null) {
					TicketType tipo = ticket.getTicketType();
					
					if (tipo instanceof Bonoloto) {
						return ResponseEntity.ok((Bonoloto) tipo);
					} else if (tipo instanceof Eurodreams) {
						return ResponseEntity.ok((Eurodreams) tipo);
					} else if (tipo instanceof Euromillones) {
						return ResponseEntity.ok((Euromillones) tipo);
					} else if (tipo instanceof Gordo) {
						return ResponseEntity.ok((Gordo) tipo);
					} else if (tipo instanceof Loteria) {
						return ResponseEntity.ok((Loteria) tipo);
					} else if (tipo instanceof Lototurf) {
						return ResponseEntity.ok((Lototurf) tipo);
					} else if (tipo instanceof Primitiva) {
						return ResponseEntity.ok((Primitiva) tipo);
					} else if (tipo instanceof Quiniela) {
						return ResponseEntity.ok((Quiniela) tipo);
					} else if (tipo instanceof Quinigol) {
						return ResponseEntity.ok((Quinigol) tipo);
					} else if (tipo instanceof Quintuple) {
						return ResponseEntity.ok((Quintuple) tipo);
					} else {
						return ResponseEntity.notFound().build();
					}
				} else {
					return ResponseEntity.notFound().build();
				}
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}



	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{ticketId}")
	public ResponseEntity<?> editProfile(HttpServletRequest request, @PathVariable String ticketId, @RequestBody TicketTeamDTO ticketDTO)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<Ticket> ticketOpp = ticketService.getById(Long.valueOf(ticketId));
			
			if (ticketOpp.isPresent()) {
				Ticket ticket = ticketOpp.get();

				ticket.setDate(LocalDate.parse(ticketDTO.getDate()));
				ticket.setPaidByName(ticketDTO.getPaidByName());
				ticket.setPaidByPice(Double.valueOf(ticketDTO.getPaidByPice()));
				ticket.setClaimedBy(ticketDTO.getClaimedBy());

				ticketService.setTicket(ticket);

				return new ResponseEntity<>(null, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@SuppressWarnings("null")
	@Operation(summary = "Create a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PostMapping("/")
	public ResponseEntity<?> newTicket(HttpServletRequest request, @RequestBody TicketTeamDTO ticketDTO, @RequestParam String teamId)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent() && ticketDTO.getTicketTypeId()!=null && teamId!=null){

			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketDTO.getTicketTypeId()));
				
			if(Long.valueOf(teamId)>0){

				Optional<Team> teamOpp = teamService.getById(Long.valueOf(teamId));
			
				if (ticketTypeOpp.isPresent() && teamOpp.isPresent()){
			
					Ticket ticket = new Ticket();

					ticket.setDate(LocalDate.parse(ticketDTO.getDate()));
					ticket.setPaidByName(ticketDTO.getPaidByName());
					ticket.setPaidByPice(Double.valueOf(ticketDTO.getPaidByPice()));
					ticket.setClaimedBy(ticketDTO.getClaimedBy());
					ticket.setStatusName("Pending");
					ticket.setStatusPrice(0.0);
					
					ticket.setTicketType(ticketTypeOpp.get());
					ticket.setTeam(teamOpp.get());

					ticketService.setTicket(ticket);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

			}else{

				if (ticketTypeOpp.isPresent()){
			
					Ticket ticket = new Ticket();

					ticket.setDate(LocalDate.parse(ticketDTO.getDate()));
					ticket.setPaidByPice(Double.valueOf(ticketDTO.getPaidByPice()));
					ticket.setStatusName("Pending");
					ticket.setStatusPrice(0.0);
					
					ticket.setTicketType(ticketTypeOpp.get());
					ticket.setAccount(accountOpp.get());

					ticketService.setTicket(ticket);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			}
			
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@DeleteMapping("/{ticketId}")
	public ResponseEntity<?> deleteTicket(HttpServletRequest request, @PathVariable String ticketId)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<Ticket> ticketOpp = ticketService.getById(Long.valueOf(ticketId));
			
			if (ticketOpp.isPresent()) {
				Ticket ticket = ticketOpp.get();
				TicketType ticketType = ticket.getTicketType();

				ticket.setTicketType(null);
				ticketService.setTicket(ticket);

				ticketTypeService.deleteTicketType(ticketType);
				ticketService.deleteTicket(ticket);
				
				return new ResponseEntity<>(null, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
}