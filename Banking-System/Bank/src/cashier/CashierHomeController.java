/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cashier;

import Bank_interface.ControlledScreen;
import Bank_interface.LoginController;
import Bank_interface.MyBank;
import Bank_interface.SceneController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.control.Alert.*;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class CashierHomeController implements Initializable,ControlledScreen {
    
    SceneController myController;
    
    
    // Subscene control and scenes
    SceneController cashierController;
    @FXML public  SubScene centerScene;
    public static String creditID = "Credit";
    public static String debitID = "Debit";
    public static String editID = "Edit Info";
    public static String passID = "Change Pass";
    public static String wishID = "Wish";
    
    
    
    //Status bar labels
    @FXML public Label uidLabel;
    @FXML public Label unameLabel;
    @FXML public Label depositLabel;
    @FXML public Label withdrawnLabel;
    
    //
    private static SimpleFloatProperty deposit =new SimpleFloatProperty(0);
    private static SimpleFloatProperty withdrawn = new SimpleFloatProperty(0);
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("cashier.CashierHomeController.initialize()");
        Group root = null;
        try {
            cashierController = new SceneController();
            cashierController.loadScreen(creditID, "/cashier/Layout/Credit.fxml");
            cashierController.loadScreen(debitID, "/cashier/Layout/Debit.fxml");
            cashierController.loadScreen(editID,"/cashier/Layout/editInfo.fxml");
            cashierController.loadScreen(passID,"/Bank_interface/Layout/changePass.fxml");
            cashierController.loadScreen(wishID,"/Bank_interface/Layout/wish.fxml");
            cashierController.setScreen(wishID);
            
            root = new Group();
            root.getChildren().addAll(cashierController);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        centerScene.setRoot(root);
        withdrawnLabel.textProperty().bindBidirectional(withdrawn, new NumberStringConverter());
        depositLabel.textProperty().bindBidirectional(deposit, new NumberStringConverter());
    }
    
    
    @Override
    public void setScreenParent(SceneController screenParent) {
        myController =screenParent;
    }
    @FXML
    private void close(){
        Platform.exit();
    }
    @FXML
    private  void help(){
        Alert helpAlert = new Alert(AlertType.INFORMATION, "I can't help everyone. You are on your own.");
        helpAlert.showAndWait();
    }
    @FXML
    private void about(){
        Alert helpAlert = new Alert(AlertType.INFORMATION, "    Authors\nDevendra Samatia\nDivyanshu Jhanwar\nManan Agarwal\nHarshit Jindal");
        helpAlert.showAndWait();
    }
    @FXML 
    private void logout(){
        myController.setScreen(MyBank.TypeID);
    }
    @FXML
    private void credit(){
        cashierController.setScreen(creditID);
    }
    @FXML
    private void debit(){
        cashierController.setScreen(debitID);
    }
    @FXML
    private void changePin(){
        cashierController.setScreen(passID);
    }
    @FXML private void updateInfo(){
        cashierController.setScreen(editID);
    }

    /**
     *  Refresh the non binded labels(unameLabel and uidLabel)
     */
    public void refresh(){
        unameLabel.setText(LoginController.getName());
        uidLabel.setText(LoginController.getUserID());
        deposit.set(0);
        withdrawn.set(0);
    }
    
    static void deposited(float amount){
        deposit.set(deposit.get() + amount);
    }
    static void wiithdrawn(float amount){
        withdrawn.set(withdrawn.get()+ amount);
    }
}
