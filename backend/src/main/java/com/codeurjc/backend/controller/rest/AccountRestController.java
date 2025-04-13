package com.codeurjc.backend.controller.rest;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
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
import org.springframework.web.service.annotation.DeleteExchange;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.DTO.AccountDTO;
import com.codeurjc.backend.model.DTO.RegisterAccountDTO;
import com.codeurjc.backend.service.AccountService;
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
@RequestMapping("/api/accounts")
public class AccountRestController {

	@Autowired
	private AccountService accountService;


	/**********************************/
	/******** PROFILES DETAILS ********/
	/**********************************/

	@GetMapping("/currentAccount")
	public ResponseEntity<Optional<Account>> currentAccount(HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			return ResponseEntity.notFound().build();
		}

		try {
			Optional<Account> currentAccount = accountService.getByEmail(request.getUserPrincipal().getName());
			return ResponseEntity.ok(currentAccount);
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}


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

	



	/*********************************/
	/******** ACTIONS FRIENDS ********/
	/*********************************/


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




	/******************************/
	/******** AJAX FRIENDS ********/
	/******************************/

	// https://localhost:8443/api/users/?page=0&size=10
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

	// https://localhost:8443/api/users/?page=0&size=10
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


	// https://localhost:8443/api/users/?page=0&size=10
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


}

	// @Operation(summary = "Update the profile of the user")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "200", description = "Profile updated", content = {
	// 				@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
	// 		@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
	// 		@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	// @PutMapping("/")
	// public ResponseEntity<User> editProfile(HttpServletRequest request, @RequestBody UserDTO post)
	// 		throws IOException, SQLException {

	// 	Optional<User> currentUser = userService.getByEmail(request.getUserPrincipal().getName());

	// 	if (currentUser.isPresent()) {
	// 		User user = this.getPrincipalUser(currentUser.get(), request);

	// 		if (user != null) {
	// 			user.setFirstName(post.getFirstName());
	// 			user.setLastName(post.getLastName());
	// 			user.setEmail(post.getEmail());
	// 			user.setPassword(post.getPassword());

	// 			userService.setUser(user);

	// 			return new ResponseEntity<>(user, HttpStatus.OK);
	// 		} else {
	// 			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 		}

	// 	} else {
	// 		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 	}

	// }

// 	@Operation(summary = "Get the image profile of the user")
// 	@ApiResponses(value = {
// 			@ApiResponse(responseCode = "200", description = "Found the image profile", content = {
// 					@Content(mediaType = "image/jpg") }),
// 			@ApiResponse(responseCode = "404", description = "image profile not found", content = @Content),
// 			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
// 	@GetMapping("/image")
// 	public ResponseEntity<byte[]> downloadFile(HttpServletRequest request) {

// 		Optional<User> currentUser = userService.getByEmail(request.getUserPrincipal().getName());

// 		if (currentUser.isPresent()) {
// 			byte[] imageData = currentUser.get().getProfilePicture();
// 			return ResponseEntity.ok()
// 					.header(HttpHeaders.CONTENT_TYPE, "image/png")
// 					.contentLength(imageData.length)
// 					.body(imageData);
// 		} else {
// 			return ResponseEntity.notFound().build();
// 		}

// 	}


// 	@Operation(summary = "Set the image profile of the user")
// 	@ApiResponses(value = {
// 			@ApiResponse(responseCode = "200", description = "Found the image profile", content = {
// 					@Content(mediaType = "image/jpg") }),
// 			@ApiResponse(responseCode = "404", description = "image profile not found", content = @Content),
// 			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
// 	@PutMapping("/image")
// 	public ResponseEntity<Object> updateFile(HttpServletRequest request, @RequestBody MultipartFile file) throws IOException {
// 		System.out.println("---------------------------------------------");
// 		System.out.println("---------------------------------------------");
// 		System.out.println("---------------------------------------------");
		
// 		byte[] foto;
// 		Optional<User> currentUser = userService.getByEmail(request.getUserPrincipal().getName());

// 		if (currentUser.isPresent()) {
// 			foto = file.getBytes();
// 			User user = this.getPrincipalUser(currentUser.get(), request);

// 			user.setProfilePicture(foto);
// 			userService.setUser(user);
			
// 			URI location = fromCurrentRequest().build().toUri();
			
// 			return ResponseEntity.created(location).build();
// 		} else {
// 			return ResponseEntity.notFound().build();
// 		}

// 	}

