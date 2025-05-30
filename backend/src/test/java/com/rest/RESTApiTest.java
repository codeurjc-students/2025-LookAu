package com.rest;

import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;

import com.codeurjc.backend.LookAu;
import com.codeurjc.backend.service.AccountService;

import static io.restassured.RestAssured.*;

@SpringBootTest(classes = LookAu.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RESTApiTest {

    @Autowired
    AccountService accountService;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "https://localhost";
        RestAssured.basePath = "/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    void testReloadPendingTicketsFromTeam() {
        
        //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"alberto.lc@gmail.com\", \"password\": \"password2\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");

        Long teamId = accountService.getAllTeamsPage("Pepiflor23", PageRequest.of(0, 5)).getContent().get(0).getId();

        //do with team existen
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/loteria/teams/" + teamId)
        .then()
            .statusCode(200);

        //do with team non existen
        Long fakeTeamId = 99999L;

        given()
            .cookie("AuthToken", token)
        .when()
            .get("/loteria/teams/" + fakeTeamId)
        .then()
            .statusCode(404);
    }

    @Test
    void testReloadPendingTicketsFromProfile() {

        //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"eduardo.db@gmail.com\", \"password\": \"password7\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");

        //do with team existen
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/loteria/personal")
        .then()
            .statusCode(200);
    }


}
