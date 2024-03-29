package controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import models.StudentDAO;
import models.TeacherDAO;
import models.User;

import java.net.URL;
import java.util.ResourceBundle;

public class ScreenProfile implements Initializable {

    @FXML
    private JFXTextField fieldName;

    private User user = PopupSignIn.userData();

    private boolean isTeacher = false;
    private boolean isStudent = false;

    @FXML
    void onUpdate(MouseEvent event) {
        if (isTeacher){
            user.getTeacher().setTeacherName(fieldName.getText());
            new TeacherDAO().update(user.getTeacher());
        } else if (isStudent) {
            user.getStudent().setStudentName(fieldName.getText());
            new StudentDAO().update(user.getStudent());
        }
    }

    public String accessedName(){
        switch (user.getRole()){
            case "Teacher" :
                isTeacher = true;
                return user.getTeacher().getTeacherName();
            case "Student" :
                isStudent = true;
                return user.getStudent().getStudentName();
            case "Admin" :
                return user.getRole();
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fieldName.setText( accessedName().toUpperCase() );    //init
    }
}
