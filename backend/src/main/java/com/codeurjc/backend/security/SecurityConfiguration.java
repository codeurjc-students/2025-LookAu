package com.codeurjc.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codeurjc.backend.security.jwt.JwtRequestFilter;
import com.codeurjc.backend.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public RepositoryUserDetailService userDetailService;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
		
		http
			.authorizeHttpRequests(authorize -> authorize


				//LOGIN AND REGISTER
				.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
				.requestMatchers(HttpMethod.GET,"/api/accounts/me").permitAll()
				.requestMatchers(HttpMethod.POST,"/api/accounts").permitAll()
				.requestMatchers(HttpMethod.PUT,"/api/accounts").permitAll()
				
				//PROFILE AND EDIT PROFILR
				.requestMatchers(HttpMethod.POST,"/api/accounts/image/current").permitAll()
				.requestMatchers(HttpMethod.POST,"/api/accounts/image").permitAll()
				.requestMatchers(HttpMethod.PUT,"/api/accounts/image").permitAll()
				.requestMatchers(HttpMethod.GET,"/api/accounts/myFriends/**").permitAll()	
				.requestMatchers(HttpMethod.POST,"/api/accounts/myFriends/**").permitAll()	
				.requestMatchers(HttpMethod.DELETE,"/api/accounts/myFriends/**").permitAll()	
				.requestMatchers(HttpMethod.PUT,"/api/accounts/pendingFriends/**").permitAll()
				.requestMatchers(HttpMethod.DELETE,"/api/accounts/pendingFriends/**").permitAll()
				.requestMatchers(HttpMethod.GET,"/api/accounts/**").permitAll()
				.requestMatchers(HttpMethod.PUT,"/api/accounts/**").permitAll()

				.requestMatchers(HttpMethod.PUT,"/api/accounts/teams").permitAll()

				//TEAMS
				//Main teams 
				.requestMatchers(HttpMethod.GET,"/api/teams/**").permitAll()

				//Tickets
				.requestMatchers(HttpMethod.GET,"/api/teams/*/tickets").permitAll()
				.requestMatchers(HttpMethod.GET,"/api/teams/*/tickets/**").permitAll()

				//Profile
				.requestMatchers(HttpMethod.POST,"/api/teams/**").permitAll()
				.requestMatchers(HttpMethod.GET,"/api/teams/*/image").permitAll()
				.requestMatchers(HttpMethod.PUT,"/api/teams/*/image").permitAll()
				

				//TICKETS
				.requestMatchers(HttpMethod.GET,"/api/tickets/**").permitAll()
				.requestMatchers(HttpMethod.POST,"/api/tickets/**").permitAll()
				.requestMatchers(HttpMethod.PUT,"/api/tickets/**").permitAll()
				.requestMatchers(HttpMethod.DELETE,"/api/tickets/**").permitAll()
				.requestMatchers(HttpMethod.GET,"/api/ticketTypes/**").permitAll()
				.requestMatchers(HttpMethod.PUT,"/api/ticketTypes/**").permitAll()
				.requestMatchers(HttpMethod.POST,"/api/ticketTypes/**").permitAll()

				//API LOTERIAS
				.requestMatchers(HttpMethod.POST,"/api/loteria/**").permitAll()
				






                    // PRIVATE ENDPOINTS
						//Chart
                    .requestMatchers(HttpMethod.GET,"/api/charts/exams").hasAnyRole("STUDENT")

						//Subjects
					.requestMatchers(HttpMethod.GET,"/api/subjects/*/teachers/").hasAnyRole( "ADMIN")		//get all
					.requestMatchers(HttpMethod.POST,"/api/subjects/*/teachers/").hasAnyRole( "ADMIN")		//set one
					.requestMatchers(HttpMethod.DELETE,"/api/subjects/*/teachers/").hasAnyRole( "ADMIN")	//delete one

					.requestMatchers(HttpMethod.GET,"/api/subjects/*/students/").hasAnyRole( "TEACHER")		//get all
					.requestMatchers(HttpMethod.POST,"/api/subjects/*/students/").hasAnyRole( "TEACHER")		//set one
					.requestMatchers(HttpMethod.DELETE,"/api/subjects/*/students/").hasAnyRole( "TEACHER")	//delete one

					.requestMatchers(HttpMethod.GET,"/api/subjects/*/exams/**").hasAnyRole("TEACHER", "STUDENT")		//get exam pdf
					.requestMatchers(HttpMethod.PUT,"/api/subjects/*/exams/**").hasAnyRole( "TEACHER", "STUDENT")	//set exam pdf

					.requestMatchers(HttpMethod.GET,"/api/subjects/*/exams/*/students/**").hasAnyRole("TEACHER")					//get examstduent pdf
					.requestMatchers(HttpMethod.PUT,"/api/subjects/*/exams/*/students/**").hasAnyRole( "TEACHER", "STUDENT")		//set examstduent pdf

					// .requestMatchers(HttpMethod.DELETE, "/api/subjects/**").hasAnyRole("ADMIN")
					// .requestMatchers(HttpMethod.PUT, "/api/subjects/**").hasAnyRole("ADMIN", "TEACHER")

					.requestMatchers(HttpMethod.GET, "/api/subjects/*/forums").hasAnyRole("STUDENT", "TEACHER")



						//Exam
					.requestMatchers(HttpMethod.GET,"/api/exams/").hasAnyRole( "TEACHER")	//pageable exam
					.requestMatchers(HttpMethod.GET,"/api/exams/**").hasAnyRole( "TEACHER", "STUDENT") //one exam
					.requestMatchers(HttpMethod.PUT,"/api/exams/**").hasAnyRole( "TEACHER") //update exam
					.requestMatchers(HttpMethod.DELETE,"/api/exams/**").hasAnyRole( "TEACHER") //delete exam
					.requestMatchers(HttpMethod.POST,"/api/exams/**").hasAnyRole( "TEACHER")	//create exam

						//ExamStudent
					.requestMatchers(HttpMethod.GET,"/api/examStudents/").hasAnyRole( "TEACHER")	//pageable examStudent
					.requestMatchers(HttpMethod.GET,"/api/examStudents/**").hasAnyRole( "TEACHER", "STUDENT") //one examStudent
					.requestMatchers(HttpMethod.PUT,"/api/examStudents/**").hasAnyRole(  "TEACHER") //update examStudent
					.requestMatchers(HttpMethod.DELETE,"/api/examStudents/**").hasAnyRole( "TEACHER") //delete examStudent
					.requestMatchers(HttpMethod.POST,"/api/examStudents/**").hasAnyRole( "TEACHER")	//create examStudent

						//Forum
					.requestMatchers(HttpMethod.GET,"/api/forum/").hasAnyRole( "TEACHER", "STUDENT")	//pageable forum
					.requestMatchers(HttpMethod.GET,"/api/forum/**").hasAnyRole( "TEACHER", "STUDENT") //one forum
					.requestMatchers(HttpMethod.PUT,"/api/forum/**").hasAnyRole(  "TEACHER") //update forum
					.requestMatchers(HttpMethod.DELETE,"/api/forum/**").hasAnyRole( "TEACHER") //delete forum
					.requestMatchers(HttpMethod.POST,"/api/forum/**").hasAnyRole( "TEACHER", "STUDENT")	//create forum
					
						//User
					//.requestMatchers(HttpMethod.GET,"/api/users/me").hasAnyRole( "TEACHER", "STUDENT", "ADMIN")	//user profile
					.requestMatchers(HttpMethod.GET,"/api/users/image").hasAnyRole( "TEACHER", "STUDENT", "ADMIN")
					.requestMatchers(HttpMethod.PUT, "/api/users/").hasAnyRole( "TEACHER", "STUDENT", "ADMIN")	//sign up
					.requestMatchers(HttpMethod.POST, "/api/users/enroll/**").permitAll()

					.requestMatchers(HttpMethod.GET,"/api/admins/*/subject/**").hasAnyRole("ADMIN")
					.requestMatchers(HttpMethod.GET,"/api/teachers/*/subject/**").hasAnyRole("TEACHER")
					.requestMatchers(HttpMethod.GET,"/api/teachers/**").hasAnyRole("TEACHER")
					.requestMatchers(HttpMethod.GET, "/api/students/download/**").hasAnyRole("STUDENT")
					.requestMatchers(HttpMethod.GET,"/api/users/recommendeds").hasAnyRole("STUDENT")	

					// PUBLIC ENDPOINTS
					.requestMatchers(HttpMethod.POST,"/api/users/").permitAll()			//create student
					.requestMatchers(HttpMethod.GET,"/api/subjects/").permitAll()			//subject
					.requestMatchers(HttpMethod.GET,"/api/charts/subjects").permitAll()		//chart 	
					.requestMatchers(HttpMethod.GET, "/api/subjects/*/examsStudent").permitAll()		
					.anyRequest().permitAll()
					
			);
		
        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/new/**").permitAll()
				
				//LOGIN - SIGNUP
				.requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/favicon.ico").permitAll()
				.requestMatchers("/signup").permitAll()
				.requestMatchers("/login").permitAll()
				.requestMatchers("/logout").permitAll()
				
				//PROFILE
				.requestMatchers("/searchAccounts").permitAll()
				.requestMatchers("/profile").permitAll()
				.requestMatchers("/acc/photo").permitAll()
				.requestMatchers("/acc/photoFriend/**").permitAll()	
				.requestMatchers("/sendPendingFriend/**").permitAll()
				.requestMatchers("/aceptPendingFriend/**").permitAll()
				.requestMatchers("/denyPendingFriend/**").permitAll()		

				//EDIT PROFILE
				.requestMatchers("/editProfile").permitAll()	

				//GROUPS
				.requestMatchers("/groups").permitAll()

				//PERSONAL
				.requestMatchers("/personal").permitAll()
				
				//STATS
				.requestMatchers("/stats").permitAll()	
				
				//OTHERS	
				.requestMatchers("/error").permitAll()	
				.requestMatchers("/moreMyFriends").permitAll()		
				.requestMatchers("/morePendingFriends").permitAll()
				.requestMatchers("/moreRequestFriends").permitAll()		
				.requestMatchers("/moreDeleteFriends").permitAll()

				
				
				
				
				
				.requestMatchers("/subjectInfo").permitAll()
				.requestMatchers("/chart/**").permitAll()
				.requestMatchers("/swagger-ui/**").permitAll()
				.requestMatchers("/v3/**").permitAll()

				// PRIVATE PAGES
				.requestMatchers("/subjects-registered/**").hasAnyRole("STUDENT", "TEACHER")
				.requestMatchers("/registered").hasAnyRole("STUDENT", "TEACHER", "ADMIN")
				.requestMatchers("/subjectUser").permitAll()
				.requestMatchers("/editProfile").hasAnyRole("STUDENT", "TEACHER", "ADMIN")
				.requestMatchers("/moreSubjectsAdmin/**").hasAnyRole("ADMIN")
				.requestMatchers("/moreSubjectsMain/**").hasAnyRole("ADMIN")
				.requestMatchers("/moreSubjectsRegistered/**").hasAnyRole("TEACHER", "STUDENT")
				.requestMatchers("/redirection/**").hasAnyRole("TEACHER", "STUDENT")
				.requestMatchers("/teachers/**").hasAnyRole("TEACHER")
				.requestMatchers("/students/**").hasAnyRole("STUDENT")
				.requestMatchers("/admins/**").hasAnyRole("ADMIN")
				.requestMatchers("/user/photo").hasAnyRole("STUDENT", "TEACHER", "ADMIN")

			)
			.formLogin(formLogin -> formLogin
				.loginPage("/login")
				.usernameParameter("email")
				.failureUrl("/login")
				.usernameParameter("email") 
				.passwordParameter("password") 
				.failureUrl("/login?error=true")
				.defaultSuccessUrl("/groups")
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
				.permitAll()
			);

		http.csrf(csrf -> csrf.ignoringRequestMatchers("/sendSolicitud"));

		return http.build();

	}

}
