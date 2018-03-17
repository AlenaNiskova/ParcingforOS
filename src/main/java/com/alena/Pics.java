package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Pics implements Runnable {
    List<WebElement> WallPosts;

    Pics(List<WebElement> WallPosts) {
        this.WallPosts = WallPosts;
    }

    public void run() {

    }
}
