package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Texts implements Runnable {
    List<WebElement> WallPosts;
    JSONWork jsonWork;

    Texts(List<WebElement> WallPosts, JSONWork jsonWork) {
        this.WallPosts = WallPosts;
        this.jsonWork = jsonWork;
    }

    public void run() {
        List<WebElement> TextList;
        String Text;
        String id;

        for (int i=0; i < WallPosts.size(); i++) {
            Text = "";
            id = WallPosts.get(i).findElement(By.cssSelector("div > div")).getAttribute("id");
            TextList = WallPosts.get(i).findElements(By.className("wall_post_text"));
            for (WebElement TextEl: TextList) {
                Text = TextList.get(0).getText();
            }
            jsonWork.addText(id, Text);
        }
    }
}
