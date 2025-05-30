package com.rest;

import io.restassured.RestAssured;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;

import com.codeurjc.backend.LookAu;
import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.DTO.TicketTeamDTO;
import com.codeurjc.backend.service.AccountService;
import com.codeurjc.backend.service.TicketService;
import com.codeurjc.backend.service.TicketTypeService;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = LookAu.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RESTTicketTest {

    @Autowired
    AccountService accountService;
    
    @Autowired
    TicketService ticketService;

    @Autowired
    TicketTypeService ticketTypeService;

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
    void testGetTicketSave() {

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

        //get tickets
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/tickets/")
        .then()
            .statusCode(200)
            .body(isEmptyOrNullString());
    }


    @Test
    void testGetTicket() {

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

        Long ticketId = ticketService.getOne().getId();


        //get existen id ticket
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/tickets/" + ticketId)
        .then()
            .statusCode(200)
            .body("id", equalTo(ticketId.intValue())); 


        //get non existen id ticket
        Long fakeTicketId = 99999L;

        given()
            .cookie("AuthToken", token)
        .when()
            .get("/tickets/" + fakeTicketId)
        .then()
            .statusCode(404);

        //get ticket not login
        given()
        .when()
            .get("/tickets/" + ticketId)
        .then()
            .statusCode(500);
    }


    @Test
    void testGetTicketType() {

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

        Ticket ticket = ticketService.getOne();

        //get existen id ticket
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/tickets/" + ticket.getId() + "/ticketType")
        .then()
            .statusCode(200)
            .body("id", equalTo(ticket.getTicketType().getId().intValue()));

       
        //get not existen id ticket
        Long fakeTicketId = 99999L;

        given()
            .cookie("AuthToken", token)
        .when()
            .get("/tickets/" + fakeTicketId + "/ticketType")
        .then()
            .statusCode(404);
    }


    @Test
    void testEditTicket() throws Exception {

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

        Long ticketId = ticketService.getOne().getId();

        TicketTeamDTO ticketDTO = new TicketTeamDTO();
        ticketDTO.setDate(LocalDate.now().toString());   
        ticketDTO.setPaidByName("test");
        ticketDTO.setPaidByPice("12.50"); 
        ticketDTO.setClaimedBy("test");
        ticketDTO.setStatusName("Pending");

        //send update existen ticket
        given()
            .cookie("AuthToken", token)
            .contentType("application/json")
            .body(ticketDTO)
        .when()
            .put("/tickets/" + ticketId)
        .then()
            .statusCode(200);

        //send update non existen ticket
        Long fakeTicketId = 999999L;

        given()
            .cookie("AuthToken", token)
            .contentType("application/json")
            .body(ticketDTO)
        .when()
            .put("/tickets/" + fakeTicketId)
        .then()
            .statusCode(404);
    }


    @Test
    void testCreateNewTicket() throws Exception {

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

        Long ticketTypeId = ticketTypeService.getOne().getId();

        TicketTeamDTO ticketDTO = new TicketTeamDTO();
        ticketDTO.setDate(LocalDate.now().toString());
        ticketDTO.setPaidByName("test");
        ticketDTO.setPaidByPice("15.00");
        ticketDTO.setClaimedBy("test");
        ticketDTO.setTicketTypeId(ticketTypeId.toString());

        Long teamId = accountService.getAllTeamsPage("Pepiflor23", PageRequest.of(0, 5)).getContent().get(0).getId();


        //send new existen ticket
        given()
            .cookie("AuthToken", token)
            .contentType("application/json")
            .queryParam("teamId", teamId)
            .body(ticketDTO)
        .when()
            .post("/tickets/")
        .then()
            .statusCode(200);

        //send new existen ticket without real team
        Long fakeTeamId = 0L;

        given()
            .cookie("AuthToken", token)
            .contentType("application/json")
            .queryParam("teamId", fakeTeamId)
            .body(ticketDTO)
        .when()
            .post("/tickets/")
        .then()
            .statusCode(200);
    }

    @Test
    void testDeleteTicket() throws Exception {

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


        Long ticketId = ticketService.getOne().getId();

        //delete existente
        given()
            .cookie("AuthToken", token)
        .when()
            .delete("/tickets/" + ticketId)
        .then()
            .statusCode(200);

        //delete non existente
        Long fakeTicketId = 999999L;

        given()
            .cookie("AuthToken", token)
        .when()
            .delete("/tickets/" + fakeTicketId)
        .then()
            .statusCode(404);
    }




}
