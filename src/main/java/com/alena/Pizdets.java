package com.alena;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Pizdets {

    private ConcurrentMap<String, Record> json = new ConcurrentHashMap<String, Record>();
    private final String jsonpath;
    private boolean isFileExists;

    public Pizdets(String jsonpath) {
        this.jsonpath = jsonpath;
        File jsonfile = new File(jsonpath);
        isFileExists = jsonfile.exists();
        if (!isFileExists) {
            try {
                FileWriter fw = new FileWriter(jsonpath, true);
                fw.write("{ \"array\": [");
                fw.write("\n     ");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getJsonpath() {
        return jsonpath;
    }

    public synchronized void addText(String id, String text) {
        Record rec;
        if (json.containsKey(id)) { rec = json.get(id);}
        else { rec = new Record();}
        rec.setText(text);
        write(rec);
    }

    public synchronized void addLinks(String id, List<String> links) {
        Record rec;
        if (json.containsKey(id)) { rec = json.get(id);}
        else { rec = new Record();}
        rec.setLinks(links);
        check(rec);
    }

    private void check(Record rec){
        //List<String> empty = new ArrayList<String>();
        if (rec.getText()==null) { rec.setText("");}
        //if (rec.getPics().size()==0) { rec.setPics(empty);}
        //if (rec.getLinks().size()==0) { rec.setLinks(empty);}
        write(rec);
    }

    private void write(Record rec) {
        try {
            RandomAccessFile raf = new RandomAccessFile(jsonpath, "rw");
            FileChannel fc = raf.getChannel();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String js;
            if (!isFileExists){
                js = gson.toJson(rec) + "\n ]\n}";
                isFileExists = true;
            } else {
                js = ",\n" + gson.toJson(rec) + "\n ]\n}";
            }
            byte[] inputBytes = js.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(inputBytes);
            fc.write(buffer, raf.length()-5);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
