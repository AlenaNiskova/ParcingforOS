package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Pics implements Runnable {
    List<WebElement> WallPosts;

    Pics(List<WebElement> WallPosts) {
        this.WallPosts = WallPosts;
    }

    public void run() {
        WebElement ImageElement;
        List<String> Image = new ArrayList<String>(WallPosts.size());

        /*for (int i=0; i < WallPosts.size(); i++) {
            try {
                ImageElement = WallPosts.get(i).findElement(By.cssSelector(".wall_post_text > a[href]"));
                Link.add(ImageElement.getAttribute("href"));
            }
            catch (NoSuchElementException e) {
                Link.add("");
            }
        }*/
    }
}
