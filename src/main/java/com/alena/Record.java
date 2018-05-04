package com.alena;

import java.util.List;

public class Record {
    private String text;
    private List<String> links;
    private List<String> pics;

    public void setText(String text) {
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<String> getPics() {
        return pics;
    }
}
