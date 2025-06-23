package br.edu.infnet.pageflow.selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By logoImage = By.id("blog-logo-titulo");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public String getLogoAltText() {
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(logoImage));
        return logo.getAttribute("alt");
    }
}
