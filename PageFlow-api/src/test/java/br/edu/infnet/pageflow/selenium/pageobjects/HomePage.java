package br.edu.infnet.pageflow.selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By logoImage = By.id("blog-logo-titulo");
    private final By btnLogout = By.id("btnLogout");

    private final By postTitle = By.id("post-titulo");
    private final By postBody = By.cssSelector("textarea[id^='tiny-react']");
    private final By postCategory = By.id("post-select");
    private final By postSend = By.id("send-button");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public String getLogoAltText() {
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(logoImage));
        return logo.getAttribute("alt");
    }

    public LoginPage exitApplication() {
        WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(btnLogout));
        logout.click();

        return new LoginPage(driver);
    }

    public void createPost(String title, String body, String category) {
        WebElement titleField = wait.until(ExpectedConditions.visibilityOfElementLocated(postTitle));
        titleField.clear();
        titleField.sendKeys(title);

        WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe[id^='tiny-react']")));
        driver.switchTo().frame(iframe);

        WebElement editorBody = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));
        editorBody.clear();
        editorBody.sendKeys(body);

        driver.switchTo().defaultContent();

        WebElement categoryDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(postCategory));
        Select selectCategory = new Select(categoryDropdown);
        selectCategory.selectByVisibleText(category);

        WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(postSend));
        sendButton.click();
    }

    public String getPostTitle(String title) {
        By postTitleBy = By.xpath("//h2[normalize-space(text())='" + title + "']");
        WebElement postTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(postTitleBy));
        return postTitleElement.getText();
    }
}
