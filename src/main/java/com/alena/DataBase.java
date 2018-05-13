package com.alena;

import java.sql.*;

public class DataBase {
    private final String url = "jdbc:mysql://127.0.0.1:3306/os?autoReconnect=true&useSSL=false&serverTimezone=UTC";
    private final String user = "root";
    private final String pass = "FuckingSumkin!";
    public static Connection con;
    public static Statement state;
    public static ResultSet res;
    public int count;

    DataBase() throws SQLException {
        con = DriverManager.getConnection(url, user, pass);
        state = con.createStatement();
        count = CreateDB();
    }

    private int CreateDB() throws SQLException {
        String records = "CREATE TABLE IF NOT EXISTS `records` ( \n" +
                "  `Key` int(11) NOT NULL AUTO_INCREMENT, \n" +
                "  `Id` tinytext, \n" +
                "  `Text` text, \n" +
                "  PRIMARY KEY (`Key`), \n" +
                "  UNIQUE KEY `key_UNIQUE` (`Key`));";
        String links = "CREATE TABLE IF NOT EXISTS `links` (\n" +
                "  `Id` int(11),\n" +
                "  FOREIGN KEY(Id) REFERENCES `os`.`records`(`Key`),\n" +
                "  `Link` text);";
        String pics = "CREATE TABLE IF NOT EXISTS `pics` (\n" +
                "  `Id` int(11),\n" +
                "  FOREIGN KEY(Id) REFERENCES `os`.`records`(`Key`),\n" +
                "  `Pic` tinytext);";
        state.execute(records);
        state.execute(links);
        state.execute(pics);
        res = state.executeQuery("SELECT `Key` FROM records");
        res.getFetchSize();
        res.last();
        return res.getRow();
    }

    public void addRecord(Record rec) throws SQLException {
        String inRecs = "INSERT INTO records (Id, Text) \n" +
                "VALUES (' "+rec.getId()+" ',' "+rec.getText()+" ');";
        state.executeUpdate(inRecs);
        res = state.executeQuery("SELECT `Key` FROM records WHERE Id = ' "+rec.getId()+" ';");
        res.next();
        int Key = Integer.parseInt(res.getString(1));
        String inLinks = "INSERT INTO links (Id, Link) \n" +
                "VALUES ( "+Key+" ,' ";
        String inPics = "INSERT INTO pics (Id, Pic) \n" +
                "VALUES ( "+Key+" ,' ";
        for (String link: rec.getLinks()) {
            state.executeUpdate(inLinks+link+" ');");
        }
        for (String pic: rec.getPics()) {
            state.executeUpdate(inPics+pic+" ');");
        }
    }
}
