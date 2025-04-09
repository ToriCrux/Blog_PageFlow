package br.edu.infnet.pageflow.paginas;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CadastroPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public CadastroPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void realizarCadastro(String nome, String email, String senha) {
        driver.findElement(By.name("name")).sendKeys(nome);
        driver.findElement(By.name("username")).sendKeys(nome);
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(senha);
        driver.findElement(By.name("confirmPassword")).sendKeys(senha);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.findElement(By.xpath("//button[text()='Sign Up']")).click();

        try {
            WebDriverWait waitAlerta = new WebDriverWait(driver, Duration.ofSeconds(5));
            waitAlerta.until(ExpectedConditions.alertIsPresent());

            Alert alert = driver.switchTo().alert();
            System.out.println("Texto do alerta: " + alert.getText());
            alert.accept();
        } catch (NoAlertPresentException e) {
            System.out.println("Nenhum alerta foi exibido.");
        }
    }
}
