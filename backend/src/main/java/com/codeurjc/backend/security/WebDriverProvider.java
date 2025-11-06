package com.codeurjc.backend.security;

import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebDriverProvider {

    @Value("${SELENIUM_URL}")
    private String seleniumUrl;

    public WebDriver createDriver() {
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox","--disable-dev-shm-usage", "--user-agent=Mozilla/5.0");
        
        try {
            return new RemoteWebDriver(new URL(seleniumUrl), options);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create WebDriver", e);
        }
    }
}

