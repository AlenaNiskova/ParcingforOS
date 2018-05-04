package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Links implements Runnable {
    List<WebElement> WallPosts;
    Pizdets pizdets;

    Links(List<WebElement> WallPosts, Pizdets pizdets) {
        this.WallPosts = WallPosts;
        this.pizdets = pizdets;
    }

    public void run() {
        WebElement WallPost;
        Record rec = new Record();
        String id;
        List<WebElement> Link;
        List<String> LinkText;

        for (int i=0; i < WallPosts.size(); i++) {
            LinkText = new ArrayList<String>();
            try {
            id = WallPosts.get(i).findElement(By.cssSelector("div > div")).getAttribute("id");
            }
            catch (NoSuchElementException e) {
                continue;
            }
            try {
                WallPost = WallPosts.get(i).findElement(By.cssSelector("div div.wall_post_text"));
                Link = WallPost.findElements(By.cssSelector("div.wall_post_text > a[href]"));
                for (int j=0; j<Link.size(); j++) {
                    LinkText.add(Link.get(j).getAttribute("href"));
                }
            }
            catch (NoSuchElementException e) {
            }
            pizdets.addLinks(id, LinkText);
        }
    }
}
