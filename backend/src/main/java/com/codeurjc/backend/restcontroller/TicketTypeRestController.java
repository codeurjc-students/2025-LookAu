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
import com.codeurjc.backend.model.DTO.typeDTO.BonolotoDTO;
import com.codeurjc.backend.model.DTO.typeDTO.EurodreamsDTO;
import com.codeurjc.backend.model.DTO.typeDTO.EuromillonesDTO;
import com.codeurjc.backend.model.DTO.typeDTO.GordoDTO;
import com.codeurjc.backend.model.DTO.typeDTO.LoteriaDTO;
import com.codeurjc.backend.model.DTO.typeDTO.LototurfDTO;
import com.codeurjc.backend.model.DTO.typeDTO.PrimitivaDTO;
import com.codeurjc.backend.model.DTO.typeDTO.QuinielaDTO;
import com.codeurjc.backend.model.DTO.typeDTO.QuinigolDTO;
import com.codeurjc.backend.model.DTO.typeDTO.QuintupleDTO;
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
@RequestMapping("/api/ticketTypes")
public class TicketTypeRestController {

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

	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketTypeId}")
	public ResponseEntity<?> getTicket(HttpServletRequest request, @PathVariable String ticketTypeId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();
				return ResponseEntity.ok(ticketType);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}

	/** BONOLOTO **/
	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketTypeId}/bonoloto")
	public ResponseEntity<?> getTicketBonoloto(HttpServletRequest request, @PathVariable String ticketTypeId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();
				return ResponseEntity.ok(ticketType);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	
	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{ticketTypeId}/bonoloto")
	public ResponseEntity<?> editTicketBonoloto(HttpServletRequest request, @PathVariable String ticketTypeId, @RequestBody BonolotoDTO bonolotoDTO)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();

				if (ticketType instanceof Bonoloto) {
					Bonoloto bonoloto = (Bonoloto) ticketType;

					bonoloto.setNum1(bonolotoDTO.getNum1());
					bonoloto.setNum2(bonolotoDTO.getNum2());
					bonoloto.setNum3(bonolotoDTO.getNum3());
					bonoloto.setNum4(bonolotoDTO.getNum4());
					bonoloto.setNum5(bonolotoDTO.getNum5());
					bonoloto.setNum6(bonolotoDTO.getNum6());

					ticketTypeService.setTicketType(bonoloto);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}


	/** EURODREAMS **/
	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketTypeId}/eurodreams")
	public ResponseEntity<?> getTicketEurodreams(HttpServletRequest request, @PathVariable String ticketTypeId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();
				return ResponseEntity.ok(ticketType);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	
	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{ticketTypeId}/eurodreams")
	public ResponseEntity<?> editTicketEurodreams(HttpServletRequest request, @PathVariable String ticketTypeId, @RequestBody EurodreamsDTO eurodreamsDTO)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();

				if (ticketType instanceof Eurodreams) {
					Eurodreams eurodreams = (Eurodreams) ticketType;

					eurodreams.setNum1(eurodreamsDTO.getNum1());
					eurodreams.setNum2(eurodreamsDTO.getNum2());
					eurodreams.setNum3(eurodreamsDTO.getNum3());
					eurodreams.setNum4(eurodreamsDTO.getNum4());
					eurodreams.setNum5(eurodreamsDTO.getNum5());
					eurodreams.setNum6(eurodreamsDTO.getNum6());
					eurodreams.setDream(eurodreamsDTO.getDream());

					ticketTypeService.setTicketType(eurodreams);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}


	/** EUROMILLONES **/
	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketTypeId}/euromillones")
	public ResponseEntity<?> getTicketEuromillones(HttpServletRequest request, @PathVariable String ticketTypeId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();
				return ResponseEntity.ok(ticketType);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	
	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{ticketTypeId}/euromillones")
	public ResponseEntity<?> editProfile(HttpServletRequest request, @PathVariable String ticketTypeId, @RequestBody EuromillonesDTO euromillonesDTO)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();

				if (ticketType instanceof Euromillones) {
					Euromillones euromillones = (Euromillones) ticketType;

					euromillones.setNum1(euromillonesDTO.getNum1());
					euromillones.setNum2(euromillonesDTO.getNum2());
					euromillones.setNum3(euromillonesDTO.getNum3());
					euromillones.setNum4(euromillonesDTO.getNum4());
					euromillones.setNum5(euromillonesDTO.getNum5());
					euromillones.setStar1(euromillonesDTO.getStar1());
					euromillones.setStar2(euromillonesDTO.getStar2());

					ticketTypeService.setTicketType(euromillones);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}


	/** GORDO **/
	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketTypeId}/gordo")
	public ResponseEntity<?> getTicketGordo(HttpServletRequest request, @PathVariable String ticketTypeId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();
				return ResponseEntity.ok(ticketType);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	
	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{ticketTypeId}/gordo")
	public ResponseEntity<?> editTicketGordo(HttpServletRequest request, @PathVariable String ticketTypeId, @RequestBody GordoDTO gordoDTO)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();

				if (ticketType instanceof Gordo) {
					Gordo gordo = (Gordo) ticketType;

					gordo.setNum1(gordoDTO.getNum1());
					gordo.setNum2(gordoDTO.getNum2());
					gordo.setNum3(gordoDTO.getNum3());
					gordo.setNum4(gordoDTO.getNum4());
					gordo.setNum5(gordoDTO.getNum5());
					gordo.setKey(gordoDTO.getKey());

					ticketTypeService.setTicketType(gordo);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}


	/** LOTERIA **/
	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketTypeId}/loteria")
	public ResponseEntity<?> getTicketLoteria(HttpServletRequest request, @PathVariable String ticketTypeId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();
				return ResponseEntity.ok(ticketType);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	
	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{ticketTypeId}/loteria")
	public ResponseEntity<?> editTicketLoteria(HttpServletRequest request, @PathVariable String ticketTypeId, @RequestBody LoteriaDTO loteriaDTO)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();

				if (ticketType instanceof Loteria) {
					Loteria loteria = (Loteria) ticketType;

					loteria.setNumber(loteriaDTO.getNumber());
					loteria.setEuros(loteriaDTO.getEuros());
					loteria.setSeries(loteriaDTO.getSeries());
					loteria.setFraction(loteriaDTO.getFraction());

					ticketTypeService.setTicketType(loteria);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}


	/** LOTOTURF **/
	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketTypeId}/lototurf")
	public ResponseEntity<?> getTicketLototurf(HttpServletRequest request, @PathVariable String ticketTypeId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();
				return ResponseEntity.ok(ticketType);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	
	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{ticketTypeId}/lototurf")
	public ResponseEntity<?> editTicketLototurf(HttpServletRequest request, @PathVariable String ticketTypeId, @RequestBody LototurfDTO lototurfDTO)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();

				if (ticketType instanceof Lototurf) {
					Lototurf lototurf = (Lototurf) ticketType;

					lototurf.setNum1(lototurfDTO.getNum1());
					lototurf.setNum2(lototurfDTO.getNum2());
					lototurf.setNum3(lototurfDTO.getNum3());
					lototurf.setNum4(lototurfDTO.getNum4());
					lototurf.setNum5(lototurfDTO.getNum5());
					lototurf.setNum6(lototurfDTO.getNum6());
					lototurf.setHorse(lototurfDTO.getHorse());

					ticketTypeService.setTicketType(lototurf);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}


	/** PRIMITIVA **/
	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketTypeId}/primitiva")
	public ResponseEntity<?> getTicketPrimitiva(HttpServletRequest request, @PathVariable String ticketTypeId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();
				return ResponseEntity.ok(ticketType);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	
	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{ticketTypeId}/primitiva")
	public ResponseEntity<?> editTicketPrimitiva(HttpServletRequest request, @PathVariable String ticketTypeId, @RequestBody PrimitivaDTO primitivaDTO)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();

				if (ticketType instanceof Primitiva) {
					Primitiva primitiva = (Primitiva) ticketType;

					primitiva.setNum1(primitivaDTO.getNum1());
					primitiva.setNum2(primitivaDTO.getNum2());
					primitiva.setNum3(primitivaDTO.getNum3());
					primitiva.setNum4(primitivaDTO.getNum4());
					primitiva.setNum5(primitivaDTO.getNum5());
					primitiva.setNum6(primitivaDTO.getNum6());
					primitiva.setReimbursement(primitivaDTO.getReimbursement());

					ticketTypeService.setTicketType(primitiva);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}


	/** QUINIELA **/
	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketTypeId}/quiniela")
	public ResponseEntity<?> getTicketQuiniela(HttpServletRequest request, @PathVariable String ticketTypeId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();
				return ResponseEntity.ok(ticketType);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	
	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{ticketTypeId}/quiniela")
	public ResponseEntity<?> editTicketQuiniela(HttpServletRequest request, @PathVariable String ticketTypeId, @RequestBody QuinielaDTO quinielaDTO)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();

				if (ticketType instanceof Quiniela) {
					Quiniela quiniela = (Quiniela) ticketType;

					quiniela.setBet1(quinielaDTO.getBet1());
					quiniela.setBet2(quinielaDTO.getBet2());
					quiniela.setBet3(quinielaDTO.getBet3());
					quiniela.setBet4(quinielaDTO.getBet4());
					quiniela.setBet5(quinielaDTO.getBet5());
					quiniela.setBet6(quinielaDTO.getBet6());
					quiniela.setBet7(quinielaDTO.getBet7());
					quiniela.setBet8(quinielaDTO.getBet8());

					ticketTypeService.setTicketType(quiniela);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}


	/** QUINIGOL **/
	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketTypeId}/quinigol")
	public ResponseEntity<?> getTicketQuinigol(HttpServletRequest request, @PathVariable String ticketTypeId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();
				return ResponseEntity.ok(ticketType);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	
	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{ticketTypeId}/quinigol")
	public ResponseEntity<?> editProfile(HttpServletRequest request, @PathVariable String ticketTypeId, @RequestBody QuinigolDTO quinigolDTO)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();

				if (ticketType instanceof Quinigol) {
					Quinigol quinigol = (Quinigol) ticketType;

					quinigol.setBet1(quinigolDTO.getBet1());
					quinigol.setBet2(quinigolDTO.getBet2());
					quinigol.setBet3(quinigolDTO.getBet3());
					quinigol.setBet4(quinigolDTO.getBet4());
					quinigol.setBet5(quinigolDTO.getBet5());
					quinigol.setBet6(quinigolDTO.getBet6());

					ticketTypeService.setTicketType(quinigol);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}


	/** QUINTUPLE **/
	@Operation(summary = "Get a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{ticketTypeId}/quintuple")
	public ResponseEntity<?> getTicketQuintuple(HttpServletRequest request, @PathVariable String ticketTypeId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	 	if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();
				return ResponseEntity.ok(ticketType);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	
	@SuppressWarnings("null")
	@Operation(summary = "Update the ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{ticketTypeId}/quintuple")
	public ResponseEntity<?> editTicketQuintuple(HttpServletRequest request, @PathVariable String ticketTypeId, @RequestBody QuintupleDTO quintupleDTO)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Optional<TicketType> ticketTypeOpp = ticketTypeService.getById(Long.valueOf(ticketTypeId));
			
			if (ticketTypeOpp.isPresent()) {
				TicketType ticketType = ticketTypeOpp.get();

				if (ticketType instanceof Quintuple) {
					Quintuple quintuple = (Quintuple) ticketType;

					quintuple.setNum1(quintupleDTO.getNum1());
					quintuple.setNum2(quintupleDTO.getNum2());
					quintuple.setNum3(quintupleDTO.getNum3());
					quintuple.setNum4(quintupleDTO.getNum4());
					quintuple.setNum5(quintupleDTO.getNum5());
					quintuple.setNum6(quintupleDTO.getNum6());

					ticketTypeService.setTicketType(quintuple);

					return new ResponseEntity<>(null, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	
}