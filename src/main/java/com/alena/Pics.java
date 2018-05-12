package com.alena;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class Pics implements Runnable {
    List<WebElement> WallPosts;
    JSONWork jsonWork;

    Pics(List<WebElement> WallPosts, JSONWork jsonWork) {
        this.WallPosts = WallPosts;
        this.jsonWork = jsonWork;
    }

    public void run() {
        String id;
        List<WebElement> Images;
        List<String> ImagePath;

        for (int i=0; i<WallPosts.size(); i++) {
            ImagePath = new ArrayList<String>();
            id = WallPosts.get(i).findElement(By.cssSelector("div > div")).getAttribute("id");
            Images = WallPosts.get(i).findElements(By.cssSelector("div.wall_text div.page_post_sized_thumbs.clear_fix > a"));
            for (WebElement web: Images) {
                String[] ar = web.getAttribute("style").split("url");
                String url = ar[1].substring(2, ar[1].length()-3);
                String link = save(url);
                ImagePath.add(link);
            }
            jsonWork.addPics(id, ImagePath);
        }
    }

    private String save(String url) {
        String[] ar = url.split("/");
        try {
            ReadableByteChannel in = Channels.newChannel(
                    new URL(url).openStream());
            FileChannel out = new FileOutputStream(
                    "Pictures/"+ar[ar.length-1]).getChannel();
            out.transferFrom(in, 0, Long.MAX_VALUE);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "Pictures/"+ar[ar.length-1];
    }
}
