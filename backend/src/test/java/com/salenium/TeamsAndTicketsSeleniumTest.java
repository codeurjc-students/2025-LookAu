package com.salenium;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.codeurjc.backend.LookAu;
import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.repository.AccountRepository;

import io.github.bonigarcia.wdm.WebDriverManager;

@Tag("selenium")
@SpringBootTest(classes = LookAu.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TeamsAndTicketsSeleniumTest {

    private String baseUrl;
    private WebDriver driver;
    private WebDriverWait wait;

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
    void testCreateNewTeam() throws InterruptedException {
        driver.get(baseUrl + "/login");

        // Login
        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        driver.get(baseUrl + "/teams/newteam");

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
        driver.get(baseUrl + "/login");

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
        driver.get(baseUrl + "/login");

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
            By.xpath("//button[contains(., ' Edit ') and contains(@class, 'btn-dark')]")
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
        driver.get(baseUrl + "/login");

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


    @Test
    void testCreateTicketTeamBonolotoAndFilters() {

        //login
        driver.get(baseUrl + "/login");

        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        //open group
        wait.until(ExpectedConditions.urlContains("/teams"));

        String xpathTeamLink = String.format("//h3[contains(@class,'name-groups') and text()='%s']/ancestor::a", "Decramados Team");
        WebElement teamLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathTeamLink)));
        teamLink.click();

        //open new ticket
        wait.until(ExpectedConditions.urlContains("/tickets"));
        
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[.//i[contains(@class,'fa-ticket')]]")
        ));
        addButton.click();

        //fill the new ticket
        wait.until(ExpectedConditions.urlMatches(".*/tickets/new"));

            //type = bonoloto
        WebElement ticketTypeSelect = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("select[name='ticketType']")
        ));
        Select ticketTypeDropdown = new Select(ticketTypeSelect);
        ticketTypeDropdown.selectByVisibleText("Bonoloto");


            //date
        WebElement dateInput = driver.findElement(By.cssSelector("input[type='date']"));
        LocalDate today = LocalDate.now();
        dateInput.sendKeys(today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            //friend
        WebElement friendSelect = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("select[name='ticketPaidBy']")
        ));
        Select friendDropdown = new Select(friendSelect);
        friendDropdown.selectByIndex(1);

            //priece
        WebElement moneyInput = driver.findElement(By.cssSelector("input[placeholder='€€€€€']"));
        moneyInput.sendKeys("1");

        //fill the bonoloto bet
        WebElement bonolotoComponent = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.tagName("app-bonoloto")
        ));

        List<WebElement> bonolotoInputs = bonolotoComponent.findElements(By.cssSelector("input[type='text']"));

        if (bonolotoInputs.size() >= 6) {
            bonolotoInputs.get(0).sendKeys("5");
            bonolotoInputs.get(1).sendKeys("12");
            bonolotoInputs.get(2).sendKeys("19");
            bonolotoInputs.get(3).sendKeys("25");
            bonolotoInputs.get(4).sendKeys("33");
            bonolotoInputs.get(5).sendKeys("42");
        } else {
            throw new RuntimeException("No se encontraron los 6 inputs del Bonoloto.");
        }

        WebElement applyButton = bonolotoComponent.findElement(By.xpath("//button[contains(., 'Apply')]"));
        applyButton.click();

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(., 'Save New')]")
        ));
        saveButton.click();
    }


    @Test
    void testFilter(){

        //login
        driver.get(baseUrl + "/login");

        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        //open group
        wait.until(ExpectedConditions.urlContains("/teams"));

        String xpathTeamLink = String.format("//h3[contains(@class,'name-groups') and text()='%s']/ancestor::a", "Decramados Team");
        WebElement teamLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathTeamLink)));
        teamLink.click();
        
        //find the new ticket with filters
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement filterButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("button.btn-dark i.fa-filter")
        )).findElement(By.xpath("./.."));
        filterButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='date']")));

            //date filter
        LocalDate today = LocalDate.of(2025,12,3);
        WebElement dateInputFilter = driver.findElement(By.name("selectedDate"));
        dateInputFilter.clear();
        dateInputFilter.sendKeys(today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dateInputFilter.sendKeys(Keys.RETURN);

            //type filter
        Select ticketTypeSelectFilter = new Select(driver.findElement(By.name("ticketType")));
        ticketTypeSelectFilter.selectByVisibleText("Bonoloto");

            //find ticket after tickets
        List<WebElement> ticketCards = driver.findElements(By.cssSelector("a.card"));
        boolean found = ticketCards.stream().anyMatch(card ->
            card.getText().toLowerCase().contains("bonoloto") &&
            card.getText().contains(today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        );

        assertFalse(found, "The ticket isnt exist");
    }

    @Test
    void testLeaveTeam(){

        //login
        driver.get(baseUrl + "/login");

        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com");
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        //open group
        wait.until(ExpectedConditions.urlContains("/teams"));

        String xpathTeamLink = String.format("//h3[contains(@class,'name-groups') and text()='%s']/ancestor::a", "Motogoat");
        WebElement teamLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathTeamLink)));
        teamLink.click();

        //open edit group
        wait.until(ExpectedConditions.urlContains("/tickets"));

            //get team id
        String currentUrl = driver.getCurrentUrl();
        String[] parts = currentUrl.split("/");

        int teamsIndex = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("teams")) {
                teamsIndex = i;
                break;
            }
        }

        String teamId = null;
        if (teamsIndex != -1 && teamsIndex + 1 < parts.length) {
            teamId = parts[teamsIndex + 1];
        } 

            //click button
        String xpathEditButton = String.format("//a[contains(@href, '/teams/%s/edit')]", teamId);
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathEditButton)));
        editButton.click();

        //click button leave and confirm popup
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement leaveTeamButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//button[text()='Leave Team']")
        ));
        leaveTeamButton.click();

            //click confirm popup
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Confirm')]")));
        WebElement confirmBtn = driver.findElement(By.xpath("//button[contains(text(), 'Confirm')]"));
        confirmBtn.click();

        wait.until(ExpectedConditions.urlContains("/teams"));

        //asset group is delete
        boolean isNotVisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpathTeamLink)));
        assertTrue(isNotVisible, "El grupo 'Motogoat' sigue visible, no se eliminó correctamente.");
    }
}
