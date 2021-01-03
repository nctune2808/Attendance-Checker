package main.utils;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.Random;

public class DBbean {

    static Connection conn;

    public static void getConnection() {

        String mysqlUrl = "jdbc:mysql://localhost:3306/schoolmana";
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            conn = DriverManager.getConnection(mysqlUrl, "root", "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Connection established......");

    }

    public static void insertStudent(int sid, String sname) throws SQLException {

            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO student (sid,sname) VALUES(?,?)");
            pstmt.setInt(1, sid);
            pstmt.setString(2, sname);
            //Executing the statement
            pstmt.execute();
            System.out.println("student inserted......");

    }

    public static void insertFace(String input, String output, String data, int set, int sid) throws SQLException {

            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO face (finput, foutput, fdata, fset, sid) VALUES(?,?,?,?,?)");

            pstmt.setString(1, input);
            pstmt.setString(2, output);
            pstmt.setString(3, data);
            pstmt.setInt(4, set);
            pstmt.setInt(5, sid);

            //Executing the statement
            pstmt.execute();
            System.out.println("face inserted......");

    }
}

