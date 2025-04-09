package br.edu.infnet.pageflow.testes;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import br.edu.infnet.pageflow.paginas.CadastroPage;
import br.edu.infnet.pageflow.paginas.LoginPage;
import br.edu.infnet.pageflow.utilitarios.ScreenshotUtil;

import java.time.Duration;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CadastroLoginTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static CadastroPage cadastro;
    private static LoginPage login;

    private static String email;
    private static String senha = "Senha123@";
    private static String nome;

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();

        cadastro = new CadastroPage(driver, wait);
        login = new LoginPage(driver, wait);
    }

    @Test
    @Order(1)
    public void deveRealizarCadastroComSucesso() {
        driver.get("http://localhost:3000/Cadastro");
        ScreenshotUtil.capturar(driver, "01_cadastro_antes");

        String uuid = UUID.randomUUID().toString().substring(0, 5);
        email = "teste_" + uuid + "@gmail.com";
        nome = "Usuario" + uuid;

        try {
            cadastro.realizarCadastro(nome, email, senha);
            ScreenshotUtil.capturar(driver, "01_cadastro_depois");
            System.out.println("✅ Cadastro realizado: " + email);
            Assertions.assertTrue(true);
        } catch (Exception e) {
            ScreenshotUtil.capturar(driver, "01_cadastro_erro");
            e.printStackTrace();
            Assertions.fail("❌ Falha no cadastro: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void deveLogarComMesmoUsuario() {
        driver.get("http://localhost:3000/Login");
        ScreenshotUtil.capturar(driver, "02_login_antes");

        try {
            if (email == null || senha == null) {
                throw new IllegalArgumentException("❌ Email ou senha estão nulos no login.");
            }

            login.realizarLogin(email, senha);

            try {
                wait.until(ExpectedConditions.alertIsPresent());
                Alert alerta = driver.switchTo().alert();
                alerta.accept();
            } catch (NoAlertPresentException | TimeoutException ignored) {
            }

            wait.until(ExpectedConditions.urlContains("/Home"));
            ScreenshotUtil.capturar(driver, "02_login_depois");
            Assertions.assertTrue(true);

        } catch (Exception e) {
            ScreenshotUtil.capturar(driver, "02_login_erro");
            e.printStackTrace();
            Assertions.fail("❌ Falha no login: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void deveCriarPostagensNaHome() {
        try {
            wait.until(ExpectedConditions.urlContains("/Home"));
            ScreenshotUtil.capturar(driver, "03_criacao_antes");

            String postTag = email.split("@")[0];

            for (int i = 1; i <= 3; i++) {
                WebElement titulo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("input[placeholder='Title...']")));
                WebElement texto = driver.findElement(
                        By.cssSelector("textarea[placeholder='Write a post...']"));
                WebElement enviar = driver.findElement(By.cssSelector("button i.fa-paper-plane"));

                titulo.sendKeys("Título Teste " + i + " - " + postTag);
                texto.sendKeys("Este é o conteúdo da publicação número " + i);
                enviar.click();
                Thread.sleep(1000);
            }

            driver.navigate().refresh();

            WebElement postCriado = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(text(),'Título Teste 1 - " + postTag + "')]")));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                    postCriado
            );

            Thread.sleep(3000);
            ScreenshotUtil.capturar(driver, "03_criacao_depois");
            Assertions.assertTrue(true);

        } catch (Exception e) {
            ScreenshotUtil.capturar(driver, "03_criacao_erro");
            e.printStackTrace();
            Assertions.fail("❌ Falha ao criar ou visualizar postagens: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void deveClicarNoBotaoDeExcluirPostagem() {
        try {
            wait.until(ExpectedConditions.urlContains("/Home"));
            ScreenshotUtil.capturar(driver, "04_exclusao_antes");

            String postTag = email.split("@")[0];
            WebElement postCriado = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(text(),'Título Teste 1 - " + postTag + "')]")));
            WebElement wrapper = postCriado.findElement(
                    By.xpath("./ancestor::div[contains(@class,'shadow-md')]"));
            WebElement botaoExcluir = wrapper.findElement(
                    By.xpath(".//div[contains(@class,'cursor-pointer') and text()='✖']"));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", botaoExcluir);
            Thread.sleep(500);
            botaoExcluir.click();

            wait.until(ExpectedConditions.alertIsPresent());
            Alert alerta = driver.switchTo().alert();
            alerta.accept();

            Thread.sleep(1000);
            ScreenshotUtil.capturar(driver, "04_exclusao_depois");
            Assertions.assertTrue(true);

        } catch (Exception e) {
            ScreenshotUtil.capturar(driver, "04_exclusao_erro");
            e.printStackTrace();
            Assertions.fail("❌ Falha ao excluir o post: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void deveEditarPostagensDoUsuario() {
        try {
            wait.until(ExpectedConditions.urlContains("/Home"));
            ScreenshotUtil.capturar(driver, "05_edicao_antes");

            String postTag = email.split("@")[0];
            WebElement botaoEditar = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//div[text()='✎'])[1]")));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", botaoEditar);
            Thread.sleep(1000);
            botaoEditar.click();

            WebElement inputTitulo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//input)[1]")));
            WebElement inputTexto = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//textarea)[1]")));

            inputTitulo.clear();
            inputTitulo.sendKeys("Título Editado - " + postTag);
            inputTexto.clear();
            inputTexto.sendKeys("Conteúdo editado automaticamente para o post do usuário " + postTag);

            WebElement botaoEnviarEdicao = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("i.fa-paper-plane")));
            botaoEnviarEdicao.click();

            Thread.sleep(2000);
            ScreenshotUtil.capturar(driver, "05_edicao_depois");
            Assertions.assertTrue(true);

        } catch (Exception e) {
            ScreenshotUtil.capturar(driver, "05_edicao_erro");
            e.printStackTrace();
            Assertions.fail("❌ Erro ao editar postagens: " + e.getMessage());
        }
    }

    @AfterAll
    public static void finalizar() {
        if (driver != null) driver.quit();
    }
}
