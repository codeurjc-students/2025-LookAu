package com.salenium;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeurjc.backend.LookAu;
import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.repository.AccountRepository;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(classes = LookAu.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Autowired
    private AccountRepository accountRepository;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        
        //selenium with webdriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless=new"); 
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
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
    void testLoginValid() {
        driver.get("http://localhost:4200/login");

        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        wait.until(ExpectedConditions.urlContains("/teams"));

        assertTrue(driver.getCurrentUrl().endsWith("/teams"));
    }


    @Test
    void testLoginInvalid() {
        driver.get("http://localhost:4200/login");

        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("wrong@nope.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("badpass");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    void testSignupValid() {
        driver.get("http://localhost:4200/signup");

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
    void testSignupRepear() {

        //nick name repeat
        driver.get("http://localhost:4200/signup");

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
        driver.get("http://localhost:4200/signup");

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
