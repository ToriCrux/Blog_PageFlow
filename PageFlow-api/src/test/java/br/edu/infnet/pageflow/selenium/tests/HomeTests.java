package br.edu.infnet.pageflow.selenium.tests;

import br.edu.infnet.pageflow.selenium.pageobjects.HomePage;
import br.edu.infnet.pageflow.selenium.pageobjects.LoginPage;
import br.edu.infnet.pageflow.selenium.utils.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HomeTests {

    private WebDriver driver;
    private LoginPage loginPage;
    private String baseUrl = "http://localhost:3000/";

    @BeforeEach
    void setUp(){
        driver = WebDriverFactory.getDriver();
        loginPage = new LoginPage(driver);
        loginPage.navigateTo(baseUrl);
    }

    @AfterEach
    void tearDown() {
        WebDriverFactory.quitDriver();
    }

    @Test
    @DisplayName("Test successful logout")
    void testSuccessfulLogout() {
        HomePage homePage = loginPage.login("fogacatheo@bol.com.br", "fogacatheo");
        LoginPage loginPage = homePage.exitApplication();
        assertNotNull(loginPage);
        assertEquals("Entrar", loginPage.getPageTitleText(), "The h1 text should be 'Entrar'.");
    }

    @Test
    @DisplayName("Test successful create a post")
    void testSuccessfullyCreatePost() {
        HomePage homePage = loginPage.login("fogacatheo@bol.com.br", "fogacatheo");
        String title = "Post de Teste";
        String body = "Este é o corpo do post de teste.";
        String category = "Branding";

        homePage.createPost(title, body, category);

        assertEquals("Post de Teste", homePage.getPostTitle(title), "The h2 text title should be 'Post de Teste'.");
    }

}
