package br.edu.infnet.pageflow.utilitarios;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static void capturar(WebDriver driver, String nomeArquivo) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File srcFile = ts.getScreenshotAs(OutputType.FILE);

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String caminho = "screenshots/" + nomeArquivo + "_" + timestamp + ".png";

        File destFile = new File(caminho);
        destFile.getParentFile().mkdirs();

        try {
            Files.copy(srcFile.toPath(), destFile.toPath());
            System.out.println(" Screenshot salva em: " + caminho);
        } catch (IOException e) {
            System.out.println(" Erro ao salvar screenshot: " + e.getMessage());
        }
    }
}
