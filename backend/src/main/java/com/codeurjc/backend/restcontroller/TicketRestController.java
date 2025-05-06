package com.codeurjc.backend.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
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


	/*********************************/
	/******** TEAMS INTERFACE ********/
	/*********************************/
	
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




	// /**********************************/
	// /******** PROFILES DETAILS ********/
	// /**********************************/

	// @Operation(summary = "Get the image profile of a team")
	// @ApiResponses(value = {
	// 	@ApiResponse(responseCode = "200", description = "Found the image profile", content = {
	// 		@Content(mediaType = "image/jpg") }),
	// 	@ApiResponse(responseCode = "404", description = "Image profile not found", content = @Content),
	// 	@ApiResponse(responseCode = "403", description = "Forbidden or you don't have permissions", content = @Content) })
	// @GetMapping("/{teamId}/image")
	// public ResponseEntity<byte[]> downloadProfileImageByEmail(@PathVariable Long teamId) {

	// 	Optional<Team> teamOpp = teamService.getById(teamId);

	// 	if (teamOpp.isPresent()) {

	// 		Team team = teamOpp.get();

	// 		byte[] imageData = team.getProfilePicture();
	// 		if (imageData != null && imageData.length > 0) {
	// 			return ResponseEntity.ok()
	// 					.header(HttpHeaders.CONTENT_TYPE, "image/png")
	// 					.contentLength(imageData.length)
	// 					.body(imageData);
	// 		} else {
	// 			return ResponseEntity.notFound().build();
	// 		}
	// 	} else {
	// 		return ResponseEntity.notFound().build();
	// 	}
	// }

	// @Operation(summary = "Set the image profile of the team")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "200", description = "Found the image profile", content = {
	// 				@Content(mediaType = "image/jpg") }),
	// 		@ApiResponse(responseCode = "404", description = "image profile not found", content = @Content),
	// 		@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	// @PutMapping("/{teamId}/image")
	// public ResponseEntity<Object> updateFile(HttpServletRequest request, @RequestBody MultipartFile file, @PathVariable Long teamId) throws IOException {

	// 	byte[] foto;
	// 	Optional<Account> currentUser = accountService.getByEmail(request.getUserPrincipal().getName());

	// 	if (currentUser.isPresent()) {

	// 		Optional<Team> teamOpp = teamService.getById(teamId);

	// 		if (teamOpp.isPresent()) {
	// 			foto = file.getBytes();
	// 			Team team = teamOpp.get();

	// 			team.setProfilePicture(foto);
	// 			teamService.setTeam(team);
				
	// 			URI location = fromCurrentRequest().build().toUri();
				
	// 			return ResponseEntity.created(location).build();
	// 		}else{
	// 			return ResponseEntity.notFound().build();
	// 		}

	// 	} else {
	// 		return ResponseEntity.notFound().build();
	// 	}

	// }


	// @Operation(summary = "Create a team")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "201", description = "Team created", content = {
	// 				@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
	// 		@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	// @PostMapping("/")
	// public ResponseEntity<?> register(@RequestBody TeamDTO post, HttpServletRequest request) throws IOException, SQLException {

	// 	Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	// 	if (accountOpp.isPresent()) {
	// 		List<Account> lAccountToAddTeam = accountService.getAllByNicknames(Arrays.asList(post.getFriendsTeam()));
	// 		lAccountToAddTeam.add(accountOpp.get());

	// 		add the account to the team
	// 		Team team = new Team(post.getName());
	// 		team.setAccounts(lAccountToAddTeam);

	// 		add the teams to the accounts
	// 		for(Account acc: lAccountToAddTeam){
	// 			acc.getTeams().add(team);
	// 		}			

	// 		save both
	// 		teamService.setTeam(team);
	// 		accountService.setAccounts(lAccountToAddTeam);

	// 		return ResponseEntity.ok(new TeamDTO(team));
	// 	} else {
	// 		return ResponseEntity.notFound().build();
	// 	}
	// }

	
	



	// /******************************/
	// /******** AJAX FRIENDS ********/
	// /******************************/

	// https://localhost:8443/api/teams/id/tickets?page=0&size=10
	// @Operation(summary = "Get more account of the team")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "200", description = "Found more accounts", content = {
	// 				@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
	// 		@ApiResponse(responseCode = "404", description = "Team not found", content = @Content) })
	// @GetMapping("/{idTeam}/tickets")
	// public ResponseEntity<Page<TicketTeamDTO>> getMoreAcconutsTeams(HttpServletRequest request,
	// 		@RequestParam(value = "page", defaultValue = "0") int page,
	// 		@RequestParam(value = "size", defaultValue = "10") int size,
	// 		@RequestParam(value = "date", defaultValue = "") String date,
	// 		@RequestParam(value = "type", defaultValue = "") String type,
	// 		@PathVariable String idTeam) {
		
	// 	Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	// 	if (accountOpp.isPresent()) {

	// 		Page<TicketTeamDTO> lMyFriends;

	// 		if(type.equals("") && date.equals("")){
	// 			lMyFriends = teamService.getTeamTicketsPaged(idTeam, PageRequest.of(page, size));
	// 		}else{
	// 			lMyFriends = teamService.getTeamTicketsFilterPaged(idTeam, date, type, PageRequest.of(page, size));
	// 		}
			
	// 		if (lMyFriends.getSize() > 0) {
	// 			return new ResponseEntity<>(lMyFriends, HttpStatus.OK);
	// 		} else {
	// 			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 		}

			
	// 	} else{
	// 		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 	}
	// }





	// @GetMapping("/currentAccount")
	// public ResponseEntity<Optional<Account>> currentAccount(HttpServletRequest request) {
	// 	Principal principal = request.getUserPrincipal();
	// 	if (principal == null) {
	// 		return ResponseEntity.notFound().build();
	// 	}

	// 	try {
	// 		Optional<Account> currentAccount = accountService.getByEmail(request.getUserPrincipal().getName());
	// 		return ResponseEntity.ok(currentAccount);
	// 	} catch (NoSuchElementException e) {
	// 		return ResponseEntity.notFound().build();
	// 	}
	// }


	// @Operation(summary = "Get account information")
	// @ApiResponses(value = {
	// 	@ApiResponse(responseCode = "200", description = "Account information or not registered", 
	// 		content = @Content(mediaType = "application/json")),
	// 	@ApiResponse(responseCode = "403", description = "Forbidden or don't have permissions", 
	// 		content = @Content)
	// })
	// @GetMapping("/me")
	// public ResponseEntity<Map<String, Object>> profile(HttpServletRequest request) {
		
	// 	if (request.getUserPrincipal() == null) {
	// 		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	// 	}

	// 	Optional<Account> currentAccount = accountService.getByEmail(request.getUserPrincipal().getName());
		
	// 	if (currentAccount.isPresent()) {
	// 		return ResponseEntity.ok()
	// 			.body(Map.of(
	// 				"status", "registered",
	// 				"account", new AccountDTO(currentAccount.get())
	// 			));
	// 	} else {
	// 		return ResponseEntity.ok()
	// 			.body(Map.of(
	// 				"status", "not_registered",
	// 				"message", "User authenticated but not registered in the system"
	// 			));
	// 	}
	// }




	

	// @Operation(summary = "Register a account")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "201", description = "User created", content = {
	// 				@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
	// 		@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	// @PostMapping("/")
	// public ResponseEntity<?> register(@RequestBody RegisterAccountDTO post) throws IOException, SQLException {

	// 	if(!accountService.emailRepeat(post.getEmail())){
	// 		if(!accountService.nickNameRepeat(post.getNickName())){
	// 			Account acc = new Account(post.getNickName(), post.getFirstName(), post.getLastName(), post.getEmail(), post.getPassword());
	// 			accountService.setAccount(acc);

	// 			URI location = fromCurrentRequest().path("/{id}").buildAndExpand(acc.getId()).toUri();
	// 			return ResponseEntity.created(location).body(acc);
	// 		}else{
	// 			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Nickname already taken");
	// 		}
	// 	}else{
	// 		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Email already registered");
	// 	}
	// }







	// @SuppressWarnings("null")
	// @Operation(summary = "Update the account profile")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "200", description = "Profile updated", content = {
	// 				@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
	// 		@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
	// 		@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	// @PutMapping("/")
	// public ResponseEntity<?> editProfile(HttpServletRequest request, @RequestBody AccountDTO post)throws IOException, SQLException {

	// 	Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	// 	if (accountOpp.isPresent()) {
	// 		Account acc = accountOpp.get();

	// 		if (acc != null) {
	// 			acc.setFirstName(post.getFirstName());
	// 			acc.setLastName(post.getLastName());
	// 			acc.setPassword(post.getPassword());

	// 			accountService.setAccount(acc);

	// 			return new ResponseEntity<>(null, HttpStatus.OK);
	// 		} else {
	// 			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 		}

	// 	} else {
	// 		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 	}

	// }

	



	// /*********************************/
	// /******** ACTIONS FRIENDS ********/
	// /*********************************/

	// @Operation(summary = "Send friend request")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "201", description = "Searching friends", content = {
	// 				@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
	// 		@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	// @PutMapping("/{nickName}")
	// public ResponseEntity<?> sendFriendRequest(HttpServletRequest request, @PathVariable String nickName) throws IOException, SQLException {

	// 	Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	// 	if (accountOpp.isPresent()) {

	// 		Account account = accountOpp.get();

	// 		accountService.sendFriendRequest(account, nickName);

	// 		return ResponseEntity.ok(null);
			

	// 	} else {
	// 		return ResponseEntity.notFound().build();
	// 	}
	// }




	// @Operation(summary = "Delete a friend")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "201", description = "Accepted friend", content = {
	// 				@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
	// 		@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	// @DeleteMapping("/myFriends/{nickName}")
	// public ResponseEntity<?> deleteMyFriend(HttpServletRequest request, @PathVariable String nickName) throws IOException, SQLException {

	// 	Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	// 	if (accountOpp.isPresent()) {

	// 		Account account = accountOpp.get();

	// 		if(accountService.isInMyFriends(account, nickName)){
	// 			accountService.deleteMyFriend(account, nickName);
	// 			return ResponseEntity.ok(null);
	// 		}else{
	// 			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error");
	// 		}

	// 	} else {
	// 		return ResponseEntity.notFound().build();
	// 	}
	// }



	// @Operation(summary = "Acept a pending friend")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "201", description = "Accepted friend", content = {
	// 				@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
	// 		@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	// @PutMapping("/pendingFriends/{nickName}")
	// public ResponseEntity<?> aceptPendingFriend(HttpServletRequest request, @PathVariable String nickName) throws IOException, SQLException {

	// 	Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	// 	if (accountOpp.isPresent()) {

	// 		Account account = accountOpp.get();

	// 		if(accountService.isInPendingAndRequestFriends(account, nickName)){
	// 			accountService.aceptPendingFriend(account, nickName);
	// 			return ResponseEntity.ok(null);
	// 		}else{
	// 			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error");
	// 		}

	// 	} else {
	// 		return ResponseEntity.notFound().build();
	// 	}
	// }

	// @Operation(summary = "Deny a pending friend")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "201", description = "Denied friend", content = {
	// 				@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
	// 		@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	// @DeleteMapping("/pendingFriends/{nickName}")
	// public ResponseEntity<?> denyPendingFriend(HttpServletRequest request, @PathVariable String nickName) throws IOException, SQLException {
		
	// 	Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	// 	if (accountOpp.isPresent()) {

	// 		Account account = accountOpp.get();

	// 		if(accountService.isInPendingAndRequestFriends(account, nickName)){
	// 			accountService.denyPendingFriend(account, nickName);
	// 			return ResponseEntity.ok(null);
	// 		}else{
	// 			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error");
	// 		}

	// 	} else {
	// 		return ResponseEntity.notFound().build();
	// 	}

	// }






	// https://localhost:8443/api/users/?page=0&size=10
	// @Operation(summary = "Get more pending friends of the user")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "200", description = "Found more friends", content = {
	// 				@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
	// 		@ApiResponse(responseCode = "404", description = "Account not found", content = @Content) })
	// @GetMapping("/pendingFriends")
	// public ResponseEntity<Page<String>> getMorePendingFriends(HttpServletRequest request,
	// 		@RequestParam(value = "page", defaultValue = "0") int page,
	// 		@RequestParam(value = "size", defaultValue = "10") int size) {
		
	// 	Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	// 	if (accountOpp.isPresent()) {

	// 		Account acc = accountOpp.get();

	// 		Page<String> lMyFriends = accountService.getAllPendingFriendsPage(acc.getNickName(), PageRequest.of(page, size));
	// 		if (lMyFriends.getSize() > 0) {
	// 			return new ResponseEntity<>(lMyFriends, HttpStatus.OK);
	// 		} else {
	// 			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 		}

			
	// 	} else{
	// 		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 	}
	// }


	// https://localhost:8443/api/users/?page=0&size=10
	// @Operation(summary = "Get more request friends of the user")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "200", description = "Found more friends", content = {
	// 				@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
	// 		@ApiResponse(responseCode = "404", description = "Account not found", content = @Content) })
	// @GetMapping("/requestFriends")
	// public ResponseEntity<Page<String>> getMoreRequestFriends(HttpServletRequest request,
	// 		@RequestParam(value = "page", defaultValue = "0") int page,
	// 		@RequestParam(value = "size", defaultValue = "10") int size) {
		
	// 	Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

	// 	if (accountOpp.isPresent()) {

	// 		Account acc = accountOpp.get();

	// 		Page<String> lMyFriends = accountService.getAllRequestFriendsPage(acc.getNickName(), PageRequest.of(page, size));
	// 		if (lMyFriends.getSize() > 0) {
	// 			return new ResponseEntity<>(lMyFriends, HttpStatus.OK);
	// 		} else {
	// 			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 		}

			
	// 	} else{
	// 		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 	}
}