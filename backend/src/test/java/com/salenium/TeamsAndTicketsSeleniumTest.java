package com.salenium;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

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
public class TeamsAndTicketsSeleniumTest {

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
    void testCreateNewTeam() throws InterruptedException {
        driver.get("http://localhost:4200/login");

        // Login
        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        driver.get("http://localhost:4200/teams/newteam");

        //create new team
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//h3[contains(text(), 'TEAM DETAILS')]")
        ));

        WebElement nameInput = driver.findElement(By.id("teamNameInput"));
        nameInput.sendKeys("Test");

        WebElement saveBtn = driver.findElement(By.cssSelector(".fa-check"));
        saveBtn.click();

        wait.until(ExpectedConditions.urlContains("/teams"));

        assertTrue(driver.getCurrentUrl().contains("/teams"), "No se redirigió correctamente a /teams tras guardar el equipo.");
    }


    @Test
    void testEditTicketTeamDiscard() {

        //login
        driver.get("http://localhost:4200/login");

        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        //open group
        wait.until(ExpectedConditions.urlContains("/teams"));

        String xpathTeamLink = String.format("//h3[contains(@class,'name-groups') and text()='%s']/ancestor::a", "Decramados Team");
        WebElement teamLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathTeamLink)));
        teamLink.click();

        //open ticket
        WebElement firstTicket = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a.card")
        ));
        firstTicket.click();

        //open edit ticket
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(., 'Edit') and contains(@class, 'btn-dark')]")
        ));
        editButton.click();

        //change price un paid by
        WebElement paidByInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='text' and contains(@class, 'form-control')]")
        ));
        paidByInput.clear();
        paidByInput.sendKeys("2");

        //click discard button and verify
        WebElement discardButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Discard') and contains(@class, 'btn-dark')]")
        ));
        discardButton.click();

        wait.until(ExpectedConditions.urlContains("/tickets"));

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/tickets"), "La URL actual no es la esperada: " + currentUrl);
    }


    @Test
    void testEditTicketTeamSave() {

        //login
        driver.get("http://localhost:4200/login");

        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        //open group
        wait.until(ExpectedConditions.urlContains("/teams"));

        String xpathTeamLink = String.format("//h3[contains(@class,'name-groups') and text()='%s']/ancestor::a", "Decramados Team");
        WebElement teamLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathTeamLink)));
        teamLink.click();

        //open ticket
        WebElement firstTicket = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a.card")
        ));
        firstTicket.click();

        //open edit ticket
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(., 'Edit') and contains(@class, 'btn-dark')]")
        ));
        editButton.click();

        //change price un paid by
        WebElement paidByInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='text' and contains(@class, 'form-control')]")
        ));
        paidByInput.clear();
        paidByInput.sendKeys("2");

        //click save button and verify
        WebElement discardButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Save') and contains(@class, 'btn-dark')]")
        ));
        discardButton.click();

        wait.until(ExpectedConditions.urlContains("/tickets"));

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/tickets"), "La URL actual no es la esperada: " + currentUrl);
    
    }


    @Test
    void testEditTicketTeamQuinielaSave() {

        //login
        driver.get("http://localhost:4200/login");

        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        //open group
        wait.until(ExpectedConditions.urlContains("/teams"));

        String xpathTeamLink = String.format("//h3[contains(@class,'name-groups') and text()='%s']/ancestor::a", "Decramados Team");
        WebElement teamLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathTeamLink)));
        teamLink.click();

        //open ticket
        WebElement firstTicket = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a.card")
        ));
        firstTicket.click();

        //open edit ticket
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(., 'Edit') and contains(@class, 'btn-dark')]")
        ));
        editButton.click();

        //change price un paid by
        WebElement paidByInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='text' and contains(@class, 'form-control')]")
        ));
        paidByInput.clear();
        paidByInput.sendKeys("2");

        //edit quiniela
        List<WebElement> deleteButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.xpath("//button[.//i[contains(@class, 'fa-trash')]]")
        ));
        
        //get trash button and apply
        assertFalse(deleteButtons.isEmpty(), "No se encontraron botones de borrar columna.");
        deleteButtons.get(0).click();
        WebElement applyButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Apply')]")
        ));
        applyButton.click();

        //click save button and verify
        WebElement discardButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Save') and contains(@class, 'btn-dark')]")
        ));
        discardButton.click();

        wait.until(ExpectedConditions.urlContains("/tickets"));

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/tickets"), "Wrong URL: " + currentUrl);
    
    }


}
