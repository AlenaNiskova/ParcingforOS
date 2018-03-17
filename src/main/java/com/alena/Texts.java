package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Texts implements Runnable {
    List<WebElement> WallPosts;

    Texts(List<WebElement> WallPosts) {
        this.WallPosts = WallPosts;
    }

    public void run() {
        WebElement TextElement;
        WebElement ShowFullText;
        List<String> Text = new ArrayList<String>(WallPosts.size());

        for (int i=0; i < WallPosts.size(); i++) {
            try {
                try {
                    ShowFullText = WallPosts.get(i).findElement(By.className("wall_post_more"));
                    ShowFullText.click();
                }
                catch (NoSuchElementException d) { }
                TextElement = WallPosts.get(i).findElement(By.className("wall_post_text"));
                Text.add(TextElement.getText());
            }
            catch (NoSuchElementException e) {
                Text.add("");
            }
        }
        for (int i = 0; i < Text.size(); i++) {
            System.out.println(Text.get(i)+"\n\n");
        }
    }
}
