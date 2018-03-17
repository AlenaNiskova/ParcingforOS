package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class Texts implements Runnable {
    WebDriver driver;

    Texts(WebDriver driver) {
        this.driver = driver;
    }

    public void run() {
        WebElement dynamicElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("wall_post_text")));
        List<WebElement> more = driver.findElements(By.className("wall_post_more"));
        for (int i=0; i<more.size(); i++) more.get(i).click();
        List<WebElement> TextElements = driver.findElements(By.className("wall_post_text"));
        List<String> Text = new ArrayList<String>();
        for (WebElement e: TextElements) {
            Text.add(e.getText());
        }
        for (int i = 0; i < Text.size(); i++) {
            System.out.println(Text.get(i));
            System.out.println();
        }
    }
}
