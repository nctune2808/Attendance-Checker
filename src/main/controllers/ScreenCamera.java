package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import models.Attendance;
import models.AttendanceDAO;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import utils.OpenCV;
import utils.UtilsOCV;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScreenCamera implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private ImageView currentFrame;

    @FXML
    private HBox boxFooter;

    @FXML
    private HBox controlPanel;

    @FXML
    private JFXToggleButton toggleControlPanel;

    @FXML
    private JFXButton btnStart;

    @FXML
    private JFXButton btnShot;

    @FXML
    private JFXButton btnInsert;

    @FXML
    private JFXSlider scaleSlider;
    public static double scales = 1.2;

    @FXML
    private JFXSlider neighbourSlider;
    public static int neighbours = 3;

    @FXML
    private JFXSlider sizeSlider;
    public static int sizes = 30;

    private boolean cameraActive = false;

    private OpenCV callCV = OpenCV.getInstance();

    private String CaptureScreen    = "/views/PopupCaptured.fxml";
    public String testPath      = System.getProperty("user.dir").concat("\\src\\resources\\") + "images/test/";
//----------------------------instance--------------------

    public static ScreenCamera instance;
    public ScreenCamera(){
        instance = this;
    }
    public static ScreenCamera getInstance() {
        if(instance == null){
            instance = new ScreenCamera();
        }
        return instance;
    }
    //---------------------------------------------------------

    @FXML
    void takeShot(ActionEvent event) throws InterruptedException {

        Thread.sleep(200);
        callCV.stopAcquisition();

        // update the button content
        this.btnStart.setDisable(false);
        this.btnStart.setText("Continue");

        if (!callCV.listRez.isEmpty()) {
            System.out.println("Camera: "+this.callCV.listRez);
            for (int i = 0; i < callCV.listRez.size(); i++) {
                Imgcodecs.imwrite(testPath + "Image_" + (i) + ".jpg", callCV.listRez.get(i));
            }
            ScreenPrimary.displayPopup(CaptureScreen, true);
        } else {
            System.out.println("Please capture again!");
        }
    }

    @FXML
    void dragImage(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
        this.btnInsert.setText("Dragging...");

    }

    @FXML
    void dropImage(DragEvent event) throws FileNotFoundException {
//        System.out.println("Dropping.............");
        currentFrame.setVisible(true);
        this.btnInsert.setText("Dropping...");
        File selectedFile = event.getDragboard().getFiles().get(0);
        currentFrame.setImage(new Image(new FileInputStream(selectedFile)));
        event.consume();
        callCV.updateImageView(currentFrame, callCV.detectImage(selectedFile));

//        System.out.println(test);
        this.btnInsert.setText("Insert Image");
        this.btnShot.setDisable(false);
        this.btnShot.setText("Save new");


    }

    @FXML
    void insertImage(ActionEvent event) {
        currentFrame.setVisible(true);
        this.btnInsert.setText("Inserting...");
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null){
            callCV.updateImageView(currentFrame, callCV.detectImage(selectedFile));
            this.btnShot.setDisable(false);
            this.btnShot.setText("Save new");
        }
        this.btnInsert.setText("Insert Image");

    }

    public ArrayList<Integer> sortValue(Iterator<Integer> iter){
        ArrayList<Integer> orderMax = new ArrayList<>();
        while (iter.hasNext()) {
            orderMax.add(iter.next());
        }
        Collections.sort(orderMax);
//        System.out.println("sortValue: " + orderMax);

        return orderMax;
    }

    public ArrayList<Integer> listMaxValue(int total, ArrayList<Integer> orderMax) {
        ArrayList<Integer> arrPos = new ArrayList<>();
        for(int i=0; i<total; i++){
            arrPos.add(orderMax.get(orderMax.size()-i-1));
        }
//        System.out.println("listMaxValue: " + arrPos);

        return arrPos;
    }

    public ArrayList<Integer> listKeyRecognise(ArrayList<Integer> arrDuplicates) {
        Set<Integer> set = new HashSet<>();
        Map<Integer, Integer> map = new HashMap<>();
        ArrayList<Integer> listKey = new ArrayList<>();


        for (Integer mostID : arrDuplicates) {
            if (!set.add(mostID)) {
                if (map.containsKey(mostID)){
                    map.put(mostID,map.get(mostID)+1);
                } else {
                    map.put(mostID,1);
                }
            }
        }


//        System.out.println("Map init: "+map);
        Iterator<Integer> iter = map.values().iterator();



        ArrayList<Integer> listValue = listMaxValue(callCV.facesArray.length, sortValue(iter));


        for (int key : map.keySet()) {
            if (listValue.contains(map.get(key)) && key!=0) {
                listKey.add(key);
            }
        }

//        System.out.println("listKey: "+listKey);

        return listKey;
    }

    @FXML
    void startCamera(ActionEvent event) {

//        currentFrame.fitWidthProperty().bind(stackPane.widthProperty());
//        currentFrame.fitHeightProperty().bind(stackPane.heightProperty());
        if (!this.cameraActive) {
            ArrayList<Integer> arrID = new ArrayList<>();
            this.btnShot.setDisable(false);
            this.btnInsert.setDisable(true);
            // start the video capture
            this.callCV.capture.open(0);

            // is the video stream available?
            if (callCV.capture.isOpened()) {
                this.cameraActive = true;
                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {
                    @Override
                    public void run() {
                        currentFrame.setVisible(true);
                        // effectively grab and process a single frame
                        Mat frame = callCV.grabFrame();
                        // convert and show the frame
                        Image imageToShow = UtilsOCV.mat2Image(frame);
                        callCV.updateImageView(currentFrame, imageToShow);

                        arrID.add(callCV.predictionID);
                        while (arrID.size()==20){

//                            System.out.println("got "+ callCV.facesArray.length + " face");
                            for(int i: listKeyRecognise(arrID)){
                                System.out.println("add studentID "+i+"'s attendance into db");

                                new AttendanceDAO().update(new Attendance("P", i));   //open if necessary
                            }
                            arrID.clear();

                        }
                    }
                };



                callCV.timer = Executors.newSingleThreadScheduledExecutor();
                callCV.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                // update the button content
                this.btnStart.setDisable(true);
                this.btnStart.setText("Opening...");
            }
            else { System.err.println("Failed to open the camera connection..."); }
        }
        else {
            turnOffCamera();
        }

    }

    public boolean isCameraActive(){    // drawer menu used
        if(!this.cameraActive){
            return true;
        } else {
            turnOffCamera();
            return false;
        }
    }

    public void turnOffCamera(){


        callCV.stopAcquisition();
        // the camera is not active at this point
        this.cameraActive = false;
        this.currentFrame.setVisible(false);
        // update again the button content
        this.btnStart.setText("Start Camera");
        this.btnInsert.setDisable(false);
        this.btnShot.setDisable(true);
        this.btnStart.setDisable(false);
        this.currentFrame.setImage(null);   // add later

    }

    @FXML
    void onScaleReleased(MouseEvent event) {
        scales = scaleSlider.getValue();
        System.out.println("Scale: " + scales);
    }

    @FXML
    void onNeighbourReleased(MouseEvent event) {
        neighbours = (int) neighbourSlider.getValue();
        System.out.println("Neighbour: " + neighbours);
    }

    @FXML
    void onSizeReleased(MouseEvent event) {
        sizes = (int) sizeSlider.getValue();
        System.out.println("Size: " + sizes);
    }

    @FXML
    void onResetButton(MouseEvent event) {
        scaleSlider.setValue(1.2);
        neighbourSlider.setValue(3);
        sizeSlider.setValue(30);

        onScaleReleased(event);
        onNeighbourReleased(event);
        onSizeReleased(event);
    }

    @FXML
    void onToggleAction(ActionEvent event) {
        if (toggleControlPanel.isSelected()){
            controlPanel.setVisible(true);
        } else {
            controlPanel.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        callCV.init();
        currentFrame.fitWidthProperty().bind(ScreenPrimary.mainStackPaneClone.widthProperty());
        currentFrame.fitHeightProperty().bind(ScreenPrimary.mainStackPaneClone.heightProperty());

    }
}
