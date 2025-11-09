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
import org.springframework.transaction.annotation.Transactional;

import com.codeurjc.backend.DataInitializer;
import com.codeurjc.backend.LookAu;
import com.codeurjc.backend.repository.AccountRepository;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(classes = LookAu.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RESTAccountTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() throws Exception {

        

        RestAssured.port = port;
        RestAssured.baseURI = "https://localhost";
        RestAssured.basePath = "/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
    }


    @Test
    void testMe() {

        //without logging in
        given()
        .when()
            .get("/accounts/me")
        .then()
            .statusCode(403);


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

        
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/accounts/me")
        .then()
            .statusCode(200)
            .body("account.nickName", equalTo("Pepiflor23"));
    }

    @Test
    void testRegisterAccount() {

        //new user
        String accountJson = """
            {
                "nickName": "test",
                "firstName": "test",
                "lastName": "test",
                "email": "test@gmail.com",
                "password": "test"
            }
        """;

        //successfull register
        given()
            .contentType("application/json")
            .body(accountJson)
        .when()
            .post("/accounts/")
        .then()
            .statusCode(201)
            .body("email", equalTo("test@gmail.com"))
            .body("nickName", equalTo("test"))
            .body("firstName", equalTo("test"))
            .body("lastName", equalTo("test"));

        //repeat email
        given()
            .contentType("application/json")
            .body(accountJson)
        .when()
            .post("/accounts/")
        .then()
            .statusCode(403)
            .body(equalTo("Error: Email already registered"));


        String accountJsonRepeatNickName = """
            {
                "nickName": "test",
                "firstName": "test2",
                "lastName": "test2",
                "email": "test2@gmail.com",
                "password": "test2"
            }
        """;

        //repeat nickName
        given()
            .contentType("application/json")
            .body(accountJsonRepeatNickName)
        .when()
            .post("/accounts/")
        .then()
            .statusCode(409)
            .body(equalTo("Error: Nickname already taken"));
    }


    @Test
    void testGetPhoto(){

        //successfull get photo
        given()
            .queryParam("nickName", "Pepiflor23")
        .when()
            .get("/accounts/image")
        .then()
            .statusCode(200)
            .header("Content-Type", "image/png")
            .header("Content-Length", notNullValue())
            .body(notNullValue());

        //user dont exists
        given()
            .queryParam("nickName", "NoUserExisten")
        .when()
            .get("/accounts/image")
        .then()
            .statusCode(500);
    }


    @Test
    void testSetPhoto() throws IOException{

        //login
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


        //get photo
        byte[] photo = this.getFile("C:\\Users\\amand\\Desktop\\2025-LookAu\\backend\\src\\main\\resources\\static\\images\\others\\flork_noprofile.jpg");

        //successfull set photo
        given()
            .cookie("AuthToken", token)
            .multiPart("file", "profile.png", photo, "image/png")
        .when()
            .put("/accounts/image")
        .then()
            .statusCode(201);

    }

    @Test
    void testEditProfile() {

        //successfull login
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

        //update account
        given()
            .cookie("AuthToken", token)
            .contentType("application/json")
            .body("""
                {
                    "firstName": "Alberta",
                    "lastName": "Liminoa",
                    "password": "pass2"
                }
            """)
        .when()
            .put("/accounts/")
        .then()
            .statusCode(200);
    }


    @Test
    void testSendFriendRequest() {
       
        //successfull login
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

        //send request
        given()
            .cookie("AuthToken", token)
        .when()
            .put("/accounts/Miguelog")
        .then()
            .statusCode(200);
    }


    @Test
    void testSearchFriend() {

       //successfull login
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

        //search a friend
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/accounts/LaTi")
        .then()
            .statusCode(200)
            .body(not(empty()));  
    }


    @Test
    void testSearchMyFriend() {
        
        //successfull login
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

        //search in my friends
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/accounts/myFriends/LaTi")
        .then()
            .statusCode(200)
            .body(not(empty())); 
    }


    @Test
    void testDeleteMyFriend() {
        
        //successfull login
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

        //delete my friends
        given()
            .cookie("AuthToken", token)
        .when()
            .delete("/accounts/myFriends/LaTinyLoco")
        .then()
            .statusCode(200)
            .body(emptyOrNullString());

        //delete no my friend
        given()
            .cookie("AuthToken", token)
        .when()
            .delete("/accounts/myFriends/Aderudo")
        .then()
            .statusCode(403)
            .body(equalTo("Error"));
    }


    @Test
    void testAceptPendingFriend() {
        
        //successfull login
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

        //acept pending friends
        given()
            .cookie("AuthToken", token)
        .when()
            .put("/accounts/pendingFriends/DaniDu")
        .then()
            .statusCode(200)
            .body(emptyOrNullString());

        //acept no pending friends
        given()
            .cookie("AuthToken", token)
        .when()
            .put("/accounts/pendingFriends/Pepiflor23")
        .then()
            .statusCode(403)
            .body(equalTo("Error"));
    }


    @Test
    void testDenyPendingFriend() {
        
        //successfull login
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

        //deny pending friends
        given()
            .cookie("AuthToken", token)
        .when()
            .delete("/accounts/pendingFriends/Pepiflor23")
        .then()
            .statusCode(200)
            .body(emptyOrNullString());

        //deny no pending friends
        given()
            .cookie("AuthToken", token)
        .when()
            .delete("/accounts/pendingFriends/Pepiflor23")
        .then()
            .statusCode(403)
            .body(equalTo("Error"));
    }


    @Test
    void testGetAllTicketsAccount() {

        //login
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

        //successfull get tickets
        given()
            .cookie("AuthToken", token)
        .when()
            .get("/accounts/ticketss")
        .then()
            .statusCode(200)
            .body("$", notNullValue());

        //forbidden user
        given()
            .cookie("AuthToken", new String("Hola"))
        .when()
            .get("/accounts/ticketss")
        .then()
            .statusCode(500);
    }


    @Test
    void testGetMoreAccountTickets() {

        //login
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

        //successfull get ajax tickets with filters
        given()
            .cookie("AuthToken", token)
            .queryParam("page", 0)
            .queryParam("size", 5)
            .queryParam("date", "")  
            .queryParam("type", "Bonoloto")
        .when()
            .get("/accounts/tickets")
        .then()
            .statusCode(200)
            .body("content", not(empty()));

        //successfull get ajax tickets without filters
        given()
            .cookie("AuthToken", token)
            .queryParam("page", 0)
            .queryParam("size", 5)
            .queryParam("date", "")  
            .queryParam("type", "Bonoloto")
        .when()
            .get("/accounts/tickets")
        .then()
            .statusCode(200)
            .body("content", not(empty()));

        //forbidden user
        given()
            .cookie("AuthToken", new String("Hola")) 
            .queryParam("page", 0)
            .queryParam("size", 5)
            .queryParam("date", "")
            .queryParam("type", "")
        .when()
            .get("/accounts/tickets")
        .then()
            .statusCode(500);
    }


    @Test
    void testGetMoreMyFriends() {

        //login
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
        
        //get more my friends
        given()
            .cookie("AuthToken", token)
            .queryParam("page", 0)
            .queryParam("size", 5)
        .when()
            .get("/accounts/myFriends")
        .then()
            .statusCode(200)
            .body("content", not(empty()));

        //forbidden user
        given()
            .cookie("AuthToken", new String("Hola"))
            .queryParam("page", 0)
            .queryParam("size", 5)
        .when()
            .get("/accounts/myFriends")
        .then()
            .statusCode(500);
    }


    @Test
    void testGetMorePendingFriends() {

        //login
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
        
        //get more pending friends
        given()
            .cookie("AuthToken", token)
            .queryParam("page", 0)
            .queryParam("size", 5)
        .when()
            .get("/accounts/pendingFriends")
        .then()
            .statusCode(200)
            .body("content", not(empty()));

        //forbidden user
        given()
            .cookie("AuthToken", new String("Hola"))
            .queryParam("page", 0)
            .queryParam("size", 5)
        .when()
            .get("/accounts/pendingFriends")
        .then()
            .statusCode(500);
    }


    @Test
    void testGetMoreRequestFriends() {

        //login
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

        //set one pending friend
        given()
            .cookie("AuthToken", token)
        .when()
            .put("/accounts/Miguelog")
        .then()
            .statusCode(200);
        
        //get more request friends
        given()
            .cookie("AuthToken", token)
            .queryParam("page", 0)
            .queryParam("size", 5)
        .when()
            .get("/accounts/requestFriends")
        .then()
            .statusCode(200)
            .body("content", not(empty()));

        //forbidden user
        given()
            .cookie("AuthToken", new String("Hola"))
            .queryParam("page", 0)
            .queryParam("size", 5)
        .when()
            .get("/accounts/requestFriends")
        .then()
            .statusCode(500);
    }


    @Test
    void testGetMoreTeams() {

        //login
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
        
        //get more teams
        given()
            .cookie("AuthToken", token)
            .queryParam("page", 0)
            .queryParam("size", 5)
        .when()
            .get("/accounts/teams")
        .then()
            .statusCode(200)
            .body("content", not(empty()));

        //forbidden user
        given()
            .cookie("AuthToken", new String("Hola"))
            .queryParam("page", 0)
            .queryParam("size", 5)
        .when()
            .get("/accounts/teams")
        .then()
            .statusCode(500);
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