// 	@Operation(summary = "Get recomend subjects") // student and not registered
// 	@ApiResponses(value = {
// 			@ApiResponse(responseCode = "200", description = "Found games recomendations", content = {
// 					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
// 			@ApiResponse(responseCode = "404", description = "Not found games recomendation", content = @Content) })
// 	@GetMapping("/recommendeds")
// 	public ResponseEntity<List<Subject>> recomendations(HttpServletRequest request) {

// 		Optional<User> currentUser = userService.getByEmail(request.getUserPrincipal().getName());
// 		if (currentUser.isPresent() && currentUser.get().getRoles().contains("STUDENT")) {
// 			Student student = studentService.getStudentById(currentUser.get().getId());
// 			return new ResponseEntity<>(subjectService.recommendSubjects(student), HttpStatus.OK);
// 		} else {
// 			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// 		}
// 	}

// 	// https://localhost:8443/api/users/?page=0&size=10
// 	@Operation(summary = "Get more page subjects of the user")
// 	@ApiResponses(value = {
// 			@ApiResponse(responseCode = "200", description = "Found more subjects", content = {
// 					@Content(mediaType = "application/json", schema = @Schema(implementation = Subject.class)) }),
// 			@ApiResponse(responseCode = "404", description = "Subjects not found", content = @Content) })
// 	@GetMapping("/")
// 	public ResponseEntity<Page<Subject>> getMoreSubjects(HttpServletRequest request,
// 			@RequestParam(value = "page", defaultValue = "0") int page,
// 			@RequestParam(value = "size", defaultValue = "10") int size) {
// 		Optional<User> currentUser = userService.getByEmail(request.getUserPrincipal().getName());

// 		if (currentUser.isPresent()) {

// 			if (currentUser.get().getRoles().contains("STUDENT")) {
// 				Page<Subject> findSubjects = subjectService.getAllByStudentId(currentUser.get().getId(),
// 						PageRequest.of(page, size));
// 				if (findSubjects.getNumberOfElements() > 0) {
// 					return new ResponseEntity<>(findSubjects, HttpStatus.OK);
// 				} else {
// 					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// 				}

// 			} else {
// 				Page<Subject> findSubjects = subjectService.getAllByTeacherId(currentUser.get().getId(),
// 						PageRequest.of(page, size));
// 				if (findSubjects.getNumberOfElements() > 0) {
// 					return new ResponseEntity<>(findSubjects, HttpStatus.OK);
// 				} else {
// 					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// 				}
// 			}
// 		} else

// 		{
// 			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// 		}
// 	}


// 	@Operation(summary = "Subjects of the user")
// 	@ApiResponses(value = {
// 			@ApiResponse(responseCode = "200", description = "Found more subjects", content = {
// 					@Content(mediaType = "application/json", schema = @Schema(implementation = Subject.class)) }),
// 			@ApiResponse(responseCode = "404", description = "Subjects not found", content = @Content) })
// 	@GetMapping("/userSubjects")
// 	public ResponseEntity<?> getMoreSubjects(HttpServletRequest request) {
// 		Optional<User> currentUser = userService.getByEmail(request.getUserPrincipal().getName());

// 		if (currentUser.isPresent()) {

// 			if (currentUser.get().getRoles().contains("TEACHER")) {
// 				List<Subject> findSubjects = subjectService.getAllByTeacherIdNoPage(currentUser.get().getId());
// 				if (findSubjects.size() > 0) {
// 					return new ResponseEntity<>(findSubjects, HttpStatus.OK);
// 				} else {
// 					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// 				}

// 			} else {
// 				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
// 			}
// 		} else

// 		{
// 			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// 		}

// 	}




// 	@Operation(summary = "")
// 	@ApiResponses(value = {
// 			@ApiResponse(responseCode = "200", description = "Found the gmail", content = {
// 					@Content(mediaType = "image/jpg") }),
// 			@ApiResponse(responseCode = "404", description = "Gmail not found", content = @Content),
// 			@ApiResponse(responseCode = "403", description = "Forbiden o dont have permissions", content = @Content) })
// 	@GetMapping("/teacherGmail")
// 	public ResponseEntity<Boolean> isGmailTeacher(HttpServletRequest request, @RequestParam String gmail) {

// 		Optional<User> currentUser = userService.getByEmail(gmail);

// 		if (currentUser.isPresent()) {

// 			if (currentUser.get().getRoles().contains("TEACHER")) {
// 				return new ResponseEntity<>(true, HttpStatus.OK);
// 			} else {
// 				return new ResponseEntity<>(false, HttpStatus.OK);
// 			}

