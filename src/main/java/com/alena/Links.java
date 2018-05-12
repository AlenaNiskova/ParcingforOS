package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Links implements Runnable {
    List<WebElement> WallPosts;
    JSONWork jsonWork;

    Links(List<WebElement> WallPosts, JSONWork jsonWork) {
        this.WallPosts = WallPosts;
        this.jsonWork = jsonWork;
    }

    public void run() {
        String id;
        List<WebElement> Link;
        List<String> LinkText;

        for (int i=0; i < WallPosts.size(); i++) {
            LinkText = new ArrayList<String>();
            id = WallPosts.get(i).findElement(By.cssSelector("div > div")).getAttribute("id");
            Link = WallPosts.get(i).findElements(By.cssSelector("div.wall_post_text > a[href]"));
            for (WebElement link: Link) {
                LinkText.add(link.getAttribute("href"));
            }
            jsonWork.addLinks(id, LinkText);
        }
    }
}
