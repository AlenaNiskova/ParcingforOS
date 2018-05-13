package com.alena;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
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
    private long pos;

    //Creates file, if it's not exists. Read all records' IDs and remembers them in HashMap,
    //'cause HashMap is usable to quick searching. Locks file while using it.
    //Using RandomAccessFile, 'cause FileLock works only with FileChannel.
    public JSONWork(String jsonpath) {
        this.jsonpath = jsonpath;
        File file = new File(jsonpath);
        isFileExists = file.exists();
        try {
            RandomAccessFile RAFile = new RandomAccessFile(jsonpath, "rw");
            FileChannel FChannel = RAFile.getChannel();
            FileLock FLock = FChannel.lock();
            if (!isFileExists) {
                RAFile.writeBytes("{ \"array\": [\n");
                pos = RAFile.getFilePointer();
            }
            else {
                String id = "";
                while (!id.startsWith(" ]")) {
                    id = RAFile.readLine();
                    if (id.contains("\"id\":")) {
                        IDinJson.add(id.substring(9, id.length()-2));
                    }
                }
                pos = RAFile.getFilePointer()-4;
                String st = "\n";
                byte[] inputBytes = st.getBytes();
                RAFile.seek(pos);
                RAFile.write(inputBytes);
                pos = RAFile.getFilePointer()-1;
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

    //Void to check, that record is full and ready to be written in JSON.
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

    //Void to write record in JSON. Locks file while using it.
    //Using RandomAccessFile, 'cause FileLock works only with FileChannel.
    private void write(Record rec) {
        try {
            RandomAccessFile RAF = new RandomAccessFile(jsonpath, "rw");
            FileChannel FChan = RAF.getChannel();
            FileLock lock = FChan.lock();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String StToGSON;
            if (!isFileExists){
                StToGSON = gson.toJson(rec);
                isFileExists = true;
            } else {
                StToGSON = ",\n" + gson.toJson(rec);
            }
            byte[] inputBytes = StToGSON.getBytes();
            RAF.seek(pos);
            RAF.write(inputBytes);
            pos = RAF.getFilePointer();
            lock.release();
            RAF.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Reading from the JSON-file in public HashMap to convey it into GUI.
    //Locks file while using it. Using RandomAccessFile, 'cause FileLock works
    //only with FileChannel. Don't forget to decode String, 'cause it's UTF-8!!!
    public Set<Record> FromJSONSet() {
        Set<Record> From = new LinkedHashSet<Record>();
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
                From.add(rec);
            }
            Lock.release();
            RAF.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return From;
    }
}
