package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Links implements Runnable {
    List<WebElement> WallPosts;

    Links(List<WebElement> WallPosts) {
        this.WallPosts = WallPosts;
    }

    public void run() {

        /*List<WebElement> LinkElements = WallPosts.get(0).findElements(By.cssSelector(".wall_post_text > a[href]"));
        List<String> Link = new ArrayList<String>();
        for (WebElement e: LinkElements) {
            Link.add(e.getAttribute("href"));
        }*/
        /*for (int i = 0; i < Link.size(); i++) {
            System.out.println(Link.get(i));
            System.out.println();
        }*/
    }
}
