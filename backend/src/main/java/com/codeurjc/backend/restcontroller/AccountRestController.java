package com.codeurjc.backend.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.DTO.AccountDTO;
import com.codeurjc.backend.model.DTO.RegisterAccountDTO;
import com.codeurjc.backend.model.DTO.TeamDTO;
import com.codeurjc.backend.model.DTO.TicketTeamDTO;
import com.codeurjc.backend.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;


@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

	@Autowired
	private AccountService accountService;



	@Operation(summary = "Get account information")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Account information or not registered", 
			content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "Forbidden or don't have permissions", 
			content = @Content)
	})
	@GetMapping("/me")
	public ResponseEntity<Map<String, Object>> profile(HttpServletRequest request) {
		
		if (request.getUserPrincipal() == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		Optional<Account> currentAccount = accountService.getByEmail(request.getUserPrincipal().getName());
		
		if (currentAccount.isPresent()) {
			return ResponseEntity.ok()
				.body(Map.of(
					"status", "registered",
					"account", new AccountDTO(currentAccount.get())
				));
		} else {
			return ResponseEntity.ok()
				.body(Map.of(
					"status", "not_registered",
					"message", "User authenticated but not registered in the system"
				));
		}
	}




	

	@Operation(summary = "Register a account")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PostMapping("/")
	public ResponseEntity<?> register(@RequestBody RegisterAccountDTO post) throws IOException, SQLException {

		if(!accountService.emailRepeat(post.getEmail())){
			if(!accountService.nickNameRepeat(post.getNickName())){
				Account acc = new Account(post.getNickName(), post.getFirstName(), post.getLastName(), post.getEmail(), post.getPassword());
				accountService.setAccount(acc);

				URI location = fromCurrentRequest().path("/{id}").buildAndExpand(acc.getId()).toUri();
				return ResponseEntity.created(location).body(acc);
			}else{
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Nickname already taken");
			}
		}else{
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Email already registered");
		}
	}


	@Operation(summary = "Get the image profile of a user by email")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Found the image profile", content = {
			@Content(mediaType = "image/jpg") }),
		@ApiResponse(responseCode = "404", description = "Image profile not found", content = @Content),
		@ApiResponse(responseCode = "403", description = "Forbidden or you don't have permissions", content = @Content) })
	@GetMapping("/image")
	public ResponseEntity<byte[]> downloadProfileImageByEmail(@RequestParam String nickName) {

		Account acc = accountService.getByNickName(nickName);

		if (acc!=null) {
			byte[] imageData = acc.getProfilePicture();
			if (imageData != null && imageData.length > 0) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_TYPE, "image/png")
						.contentLength(imageData.length)
						.body(imageData);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}


	@Operation(summary = "Get the image profile of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the image profile", content = {
					@Content(mediaType = "image/jpg") }),
			@ApiResponse(responseCode = "404", description = "image profile not found", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/image/current")
	public ResponseEntity<byte[]> getUserImageProfile(HttpServletRequest request) {

		Optional<Account> currentUser = accountService.getByEmail(request.getUserPrincipal().getName());

		if (currentUser.isPresent()) {
			byte[] imageData = currentUser.get().getProfilePicture();
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_TYPE, "image/png")
					.contentLength(imageData.length)
					.body(imageData);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	
	@Operation(summary = "Set the image profile of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the image profile", content = {
					@Content(mediaType = "image/jpg") }),
			@ApiResponse(responseCode = "404", description = "image profile not found", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/image")
	public ResponseEntity<Object> updateFile(HttpServletRequest request, @RequestBody MultipartFile file) throws IOException {

		byte[] foto;
		Optional<Account> currentUser = accountService.getByEmail(request.getUserPrincipal().getName());

		if (currentUser.isPresent()) {
			foto = file.getBytes();
			Account acc = currentUser.get();

			acc.setProfilePicture(foto);
			accountService.setAccount(acc);
			
			URI location = fromCurrentRequest().build().toUri();
			
			return ResponseEntity.created(location).build();
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@SuppressWarnings("null")
	@Operation(summary = "Update the account profile")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/")
	public ResponseEntity<?> editProfile(HttpServletRequest request, @RequestBody AccountDTO post)throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			Account acc = accountOpp.get();

			if (acc != null) {
				acc.setFirstName(post.getFirstName());
				acc.setLastName(post.getLastName());
				acc.setPassword(post.getPassword());

				accountService.setAccount(acc);

				return new ResponseEntity<>(null, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	



	/*********************************/
	/******** ACTIONS FRIENDS ********/
	/*********************************/

	@Operation(summary = "Send friend request")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{nickName}")
	public ResponseEntity<?> sendFriendRequest(HttpServletRequest request, @PathVariable String nickName) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Account account = accountOpp.get();

			accountService.sendFriendRequest(account, nickName);

			return ResponseEntity.ok(null);
			

		} else {
			return ResponseEntity.notFound().build();
		}
	}


	@Operation(summary = "Search a friend")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{searchTem}")
	public ResponseEntity<?> searchFriend(HttpServletRequest request, @PathVariable String searchTem) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Account account = accountOpp.get();

			return ResponseEntity.ok(accountService.getSearchingAccounts(searchTem, account.getNickName()));
			

		} else {
			return ResponseEntity.notFound().build();
		}
	}


	@Operation(summary = "Search a friend")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/myFriends/{searchTem}")
	public ResponseEntity<?> searchMyFriend(HttpServletRequest request, @PathVariable String searchTem) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Account account = accountOpp.get();

			return ResponseEntity.ok(accountService.getSearchingMyFriends(searchTem, account.getMyFriends()));
			

		} else {
			return ResponseEntity.notFound().build();
		}
	}


	@Operation(summary = "Delete a friend")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Accepted friend", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@DeleteMapping("/myFriends/{nickName}")
	public ResponseEntity<?> deleteMyFriend(HttpServletRequest request, @PathVariable String nickName) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Account account = accountOpp.get();

			if(accountService.isInMyFriends(account, nickName)){
				accountService.deleteMyFriend(account, nickName);
				return ResponseEntity.ok(null);
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error");
			}

		} else {
			return ResponseEntity.notFound().build();
		}
	}



	@Operation(summary = "Acept a pending friend")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Accepted friend", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/pendingFriends/{nickName}")
	public ResponseEntity<?> aceptPendingFriend(HttpServletRequest request, @PathVariable String nickName) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Account account = accountOpp.get();

			if(accountService.isInPendingAndRequestFriends(account, nickName)){
				accountService.aceptPendingFriend(account, nickName);
				return ResponseEntity.ok(null);
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error");
			}

		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Deny a pending friend")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Denied friend", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@DeleteMapping("/pendingFriends/{nickName}")
	public ResponseEntity<?> denyPendingFriend(HttpServletRequest request, @PathVariable String nickName) throws IOException, SQLException {
		
		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Account account = accountOpp.get();

			if(accountService.isInPendingAndRequestFriends(account, nickName)){
				accountService.denyPendingFriend(account, nickName);
				return ResponseEntity.ok(null);
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error");
			}

		} else {
			return ResponseEntity.notFound().build();
		}

	}


	/*************************/
	/******** TICKETS ********/
	/*************************/


	@Operation(summary = "Get all tickets account")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found more accounts", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "404", description = "Team not found", content = @Content) })
	@GetMapping("/ticketss")
	public ResponseEntity<?> getAllTicketsAccount(HttpServletRequest request) {
		
		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			return new ResponseEntity<>(accountService.getAllTicketAccount(accountOpp.get().getTickets()), HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// https://localhost:8443/api/teams/id/tickets?page=0&size=10
	@Operation(summary = "Get more tickets of the team")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found more accounts", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "404", description = "Team not found", content = @Content) })
	@GetMapping("/tickets")
	public ResponseEntity<Page<TicketTeamDTO>> getMoreAcconutsTeams(HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "date", defaultValue = "") String date,
			@RequestParam(value = "type", defaultValue = "") String type) {
		
		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Page<TicketTeamDTO> lMyFriends;
			List<Ticket> lTickets = accountOpp.get().getTickets();

			if(type.equals("") && date.equals("")){	//if the filters are on
				lMyFriends = accountService.getAccountTicketsPaged(lTickets, PageRequest.of(page, size));
			}else{
				lMyFriends = accountService.getAccountTicketsFilterPaged(lTickets, date, type, PageRequest.of(page, size));
			}
			
			if (lMyFriends.getSize() > 0) {
				return new ResponseEntity<>(lMyFriends, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}




	/******************************/
	/******** AJAX FRIENDS ********/
	/******************************/

	// https://localhost:8443/api/account/myFriends/?page=0&size=10
	@Operation(summary = "Get more my friends of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found more friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content) })
	@GetMapping("/myFriends")
	public ResponseEntity<Page<String>> getMoreMyFriends(HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		
		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Account acc = accountOpp.get();

			Page<String> lMyFriends = accountService.getAllMyFriendsPage(acc.getNickName(), PageRequest.of(page, size));
			if (lMyFriends.getSize() > 0) {
				return new ResponseEntity<>(lMyFriends, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// https://localhost:8443/api/account/pendingFriends/?page=0&size=10
	@Operation(summary = "Get more pending friends of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found more friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content) })
	@GetMapping("/pendingFriends")
	public ResponseEntity<Page<String>> getMorePendingFriends(HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		
		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Account acc = accountOpp.get();

			Page<String> lMyFriends = accountService.getAllPendingFriendsPage(acc.getNickName(), PageRequest.of(page, size));
			if (lMyFriends.getSize() > 0) {
				return new ResponseEntity<>(lMyFriends, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	// https://localhost:8443/api/account/requestFriends/?page=0&size=10
	@Operation(summary = "Get more request friends of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found more friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content) })
	@GetMapping("/requestFriends")
	public ResponseEntity<Page<String>> getMoreRequestFriends(HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		
		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Account acc = accountOpp.get();

			Page<String> lMyFriends = accountService.getAllRequestFriendsPage(acc.getNickName(), PageRequest.of(page, size));
			if (lMyFriends.getSize() > 0) {
				return new ResponseEntity<>(lMyFriends, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	// https://localhost:8443/api/account/teams/?page=0&size=10
	@Operation(summary = "Get more teams from Account")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found more teams", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content) })
	@GetMapping("/teams")
	public ResponseEntity<Page<TeamDTO>> getMoreTeams(HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		
		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Account acc = accountOpp.get();

			Page<TeamDTO> lTeams = accountService.getAllTeamsPage(acc.getNickName(), PageRequest.of(page, size));
			if (lTeams.getSize() > 0) {
				return new ResponseEntity<>(lTeams, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}