package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Texts implements Runnable {
    List<WebElement> WallPosts;

    Texts(List<WebElement> WallPosts) {
        this.WallPosts = WallPosts;
    }

    public void run() {
        Map<String, Record> forjson = new HashMap<String, Record>();
        WebElement TextElement;
        String Text = "";
        Record rec = new Record();
        String id;

        for (int i=0; i < WallPosts.size(); i++) {
            id = WallPosts.get(i).findElement(By.className("_post post page_block post_likes_test_group_-1")).getAttribute("id");
            try {
                TextElement = WallPosts.get(i).findElement(By.className("wall_post_text"));
                Text = TextElement.getText();
            }
            catch (NoSuchElementException e) {
            }
        }
    }
}
