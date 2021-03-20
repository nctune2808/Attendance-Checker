package main.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.utils.DBbean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static main.utils.DBbean.isIdMark;

public class TeacherDAO {
    private Connection conn;
    private PreparedStatement pstmt;

    public TeacherDAO() {
        this.conn = DBbean.getConnection();
        this.pstmt = DBbean.getPreparedStatement();
    }

    public ObservableList<Teacher> showTeacherTable(){
        ObservableList<Teacher> teacherLists = FXCollections.observableArrayList();
        try {
            pstmt = conn.prepareStatement("Select * from Teacher");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                teacherLists.add(new Teacher(
                        rs.getInt("tId"),
                        rs.getString("tName")
                ));
            }
//            System.out.println("accessed successfully");
        } catch (SQLException throwables) {
            System.out.println("cannot access teacher table");
        }
        return teacherLists;
    }

//    public void insert(Teacher teacher){
//        try {
//            pstmt = conn.prepareStatement("INSERT INTO teacher (tUsername, tPassword, tName) VALUES(?,?,?)");
//            pstmt.setString(1,teacher.getTeacherUsername());
//            pstmt.setString(2,teacher.getTeacherPassword());
//            pstmt.setString(3,teacher.getTeacherName());
//
//            //Executing the statement
//            pstmt.execute();
//            System.out.println("teacher inserted......");
//        } catch (SQLException e) {
//            System.out.println("teacher already exist......");
//
//        }
//    }


}
