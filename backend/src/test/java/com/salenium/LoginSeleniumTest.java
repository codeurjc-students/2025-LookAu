package com.salenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.codeurjc.backend.LookAu;
import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.repository.AccountRepository;

import io.github.bonigarcia.wdm.WebDriverManager;

import static io.restassured.RestAssured.given;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("selenium")
@SpringBootTest(classes = LookAu.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginSeleniumTest {

    private String baseUrl;
    private WebDriver driver;
    private WebDriverWait wait;

    @LocalServerPort
    private int port;

    @Autowired
    private AccountRepository accountRepository;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--accept-insecure-certs=true");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        baseUrl = "http://localhost:4200";
    }

    @AfterEach
    void tearDown() {
        //close selenium
        if (driver != null) {
            driver.quit();
        }
        //delete user
        if (testAccount != null && testAccount.getId() != null) {
            accountRepository.delete(testAccount);
        }
    }

    @Test
    @Order(4)
    void testLoginValid() {

        System.out.println("ORDERTEST: testLoginInvalid1" + driver.getCurrentUrl());

        String body = given()
            .formParam("username", "amanda.cl@gmail.com")
            .formParam("password", "password1")
        .when()
            .post(baseUrl + "/api/login")
        .then()
            .extract().body().asString();

        System.out.println("ORDERTEST: testLoginInvalid BODY" + body);

        
        driver.get(baseUrl+"/login");

        System.out.println("ORDERTEST: testLoginValid2" + driver.getCurrentUrl());

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".input-login-1")));

        System.out.println("ORDERTEST: testLoginInvalid3" + driver.getCurrentUrl());

        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        System.out.println("ORDERTEST: testLoginInvalid4" + driver.getCurrentUrl());

        wait.until(ExpectedConditions.urlContains("/teams"));

        System.out.println("ORDERTEST: testLoginInvalid5" + driver.getCurrentUrl());

        assertTrue(driver.getCurrentUrl().endsWith("/teams"));

        System.out.println("ORDERTEST: testLoginInvalid5" + driver.getCurrentUrl());
    }


    @Test
    @Order(1)
    void testLoginInvalid() {
        System.out.println("ORDERTEST: testLoginInvalid");

        driver.get(baseUrl+"/login");

        System.out.println("ORDERTEST: testLoginInvalid1" + driver.getCurrentUrl());

        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("wrong@nope.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("badpass");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        System.out.println("ORDERTEST: testLoginInvalid2" + driver.getCurrentUrl());

        wait.until(ExpectedConditions.urlContains("/login"));

        System.out.println("ORDERTEST: testLoginInvalid3" + driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("/login"));
        System.out.println("ORDERTEST: testLoginInvalid4" + driver.getCurrentUrl());
    }

    @Test
    @Order(3)
    void testSignupValid() {
        System.out.println("ORDERTEST: testSignupValid");
        driver.get(baseUrl+"/signup");

        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("Test");
        driver.findElement(By.name("nickName")).sendKeys("test");
        driver.findElement(By.name("mail")).sendKeys("test@gmail.com");
        driver.findElement(By.name("password")).sendKeys("test");
        driver.findElement(By.name("repeatPassword")).sendKeys("test");

        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));
        submitButton.click();

        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("/login") 
        ));

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/login") || currentUrl.contains("success"), "Wrong process.");
    }

    @Test
    @Order(2)
    void testSignupRepear() {
        System.out.println("ORDERTEST: testSignupRepear");

        //nick name repeat
        driver.get(baseUrl+"/signup");

        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("Test");
        driver.findElement(By.name("nickName")).sendKeys("LaTinyLoco");
        driver.findElement(By.name("mail")).sendKeys("test@gmail.com");
        driver.findElement(By.name("password")).sendKeys("test");
        driver.findElement(By.name("repeatPassword")).sendKeys("test");

        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));
        submitButton.click();

        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("/signup") 
        ));

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/signup"), "Wrong process.");

        //gmail repeat
        driver.get(baseUrl+"/signup");

        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("Test");
        driver.findElement(By.name("nickName")).sendKeys("test");
        driver.findElement(By.name("mail")).sendKeys("amanda.cl@gmail.com");
        driver.findElement(By.name("password")).sendKeys("test");
        driver.findElement(By.name("repeatPassword")).sendKeys("test");

        submitButton = driver.findElement(By.cssSelector("input[type='submit']"));
        submitButton.click();

        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("/signup") 
        ));

        currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/signup"), "Wrong process.");
    }


}
