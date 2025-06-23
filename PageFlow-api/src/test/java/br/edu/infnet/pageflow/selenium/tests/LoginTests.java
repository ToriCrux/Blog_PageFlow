package br.edu.infnet.pageflow.selenium.tests;

import br.edu.infnet.pageflow.selenium.pageobjects.HomePage;
import br.edu.infnet.pageflow.selenium.pageobjects.LoginPage;
import br.edu.infnet.pageflow.selenium.utils.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {

    private WebDriver driver;
    private LoginPage loginPage;
    private String baseUrl = "http://localhost:3000/";

    @BeforeEach
    void setUp() throws MalformedURLException {
        driver = WebDriverFactory.getDriver();
        loginPage = new LoginPage(driver);
        loginPage.navigateTo(baseUrl);
    }

    @AfterEach
    void tearDown() {
        WebDriverFactory.quitDriver();
    }

    @Test
    @DisplayName("Test successful login")
    void testSuccessfulLogin() {
        HomePage homePage = loginPage.login("fogacatheo@bol.com.br", "fogacatheo");
        assertNotNull(homePage);
        assertEquals("Logo", homePage.getLogoAltText(), "The logo's alt text should be 'Logo'.");
    }

    @Test
    @DisplayName("Should display an alert for invalid password")
    void testLoginWithInvalidPassword() {
        loginPage.login("fogacatheo@bol.com.br", "senha-errada");

        String expectedAlertMessage = "Erro: Invalid email or password";
        assertEquals(expectedAlertMessage, loginPage.getAlertTextAndAccept(), "Alert message for invalid password was not correct.");
    }

    @Test
    @DisplayName("Should display an alert for a non-existent user")
    void testLoginWithNonExistentUser() {
        loginPage.login("usuario.naoexiste@email.com", "qualquersenha");

        String expectedAlertMessage = "Erro: Invalid email or password";
        assertEquals(expectedAlertMessage, loginPage.getAlertTextAndAccept(), "Alert message for non-existent user was not correct.");
    }

    @Test
    @DisplayName("Should display an alert for empty password field")
    void testLoginWithEmptyPassword() {
        loginPage.enterEmail("fogacatheo@bol.com.br");
        loginPage.clickLoginButton();

        String expectedAlertMessage = "Preencha todos os campos.";
        assertEquals(expectedAlertMessage, loginPage.getAlertTextAndAccept(), "Alert message for empty password was not correct.");
    }

    @Test
    @DisplayName("Should display an alert for invalid email format")
    void testLoginWithInvalidEmailFormat() {
        loginPage.login("email-invalido", "qualquersenha");

        String expectedAlertMessage = "Erro: Invalid email or password";
        assertEquals(expectedAlertMessage, loginPage.getAlertTextAndAccept(), "Alert message for invalid email format was not correct.");
    }
}
