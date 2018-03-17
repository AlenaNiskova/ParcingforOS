package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    /*ПАРСИМ ВК.НОВОСТИ.
    * НАДО ОРГАНИЗОВАТЬ ВХОД В УЧЁТНУЮ ЗАПИСЬ. - сделано
    * ИНФОРМАЦИЯ ПАРСИТСЯ В 3 ПОТОКА (ТЕКСТ, КАРТИНКИ, ССЫЛКИ) - в процессе
    * И ПОМЕЩАЕТСЯ В ФАЙЛ JSON.
    * ОРГАНИЗОВАТЬ ДВА ПРОЦЕССА ДЛЯ JSON:
    * ОДИН ВЫПИСЫВАЕТ ИНФОРМАЦИЮ ИЗ ФАЙЛА В БД,
    * ДРУГОЙ - НА ЭКРАН, ДЛЯ ВЫВОДА НА ЭКРАН ИСПОЛЬЗУЮ GUI
    * ПРИ ВЫТАСКИВАНИИ ИНФОРМАЦИИ ИЗ JSON ИСПОЛЬЗОВАТЬ
    * LINQ (JOOQ) ЗАПРОСЫ ВМЕСТО ЦИКЛОВ*/

    static String ProjectsPath = "C:\\Users\\Alena\\IdeaProjects\\";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите, сколько записей вы желаете пропарсить:");
        int n = in.nextInt();

        System.setProperty("webdriver.chrome.driver", ProjectsPath+"ParcingforOS\\chromedriver.exe");

        //path to special Chrome cash, so I don't need to sign in to my VK-account
        String chrome_cash_path = "--user-data-dir="+ProjectsPath+"ChromeCash";

        ChromeOptions options = new ChromeOptions();
        options.addArguments(chrome_cash_path);
        options.addArguments("disable-infobars");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.get("http://vk.com/feed");

        //Checks that the directory for saved pictures already exists and if not, creates that directory
        picsDirectory();

        WebElement dynamicElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("wall_text")));

        List<WebElement> WallPosts = driver.findElements(By.className("wall_text"));

        //thread for parsing texts
        new Thread(new Texts(WallPosts), "TextsParsing").start();

        //thread for parsing links
        //new Thread(new Links(WallPosts), "LinksParsing").start();

        //thread for parsing pictures
        //new Thread(new Pics(WallPosts), "PicsParsing").start();

        //driver.quit();
    }

    public static void picsDirectory() {
        File fold = new File("Pictures");
        if (!fold.exists()) {
            fold.mkdirs();
        }
    }
}
