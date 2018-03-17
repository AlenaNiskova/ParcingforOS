package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Links implements Runnable {
    List<WebElement> WallPosts;

    Links(List<WebElement> WallPosts) {
        this.WallPosts = WallPosts;
    }

    public void run() {
        WebElement LinkElement;
        List<String> Link = new ArrayList<String>(WallPosts.size());

        for (int i=0; i < WallPosts.size(); i++) {
            try {
                LinkElement = WallPosts.get(i).findElement(By.cssSelector(".wall_post_text > a[href]"));
                Link.add(LinkElement.getAttribute("href"));
            }
            catch (NoSuchElementException e) {
                Link.add("");
            }
        }
        for (int i = 0; i < Link.size(); i++) {
            System.out.println(Link.get(i)+"\n\n");
        }
    }
}
