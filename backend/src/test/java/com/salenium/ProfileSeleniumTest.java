package com.salenium;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.codeurjc.backend.LookAu;
import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.repository.AccountRepository;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(classes = LookAu.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProfileSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

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
        
        // Headless configuration
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        baseUrl = "https://localhost:8443";
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
    void testAddFriend() {

        //loggin
        driver.get(baseUrl + "/login");
        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com"); 
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");            
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        wait.until(ExpectedConditions.urlContains("/teams"));

        //check profile add a friend
        driver.get(baseUrl + "/profile");

        WebElement inputBuscar = wait.until(ExpectedConditions.elementToBeClickable(By.id("stringToFind")));
        inputBuscar.clear();
        inputBuscar.sendKeys("mi");

        //send text mi
        driver.findElement(By.cssSelector("button.btn-background")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
            "//p[contains(@class, 'card-status') and contains(text(), 'Miguelog')]"
        )));

        //click fiend Miguelog
        WebElement amigoDiv = driver.findElement(By.xpath(
            "//p[contains(@class, 'card-status') and contains(text(), 'Miguelog')]/ancestor::div[contains(@class, 'card-ticket')]"
        ));
        WebElement botonEnviar = amigoDiv.findElement(By.xpath(".//a[i[contains(@class, 'fa-check')]]"));
        botonEnviar.click();

        //check if miguelog is in request friends
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@id='moreMyFriends']//p[contains(@class, 'card-status') and contains(text(), 'Miguelog')]"
        )));

        assertTrue(true); 

    }

    @Test
    void testAceptPendingFriendAndAjaxMyFriends() {

        // login
        driver.get(baseUrl + "/login");
        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("eduardo.db@gmail.com");  
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password7");            
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        wait.until(ExpectedConditions.urlContains("/teams"));

        //acept pending requets
        driver.get(baseUrl + "/profile");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //get akalpaca and acept request
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@id='moreMyFriends']//p[contains(@class, 'card-status') and text()='Akalpaca']")

        ));
        WebElement acceptIcon = driver.findElement(By.xpath(
            "//div[@id='moreMyFriends']//p[contains(@class, 'card-status') and text()='Akalpaca']/ancestor::div[contains(@class,'card-ticket')]//i[contains(@class, 'fa-check')]"
        ));
        acceptIcon.click();


        //delete from the pending list
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//div[@id='pendingFriends']//p[contains(@class, 'card-status') and text()='Akalpaca']")
        ));

        //check if akalpaca is in the list witj ajax
        By friendSelector = By.xpath("//div[@id='moreMyFriends']//p[contains(@class, 'card-status') and text()='Akalpaca']");
        By moreBtnSelector = By.xpath("//div[@id='moreMyFriends']/following-sibling::div//a[contains(text(), 'More')]");

        boolean found = false;
        int attempts = 0;
        int maxAttempts = 5;

        while (!found && attempts < maxAttempts) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(friendSelector));
                found = true;
            } catch (TimeoutException e) {
                try {
                    WebElement moreBtn = driver.findElement(moreBtnSelector);
                    if (moreBtn.isDisplayed() && moreBtn.isEnabled()) {
                        moreBtn.click();
                        Thread.sleep(1500); 
                    } else {
                        break;
                    }
                } catch (NoSuchElementException | InterruptedException ignored) {
                    break;
                }
            }
            attempts++;
        }

        assertTrue(found, "No se encontró a Akalpaca en la lista de amigos tras aceptar.");
    }


    @Test
    void testDenyPendingFriendAndAjaxMyFriends() {

        //login
        driver.get(baseUrl + "/login");
        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("eduardo.db@gmail.com");  
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password7");            
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        wait.until(ExpectedConditions.urlContains("/teams"));

        //deny pending request (like the other but with dufferent buttons9)
        driver.get(baseUrl + "/profile");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@id='moreMyFriends']//p[contains(@class, 'card-status') and text()='DaniDu']")
        ));
        WebElement denyIcon = driver.findElement(By.xpath(
            "//div[@id='moreMyFriends']//p[contains(@class, 'card-status') and text()='DaniDu']/ancestor::div[contains(@class,'card-ticket')]//i[contains(@class, 'fa-times')]"
        ));
        denyIcon.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//div[@id='moreMyFriends']//p[contains(@class, 'card-status') and text()='DaniDu']")
        ));

        By friendSelector = By.xpath("//div[@id='moreMyFriends']//p[contains(@class, 'card-status') and text()='DaniDu']");
        By moreBtnSelector = By.xpath("//div[@id='moreMyFriends']/following-sibling::div//a[contains(text(), 'More')]");

        boolean found = false;
        int attempts = 0;
        int maxAttempts = 5;

        while (!found && attempts < maxAttempts) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(friendSelector));
                found = true;
            } catch (TimeoutException e) {
                try {
                    WebElement moreBtn = driver.findElement(moreBtnSelector);
                    if (moreBtn.isDisplayed() && moreBtn.isEnabled()) {
                        moreBtn.click();
                        Thread.sleep(1500); 
                    } else {
                        break;
                    }
                } catch (NoSuchElementException | InterruptedException ignored) {
                    break;
                }
            }
            attempts++;
        }

        assertFalse(found, "DaniDu debería haber sido eliminada, pero sigue apareciendo en la lista.");
    }




    @Test
    void testDeleteFriend() {

        //loggin
        driver.get(baseUrl + "/login");
        driver.findElement(By.cssSelector(".input-login-1")).sendKeys("amanda.cl@gmail.com");  // Cambia por usuario válido
        driver.findElement(By.cssSelector(".input-login-2")).sendKeys("password1");            // Cambia por contraseña válida
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        wait.until(ExpectedConditions.urlContains("/teams"));

        //check editprofile delete
        driver.get(baseUrl + "/editProfile");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //get akalpaca trsh button and click delete
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@id='moreMyFriends']//p[contains(@class, 'card-status') and text()='Akalpaca']")
        ));
        WebElement deleteIcon = driver.findElement(By.xpath(
            "//div[@id='moreMyFriends']//p[contains(@class, 'card-status') and text()='Akalpaca']/ancestor::div[contains(@class,'card-ticket')]//i[contains(@class, 'fa-trash')]"
        ));
        deleteIcon.click();

        //click confirn inmodel popup
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Confirm')]")));
        WebElement confirmBtn = driver.findElement(By.xpath("//button[contains(text(), 'Confirm')]"));
        confirmBtn.click();

        //check akalpaca isnt in the fiends list
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//div[@id='moreMyFriends']//p[contains(@class, 'card-status') and text()='Akalpaca']")
        ));


    }
    

}
