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
import com.codeurjc.backend.model.Team;
import com.codeurjc.backend.model.DTO.AccountDTO;
import com.codeurjc.backend.model.DTO.RegisterAccountDTO;
import com.codeurjc.backend.model.DTO.TeamDTO;
import com.codeurjc.backend.model.DTO.TicketTeamDTO;
import com.codeurjc.backend.service.AccountService;
import com.codeurjc.backend.service.TeamService;
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
@RequestMapping("/api/teams")
public class TeamRestController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private TeamService teamService;


	/*********************************/
	/******** TEAMS INTERFACE ********/
	/*********************************/

	@Operation(summary = "Get a team")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{teamId}")
	public ResponseEntity<?> updateTeam(HttpServletRequest request, @PathVariable Long teamId) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Optional<Team> teamOpp = teamService.getById(teamId);

			if (teamOpp.isPresent()) {
				return ResponseEntity.ok(new TeamDTO(teamOpp.get()));
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Update a team")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{teamId}")
	public ResponseEntity<?> getTeam(HttpServletRequest request, @PathVariable Long teamId, @RequestBody String name) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());
		String[] parts = name.split("\"");


		if (accountOpp.isPresent()) {

			Optional<Team> teamOpp = teamService.getById(teamId);

			if (teamOpp.isPresent()) {

				Team team = teamOpp.get();
				team.setName(parts[3]);
				teamService.setTeam(team);

				return ResponseEntity.ok(null);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Delete a account from a team")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@DeleteMapping("/{teamId}")
	public ResponseEntity<?> deleteAccountTeam(HttpServletRequest request, @PathVariable Long teamId, @RequestParam String nickName) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Optional<Team> teamOpp = teamService.getById(teamId);

			if (teamOpp.isPresent()) {

				Team team = teamOpp.get();
				team.getAccounts().removeIf(account -> nickName.equals(account.getNickName()));
				teamService.setTeam(team);

				Account account = accountOpp.get();
				account.getTeams().removeIf(teamRemove -> team.getName().equals(teamRemove.getName()));
				accountService.setAccount(account);

				if(team.getAccounts().size()==0){
					teamService.deleteTeam(team);
				}

				return ResponseEntity.ok(null);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@Operation(summary = "Search a team")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Searching friends", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/")
	public ResponseEntity<?> searchFriend(HttpServletRequest request, @RequestParam String searchTerm) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			return ResponseEntity.ok(teamService.getSearchingTeams(searchTerm, accountOpp.get().getTeams()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}



	/**********************************/
	/******** PROFILES DETAILS ********/
	/**********************************/

	@Operation(summary = "Get the image profile of a team")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Found the image profile", content = {
			@Content(mediaType = "image/jpg") }),
		@ApiResponse(responseCode = "404", description = "Image profile not found", content = @Content),
		@ApiResponse(responseCode = "403", description = "Forbidden or you don't have permissions", content = @Content) })
	@GetMapping("/{teamId}/image")
	public ResponseEntity<byte[]> downloadProfileImageByEmail(@PathVariable Long teamId) {

		Optional<Team> teamOpp = teamService.getById(teamId);

		if (teamOpp.isPresent()) {

			Team team = teamOpp.get();

			byte[] imageData = team.getProfilePicture();
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

	@Operation(summary = "Set the image profile of the team")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the image profile", content = {
					@Content(mediaType = "image/jpg") }),
			@ApiResponse(responseCode = "404", description = "image profile not found", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{teamId}/image")
	public ResponseEntity<Object> updateFile(HttpServletRequest request, @RequestBody MultipartFile file, @PathVariable Long teamId) throws IOException {

		byte[] foto;
		Optional<Account> currentUser = accountService.getByEmail(request.getUserPrincipal().getName());

		if (currentUser.isPresent()) {

			Optional<Team> teamOpp = teamService.getById(teamId);

			if (teamOpp.isPresent()) {
				foto = file.getBytes();
				Team team = teamOpp.get();

				team.setProfilePicture(foto);
				teamService.setTeam(team);
				
				URI location = fromCurrentRequest().build().toUri();
				
				return ResponseEntity.created(location).build();
			}else{
				return ResponseEntity.notFound().build();
			}

		} else {
			return ResponseEntity.notFound().build();
		}

	}


	@Operation(summary = "Create a team")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Team created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PostMapping("/")
	public ResponseEntity<?> register(@RequestBody TeamDTO post, HttpServletRequest request) throws IOException, SQLException {

		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			List<Account> lAccountToAddTeam = accountService.getAllByNicknames(Arrays.asList(post.getFriendsTeam()));
			lAccountToAddTeam.add(accountOpp.get());

			//add the account to the team
			Team team = new Team(post.getName());
			team.setAccounts(lAccountToAddTeam);

			//add the teams to the accounts
			for(Account acc: lAccountToAddTeam){
				acc.getTeams().add(team);
			}			

			//save both
			teamService.setTeam(team);
			accountService.setAccounts(lAccountToAddTeam);

			return ResponseEntity.ok(new TeamDTO(team));
		} else {
			return ResponseEntity.notFound().build();
		}
	}


	@Operation(summary = "Get all accounts of the team")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found more accounts", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "404", description = "Team not found", content = @Content) })
	@GetMapping("/{idTeam}/accounts")
	public ResponseEntity<List<String>> getMoreAcconutsTeams(HttpServletRequest request, @PathVariable String idTeam) {
		
		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {
			return ResponseEntity.ok(teamService.getAccountsTeam(Long.valueOf(idTeam)));		
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@Operation(summary = "Get all tickets of the team")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found more accounts", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "404", description = "Team not found", content = @Content) })
	@GetMapping("/{teamId}/ticketss")
	public ResponseEntity<?> getAllAcconutsTeams(HttpServletRequest request, @PathVariable String teamId) {
		
		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Long idTeamLong = Long.valueOf(teamId);
        	Optional<Team> teamOpp = teamService.getById(idTeamLong);

			if(teamOpp.isPresent()){
				return new ResponseEntity<>(teamService.getAllTicketTeams(teamOpp.get().getTickets()), HttpStatus.OK);
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

	// https://localhost:8443/api/teams/id/tickets?page=0&size=10
	@Operation(summary = "Get more tickets of the team")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found more accounts", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
			@ApiResponse(responseCode = "404", description = "Team not found", content = @Content) })
	@GetMapping("/{idTeam}/tickets")
	public ResponseEntity<Page<TicketTeamDTO>> getMoreAcconutsTeams(HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "date", defaultValue = "") String date,
			@RequestParam(value = "type", defaultValue = "") String type,
			@PathVariable String idTeam) {
		
		Optional<Account> accountOpp = accountService.getByEmail(request.getUserPrincipal().getName());

		if (accountOpp.isPresent()) {

			Page<TicketTeamDTO> lMyFriends;

			if(type.equals("") && date.equals("")){	//if the filters are on
				lMyFriends = teamService.getTeamTicketsPaged(idTeam, PageRequest.of(page, size));
			}else{
				lMyFriends = teamService.getTeamTicketsFilterPaged(idTeam, date, type, PageRequest.of(page, size));
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
}