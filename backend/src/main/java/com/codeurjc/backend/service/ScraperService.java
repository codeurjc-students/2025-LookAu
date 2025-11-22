package com.codeurjc.backend.service;

import java.util.List;
import java.time.Duration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeurjc.backend.model.API.*;
import com.codeurjc.backend.security.WebDriverProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScraperService {

    @Autowired
    private WebDriverProvider webDriverProvider;

    public List<?> getResults(String type, String firstDate, String lastDate) throws Exception {

        WebDriver driver = webDriverProvider.createDriver();

        try{

            //create url for scraping
            String url = "https://www.loteriasyapuestas.es/servicios/buscadorSorteos?game_id="+ type + "&celebrados=false&fechaInicioInclusiva="+ lastDate + "&fechaFinInclusiva=" + firstDate;

            //create webdriver
            driver.get(url);

            //wait for <pre>
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("pre")));

            //get html form url
            String html = driver.getPageSource();

            Document doc = Jsoup.parse(html);
            Element pre = doc.selectFirst("pre");
            if (pre == null){
                return null;
            }

            String json = pre.text();
            if (json == null || json.isBlank()) {
                return List.of();
            }

            String cleanJson = json.trim();

            //if the response not found tickets
            if (cleanJson.startsWith("\"") || cleanJson.contains("No se ha encontrado")) {
                return List.of();
            }

            //if the response is not a JSON valid
            if (!cleanJson.startsWith("[")) {
                return List.of();
            }             

            //if the response is ok
            ObjectMapper objectMapper = new ObjectMapper();

            switch (type) {
                case "BONO":
                    return objectMapper.readValue(json, new TypeReference<List<BonolotoAPI>>() {
                    });
                case "EDMS":
                    return objectMapper.readValue(json, new TypeReference<List<EurodreamsAPI>>() {
                    });
                case "EMIL":
                    return objectMapper.readValue(json, new TypeReference<List<EuromillonesAPI>>() {
                    });
                case "ELGR":
                    return objectMapper.readValue(json, new TypeReference<List<GordoAPI>>() {
                    });
                case "LNAC":
                    return objectMapper.readValue(json, new TypeReference<List<LoteriaAPI>>() {
                    });
                case "LOTU":
                    return objectMapper.readValue(json, new TypeReference<List<LototurfAPI>>() {
                    });
                case "LAPR":
                    return objectMapper.readValue(json, new TypeReference<List<PrimitivaAPI>>() {
                    });
                case "LAQU":
                    return objectMapper.readValue(json, new TypeReference<List<QuinielaAPI>>() {
                    });
                case "QGOL":
                    return objectMapper.readValue(json, new TypeReference<List<QuinigolAPI>>() {
                    });
                case "QUPL":
                    return objectMapper.readValue(json, new TypeReference<List<QuintupleAPI>>() {
                    });
                default:
                    return null;
            }

        } finally {
            driver.quit(); // ✅ SIEMPRE se cierra
        }
    }
}
