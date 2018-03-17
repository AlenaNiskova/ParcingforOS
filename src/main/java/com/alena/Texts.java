package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Texts implements Runnable {
    List<WebElement> WallPosts;

    Texts(List<WebElement> WallPosts) {
        this.WallPosts = WallPosts;
    }

    public void run() {

        List<WebElement> TextElements = WallPosts.get(0).findElements(By.className("wall_post_text"));
        List<String> Text = new ArrayList<String>();
        for (WebElement e: TextElements) {
            Text.add(e.getText());
        }
        /*for (int i = 0; i < Text.size(); i++) {
            System.out.println(Text.get(i));
            System.out.println();
        }*/
    }
}
