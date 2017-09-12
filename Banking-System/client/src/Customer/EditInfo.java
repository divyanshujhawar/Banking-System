package Customer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sun.misc.BASE64Encoder;
/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class EditInfo implements Initializable {
    private SubScene myScene;
    private Stage myStage;
    private String accountNumber;
    @FXML private TextField emailField;
    @FXML private TextField numberField;
    @FXML private Label cifLabel;
    @FXML private Label nameLabel;
    @FXML private Label addressLabel;
    @FXML private Label sigLocation;
    @FXML private ImageView sigImageView;
    private String signature;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    void setScene(Stage stage,SubScene centerScene , String acc){
        myStage=stage;
        myScene = centerScene;
        accountNumber=acc;
        
        //TODO get all info
        
    }
    @FXML private void save(){
        if(!emailField.getText().isEmpty() && !numberField.getText().isEmpty() && !signature.isEmpty()){
            //TODO send request to update
            boolean result = true;
            Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
            if(result) resultAlert.setContentText("Customer Account updated");
            else resultAlert.setContentText("Account not updated");
            resultAlert.showAndWait();
        }
        else{
             Alert resultAlert = new Alert(Alert.AlertType.ERROR,"Incomplete or incorrect information");
             resultAlert.showAndWait();
        }
    }
    @FXML private void cancel(){
        FXMLLoader mylLoader = null;
        Parent welcomeScreen = null;
        try {
            mylLoader = new FXMLLoader(getClass().getResource("/layouts/welcome.fxml"));
            welcomeScreen = (Parent)mylLoader.load();
        } catch (IOException e) {
            System.err.println(e.getMessage()+ "location 3");
        }
        myScene.setRoot(welcomeScreen);
        Welcome welcomeController = mylLoader.getController();
        //welcomeController.setScene(myScene, accountNumber);
        
    }
    @FXML private void browse(){
        FileChooser myFileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("image","*.jpg","*.png","*.jpeg");
        myFileChooser.getExtensionFilters().add(imageFilter);
        myFileChooser.setSelectedExtensionFilter(imageFilter);
        File imageFile =myFileChooser.showOpenDialog(myStage);
        if(imageFile!=null){
            Image sinatureImage = new Image(imageFile.toURI().toString());
            sigLocation.setText(imageFile.toURI().toString());
            sigImageView.setImage(sinatureImage);
            FileInputStream imageStream =null;
            byte[] imagedata = new byte[(int)imageFile.length()];
            try {
                imageStream = new FileInputStream(imageFile);
                imageStream.read(imagedata);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
            BASE64Encoder encoder = new BASE64Encoder();
            signature= encoder.encode(imagedata);            
        }
    }
}
