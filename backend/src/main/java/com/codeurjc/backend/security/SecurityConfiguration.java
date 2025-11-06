package com.codeurjc.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

}
