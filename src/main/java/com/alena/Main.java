package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.*;
import java.util.List;

public class Main {
    /*ПАРСИМ ВК.НОВОСТИ.
    * НАДО ОРГАНИЗОВАТЬ ВХОД В УЧЁТНУЮ ЗАПИСЬ. - сделано
    * ИНФОРМАЦИЯ ПАРСИТСЯ В 3 ПОТОКА (ТЕКСТ, КАРТИНКИ, ССЫЛКИ). - сделано
    * И ПОМЕЩАЕТСЯ В ФАЙЛ JSON С ДОЗАПИСЬЮ. - сделано
    * ОРГАНИЗОВАТЬ ПРОВЕРКУ, ЧТОБЫ ЗАПИСИ В JSONе НЕ ПОВТОРЯЛИСЬ. - сделано
    * ОРГАНИЗОВАТЬ ДВА ПРОЦЕССА ДЛЯ JSON:
    * ОДИН ВЫПИСЫВАЕТ ИНФОРМАЦИЮ ИЗ ФАЙЛА В БД,
    * ДРУГОЙ - НА ЭКРАН, ДЛЯ ВЫВОДА НА ЭКРАН ИСПОЛЬЗУЮ GUI. - сделано*/

    static String ProjectsPath = "C:\\Users\\Alena\\IdeaProjects\\";

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.println("Введите, сколько записей вы желаете пропарсить:");
        int n = in.nextInt();
        //int n = 50;

        System.setProperty("webdriver.chrome.driver", ProjectsPath+"ParcingwithSelenium\\chromedriver.exe");

        //path to special Chrome cash, so I don't need to sign in to my VK-account
        String chrome_cash = "--user-data-dir="+ProjectsPath+"ChromeCash";

        ChromeOptions options = new ChromeOptions();
        options.addArguments(chrome_cash);
        options.addArguments("disable-infobars");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.get("http://vk.com/feed");

        //Checks that the directory for saved pictures already exists and if not, creates that directory
        picsDirectory();

        WebElement dynamicElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class^='feed_row']")));

        JavascriptExecutor js = (JavascriptExecutor) driver;

        List<WebElement> WallPosts = driver.findElements(By.cssSelector("div[class^='feed_row']"));

        while (WallPosts.size() < n) {
            js.executeScript("window.scrollBy(0, 50);");
            WallPosts = driver.findElements(By.cssSelector("div[class^='feed_row']"));
        }
        WallPosts.subList(n, WallPosts.size()).clear();

        JSONWork jsonWork = new JSONWork("JSON.json");

        //thread for parsing texts
        Thread texts = new Thread(new Texts(WallPosts, jsonWork), "TextsParsing");

        //thread for parsing links
        Thread links = new Thread(new Links(WallPosts, jsonWork), "LinksParsing");

        //thread for parsing pictures
        Thread pics = new Thread(new Pics(WallPosts, jsonWork), "PicsParsing");

        texts.start();
        links.start();
        pics.start();
        try {
            texts.join();
            links.join();
            pics.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //creates user interface
        GUI gui = new GUI(jsonWork.FromJSONMap());

        driver.quit();
    }

    public static void picsDirectory() {
        File fold = new File("Pictures");
        if (!fold.exists()) {
            fold.mkdirs();
        }
    }
}
