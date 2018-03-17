package com.alena;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class Main {
    /*ПАРСИМ ВК.НОВОСТИ.
    * НАДО ОРГАНИЗОВАТЬ ВХОД В УЧЁТНУЮ ЗАПИСЬ. - сделано
    * ИНФОРМАЦИЯ ПАРСИТСЯ В 3 ПОТОКА (ТЕКСТ, КАРТИНКИ, ССЫЛКИ) - в процессе
    * И ПОМЕЩАЕТСЯ В ФАЙЛ JSON.
    * ОРГАНИЗОВАТЬ ДВА ПРОЦЕССА ДЛЯ JSON:
    * ОДИН ВЫПИСЫВАЕТ ИНФОРМАЦИЮ ИЗ ФАЙЛА В БД,
    * ДРУГОЙ - НА ЭКРАН, ДЛЯ ВЫВОДА НА ЭКРАН ИСПОЛЬЗУЮ GUI*/

    static String ProjectsPath = "C:\\Users\\Alena\\IdeaProjects\\";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", ProjectsPath+"ParcingforOS\\chromedriver.exe");

        //path to Chrome cash, so I don't need to sign in to my VK-account
        String chrome_cash_path = "--user-data-dir="+ProjectsPath+"ChromeCash";

        ChromeOptions options = new ChromeOptions();
        options.addArguments(chrome_cash_path);
        options.addArguments("disable-infobars");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.get("http://vk.com/feed");

        //Checks that the directory for saved pictures already exists and if not, creates that directory
        picsDirectory();

        //thread for parsing texts
        new Thread(new Texts(driver), "TextsParsing").start();

        //thread for parsing links
        new Thread(new Links(driver), "LinksParsing").start();

        //thread for parsing pictures
        new Thread(new Pics(driver), "PicsParsing").start();

        //driver.quit();
    }

    public static void picsDirectory() {
        File fold = new File("Pictures");
        if (!fold.exists()) {
            fold.mkdirs();
        }
    }
}
