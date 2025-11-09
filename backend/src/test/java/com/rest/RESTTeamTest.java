package com.rest;

import io.restassured.RestAssured;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.codeurjc.backend.LookAu;
import com.codeurjc.backend.service.AccountService;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(classes = LookAu.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RESTTeamTest {

    @Autowired
    AccountService accountService;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    void testGetTeam() {

        //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"amanda.cl@gmail.com\", \"password\": \"password1\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");

        Long teamId = accountService.getAllTeamsPage("LaTinyLoco", PageRequest.of(0, 5)).getContent().get(0).getId();

        //successful get team
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/teams/{teamId}", teamId) 
        .then()
            .statusCode(200)
            .body("id", equalTo(teamId.intValue())); 

        //non existen team
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/teams/{teamId}", 9999L)
        .then()
            .statusCode(404);
    }


    @Test
    void testUpdateTeam() {

        //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"amanda.cl@gmail.com\", \"password\": \"password1\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");

        Long teamId = accountService.getAllTeamsPage("LaTinyLoco", PageRequest.of(0, 5)).getContent().get(0).getId();
        String newNameJson = "{\"name\": \"test\"}";

        //update team
        given()
            .cookie("AuthToken", token)
            .contentType("application/json")
            .body(newNameJson)
        .when()
            .put("/teams/{teamId}", teamId)
        .then()
            .statusCode(200);

        //search the change
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/teams/{teamId}", teamId)
        .then()
            .statusCode(200)
            .body("name", equalTo("test"));
    }


    @Test
    void testDeleteAccountFromTeam() {

        //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"amanda.cl@gmail.com\", \"password\": \"password1\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");

        Long teamId = accountService.getAllTeamsPage("LaTinyLoco", PageRequest.of(0, 5)).getContent().get(0).getId();
        String nickName = "LaTinyLoco";

        //delete pepi from team
        given()
            .cookie("AuthToken", token)
            .queryParam("nickName", nickName)
        .when()
            .delete("/teams/{teamId}", teamId)
        .then()
            .statusCode(200);

        //get pepi from account (isnt there)
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/teams/{teamId}", teamId)
        .then()
            .statusCode(200)
            .body("accounts.nickName", not(hasItem(nickName)));
    }


    @Test
    void testSearchTeam() {

        //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"amanda.cl@gmail.com\", \"password\": \"password1\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");;

        String searchTerm = "test";

        //find
        given()
            .cookie("AuthToken", token)
            .queryParam("searchTerm", searchTerm)
        .when()
            .get("/teams/")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0))
            .body("name", everyItem(containsStringIgnoringCase(searchTerm)));
    }


    @Test
    void testGetTeamProfileImage() {
        
        //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"amanda.cl@gmail.com\", \"password\": \"password1\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");

        Long teamId = accountService.getAllTeamsPage("LaTinyLoco", PageRequest.of(0, 5)).getContent().get(0).getId();


        //get image form exitent team
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/teams/{teamId}/image", teamId)
        .then()
            .statusCode(200)
            .header("Content-Type", containsString("image/"));

        //get image form no exitent team
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/teams/{teamId}/image", 999999L)
        .then()
            .statusCode(404);
    }


    @Test
    void testUpdateTeamProfileImage() throws IOException {
        
        //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"amanda.cl@gmail.com\", \"password\": \"password1\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");

        Long teamId = accountService.getAllTeamsPage("Pepiflor23", PageRequest.of(0, 5)).getContent().get(0).getId();
        byte[] photo = this.getFile("C:\\Users\\amand\\Desktop\\2025-LookAu\\backend\\src\\main\\resources\\static\\images\\others\\flork_noprofile.jpg");

        //set the photo
        given()
            .cookie("AuthToken", token)
            .multiPart("file", "test-image.png", photo, "image/png")
        .when()
            .put("/teams/{teamId}/image", teamId)
        .then()
            .statusCode(201)
            .header("Location", notNullValue());

        //check the new photo
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/teams/{teamId}/image", teamId)
        .then()
            .statusCode(200)
            .header("Content-Type", containsString("image/"));
    }


    @Test
    void testCreateTeam() {

        //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"amanda.cl@gmail.com\", \"password\": \"password1\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");

       
        String teamJSON = """
            {
            "name": "test",
            "friendsTeam": ["Pepiflor23", "LaTinyLoco"]
            }
        """;

        //set new team
        given()
            .cookie("AuthToken", token)
            .contentType("application/json")
            .body(teamJSON)
        .when()
            .post("/teams/")
        .then()
            .statusCode(200)
            .body("name", equalTo("test"));
    }


    @Test
    void testGetAccountsOfTeam() {

        //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"amanda.cl@gmail.com\", \"password\": \"password1\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");

        Long teamId = accountService.getAllTeamsPage("LaTinyLoco", PageRequest.of(0, 5)).getContent().get(0).getId();


        //get accounts
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/teams/{idTeam}/accounts", teamId)
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }


    @Test
    void testGetAllTicketsOfTeam() {

        //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"amanda.cl@gmail.com\", \"password\": \"password1\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");

        Long teamId = accountService.getAllTeamsPage("LaTinyLoco", PageRequest.of(0, 5)).getContent().get(1).getId();


        //get all tickets
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/teams/{teamId}/ticketss", teamId)
        .then()
            .statusCode(200)
            .body("$", not(empty()))
            .body("[0].id", notNullValue());
    }


    @Test
    void testGetMoreTicketsOfTeam() {

        //with logging in
         //with logging in
        String token = 
            given()
                .contentType("application/json")
                .body("{\"username\": \"amanda.cl@gmail.com\", \"password\": \"password1\"}")
            .when()
                .post("/auth/login") 
            .then()
                .cookie("AuthToken")
                .extract()
                .cookie("AuthToken");

        Long teamId = accountService.getAllTeamsPage("LaTinyLoco", PageRequest.of(0, 5)).getContent().get(1).getId();


        //get tickets without filters
        given()
            .cookie("AuthToken", token)
            .queryParam("page", 0)
            .queryParam("size", 10)
            .queryParam("date", "")
            .queryParam("type", "")
        .when()
            .get("/teams/{idTeam}/tickets", teamId)
        .then()
            .statusCode(200)
            .body("content.size()", lessThanOrEqualTo(10))
            .body("content", not(empty()));

        //get tickets with filters
        given()
            .cookie("AuthToken", token)
            .queryParam("page", 0)
            .queryParam("size", 5)
            .queryParam("date", "2025-01-01") 
            .queryParam("type", "Bonoloto")   
        .when()
            .get("/teams/{idTeam}/tickets", teamId)
        .then()
            .statusCode(anyOf(is(200)));
    }



    private byte[] getFile(String url) throws IOException {

        Path path = Paths.get(url);

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}