// 		} else {
// 			return new ResponseEntity<>(false, HttpStatus.OK);
// 		}

// 	}

// 	@Operation(summary = "")
// 	@ApiResponses(value = {
// 			@ApiResponse(responseCode = "200", description = "Found the gmail", content = {
// 					@Content(mediaType = "image/jpg") }),
// 			@ApiResponse(responseCode = "404", description = "Gmail not found", content = @Content),
// 			@ApiResponse(responseCode = "403", description = "Forbiden o dont have permissions", content = @Content) })
// 	@GetMapping("/studentGmail")
// 	public ResponseEntity<Boolean> isGmailUser(HttpServletRequest request, @RequestParam String gmail) {

// 		Optional<User> currentUser = userService.getByEmail(gmail);

// 		if (currentUser.isPresent()) {

// 			if (currentUser.get().getRoles().contains("STUDENT")) {
// 				return new ResponseEntity<>(true, HttpStatus.OK);
// 			} else {
// 				return new ResponseEntity<>(false, HttpStatus.OK);
// 			}

// 		} else {
// 			return new ResponseEntity<>(false, HttpStatus.OK);
// 		}

// 	}

// 	@Operation(summary = "")
// 	@ApiResponses(value = {
// 			@ApiResponse(responseCode = "200", description = "Found the gmail", content = {
// 					@Content(mediaType = "image/jpg") }),
// 			@ApiResponse(responseCode = "404", description = "Gmail not found", content = @Content),
// 			@ApiResponse(responseCode = "403", description = "Forbiden o dont have permissions", content = @Content) })
// 	@GetMapping("/userId")
// 	public ResponseEntity<?> userId(HttpServletRequest request, @RequestParam String gmail) {

// 		Optional<User> currentUser = userService.getByEmail(gmail);

// 		if (currentUser.isPresent()) {
// 			return new ResponseEntity<>(currentUser.get().getId(), HttpStatus.OK);

// 		} else {
// 			return new ResponseEntity<>("User nor found", HttpStatus.NOT_FOUND);
// 		}

// 	}


// 	@Operation(summary = "")
// 	@ApiResponses(value = {
// 			@ApiResponse(responseCode = "200", description = "Found the gmail", content = {
// 					@Content(mediaType = "image/jpg") }),
// 			@ApiResponse(responseCode = "404", description = "Gmail not found", content = @Content),
// 			@ApiResponse(responseCode = "403", description = "Forbiden o dont have permissions", content = @Content) })
// 	@GetMapping("/userName")
// 	public ResponseEntity<?> nameId(HttpServletRequest request, @RequestParam Long id) {

// 		Optional<User> currentUser = userService.getById(id);

// 		if (currentUser.isPresent()) {
// 			return new ResponseEntity<>(currentUser.get().getFirstName() + " " + currentUser.get().getLastName(), HttpStatus.OK);

// 		} else {
// 			return new ResponseEntity<>("User nor found", HttpStatus.NOT_FOUND);
// 		}

// 	}


// 	private User getPrincipalUser(User user, HttpServletRequest request) {
// 		if (user.getRoles().contains("TEACHER")) {
// 			user = teacherService.getTeacherByEmail(request.getUserPrincipal().getName());
// 		} else if (user.getRoles().contains("STUDENT")) {
// 			user = studentService.getStudentByEmail(request.getUserPrincipal().getName());
// 		} else if (user.getRoles().contains("ADMIN")) {
// 			user = adminService.getAdminByEmail(request.getUserPrincipal().getName());
// 		}

// 		return user;
// 	}

// 	@Operation(summary = "")
// 	@ApiResponses(value = {
// 			@ApiResponse(responseCode = "200", description = "Email send", content = {
// 					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
// 			@ApiResponse(responseCode = "403", description = "Forbiden o dont have permissions", content = @Content) })
// 	@PostMapping("/enroll/{id}/{studentid}")
// 	public ResponseEntity<Boolean> enrollInSubject(@PathVariable Long id, @PathVariable Long studentid) {
// 		Student student = studentService.getStudentById(studentid);
// 		Subject subject = subjectService.getSubjectById(id);
// 		Teacher teacher = subject.getTeachers().get(0);
// 		mailService.enviarCorreo(teacher.getEmail(), student, subject);
// 		return new ResponseEntity<>(true, HttpStatus.OK);
// 	}

// }
