package com.alena;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JSONWork {

    private ConcurrentMap<String, Record> toJSONMap = new ConcurrentHashMap<String, Record>();
    private Set<String> IDinJson = new HashSet<String>();
    private final String jsonpath;
    private boolean isFileExists;

    public JSONWork(String jsonpath) {
        this.jsonpath = jsonpath;
        File file = new File(jsonpath);
        isFileExists = file.exists();
        try {
            RandomAccessFile RAFile = new RandomAccessFile(jsonpath, "rw");
            FileChannel FChannel = RAFile.getChannel();
            FileLock FLock = FChannel.lock();
            if (!isFileExists) {
                RAFile.writeBytes("{ \"array\": [\n     ");
            }
            else {
                String id = "";
                while (!id.startsWith(" ]")) {
                    id = RAFile.readLine();
                    if (id.contains("\"id\":")) {
                        IDinJson.add(id.substring(9, id.length()-2));
                    }
                }
            }
            FLock.release();
            RAFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addText(String id, String text) {
        Record rec;
        if (toJSONMap.containsKey(id)) { rec = toJSONMap.get(id);}
        else {
            rec = new Record(id);
        }
        rec.setText(text);
        check(rec);
    }

    public synchronized void addLinks(String id, List<String> links) {
        Record rec;
        if (toJSONMap.containsKey(id)) { rec = toJSONMap.get(id);}
        else { rec = new Record(id);}
        rec.setLinks(links);
        check(rec);
    }

    public synchronized void addPics(String id, List<String> pics) {
        Record rec;
        if (toJSONMap.containsKey(id)) { rec = toJSONMap.get(id);}
        else { rec = new Record(id);}
        rec.setPics(pics);
        check(rec);
    }

    private void check(Record rec){
        if ((rec.getText()!=null)&&(rec.getLinks()!=null)&&(rec.getPics()!=null)) {
            if (!IDinJson.contains(rec.getId())) {
                write(rec);
                IDinJson.add(rec.getId());
            }
            toJSONMap.remove(rec.getId());
        }
        else { toJSONMap.put(rec.getId(), rec);}
    }

    private void write(Record rec) {
        try {
            int count = 1;
            RandomAccessFile RAF = new RandomAccessFile(jsonpath, "rw");
            FileChannel FChan = RAF.getChannel();
            FileLock lock = FChan.lock();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String StToGSON;
            if (!isFileExists){
                StToGSON = gson.toJson(rec) + "\n ]\n}";
                isFileExists = true;
            } else {
                StToGSON = ",\n" + gson.toJson(rec) + "\n ]\n}";
            }
            byte[] inputBytes = StToGSON.getBytes();
            ByteBuffer buf = ByteBuffer.wrap(inputBytes);
            FChan.write(buf, RAF.length()-5);
            lock.release();
            RAF.close();
            count++;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Record> FromJSONMap() {
        Map<String, Record> From = new HashMap<String, Record>();
        String st, strec;
        Record rec;
        try {
            RandomAccessFile RAF = new RandomAccessFile(jsonpath, "rw");
            FileChannel FChan = RAF.getChannel();
            FileLock Lock = FChan.lock();
            RAF.seek(12);
            st = "";
            Gson gson = new Gson();
            while (!st.startsWith(" ]")) {
                strec = "";
                while (!st.startsWith("}")) {
                    strec = strec + st;
                    st = RAF.readLine();
                }
                strec = strec + st;
                st = RAF.readLine();
                if (!st.startsWith(" ]")) {
                    strec = strec.substring(0, strec.length()-1);
                }
                strec = new String(strec.getBytes("ISO-8859-1"), "UTF-8");
                rec = gson.fromJson(strec, Record.class);
                From.put(rec.getId(), rec);
            }
            Lock.release();
            RAF.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return From;
    }
}
