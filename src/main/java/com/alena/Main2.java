package com.alena;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main2 {

    public static String jsonpath = "JSON.json";

    public static void main(String[] args) {
        int inDB;
        try {
            DataBase db = new DataBase();
            inDB = db.count+1;
            Set<Record> JSON = JSONSet(jsonpath, inDB);
            while (JSON==null) {
                try {
                    System.out.println("Заснул");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JSON = JSONSet(jsonpath, inDB);
                System.out.println("Проснулся");
            }
            for (Record rec: JSON) {
                db.addRecord(rec);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Almost the same code, that in the JSONWork.class
    //Difference only in alcorithm of searching.
    public static Set<Record> JSONSet(String path, int n) {
        Set<Record> From = null;
        String st, strec;
        Record rec;
        int count = 0;
        try {
            RandomAccessFile RAF = new RandomAccessFile(path, "rw");
            FileChannel FChan = RAF.getChannel();
            FileLock Lock = FChan.lock();
            RAF.seek(RAF.length()-4);
            st = RAF.readLine();
            Gson gson = new Gson();
            if (!st.startsWith(" ]")) {
                Lock.release();
                RAF.close();
                return From;
            }
            From = new LinkedHashSet<Record>();
            RAF.seek(12);
            st = "";
            while (((!st.startsWith("{")) || (count!=n)) && (!st.startsWith(" ]"))) {
                st = RAF.readLine();
                if (st.startsWith("{")) {
                    count++;
                }
            }
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

    //I don't need this, but let it be.
    public static int CheckJSONCount(String path) {
        int count = 0;
        try {
            File file = new File(path);
            boolean isFileExists = file.exists();
            RandomAccessFile RAFile = new RandomAccessFile(path, "rw");
            FileChannel FChannel = RAFile.getChannel();
            FileLock FLock = FChannel.lock();
            if (isFileExists) {
                String id = "";
                while (!id.startsWith(" ]")) {
                    id = RAFile.readLine();
                    if (id.contains("\"id\":")) {
                        count++;
                    }
                }
            }
            FLock.release();
            RAFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }
}
