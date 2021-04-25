package controllers;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ScreenDashboard implements Initializable {


    @FXML
    private JFXTabPane DashboardTab = new JFXTabPane();


    public Node StudentView(){
        try {
            AnchorPane studentTab = FXMLLoader.load(getClass().getResource("/views/TabStudent.fxml"));
            return studentTab;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Node TeacherView(){
        try {
            AnchorPane teacherTab = FXMLLoader.load(getClass().getResource("/views/TabTeacher.fxml"));
            return teacherTab;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Node UserView(){
        try {
            AnchorPane userTab = FXMLLoader.load(getClass().getResource("/views/TabUser.fxml"));
            return userTab;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public void CalendarView(){
//        try {
//            AnchorPane calendarTab = FXMLLoader.load(getClass().getResource("/views/ComponentCalendar.fxml"));
//            CalendarPane.getChildren().addAll(calendarTab);
//            CalendarPane.setTopAnchor(calendarTab,0.0);
//            CalendarPane.setBottomAnchor(calendarTab,0.0);
//            CalendarPane.setRightAnchor(calendarTab,0.0);
//            CalendarPane.setLeftAnchor(calendarTab,0.0);
////            return CalendarPane;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        return null;
//    }

//    -------------------------

    @Override
    public void initialize(final URL url, final ResourceBundle rb) {

        Tab studentTab = new Tab("Student", StudentView());
        Tab teacherTab = new Tab("Teacher", TeacherView());
        Tab userTab    = new Tab("User", UserView());
        DashboardTab.getTabs().addAll(studentTab, teacherTab, userTab);

//        -----------------
//        CalendarView();
//        -----------------


    }


}
