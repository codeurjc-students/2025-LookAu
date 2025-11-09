package com.rest;

import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.codeurjc.backend.LookAu;
import com.codeurjc.backend.model.DTO.typeDTO.BonolotoDTO;
import com.codeurjc.backend.model.types.Bonoloto;
import com.codeurjc.backend.service.AccountService;
import com.codeurjc.backend.service.TicketService;
import com.codeurjc.backend.service.TicketTypeService;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = LookAu.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RESTTicketTypeTest {

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
    void testGetTicketType() {

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

        Long ticketTypeId = ticketTypeService.getOne().getId();

        //get tickettype
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/ticketTypes/" + ticketTypeId)
        .then()
            .statusCode(200)
            .body("id", equalTo(ticketTypeId.intValue()));

        //get wrong tickettype
        Long fakeId = 999999L;
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/ticketTypes/" + fakeId)
        .then()
            .statusCode(404);
    }


    @Test
    void testSaveTicketBonoloto() {
        
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

        Bonoloto bonoloto = new Bonoloto();

        BonolotoDTO dto = new BonolotoDTO(bonoloto.getId(), 1, 1,1, 1, 1, 1);
        dto.setNum1(1);
        dto.setNum2(2);
        dto.setNum3(3);
        dto.setNum4(4);
        dto.setNum5(5);
        dto.setNum6(6);

        //send new bonoloto
        given()
            .cookie("AuthToken", token)
            .contentType("application/json")
            .body(dto)
        .when()
            .post("/ticketTypes/bonoloto")
        .then()
            .statusCode(200)
            .body(notNullValue());
    }

}