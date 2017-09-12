package Customer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class CustomerHome implements Initializable {
    private Stage myStage;
    @FXML private Label accLabel;
    @FXML private SubScene centerScene;
    Connection conn;
    String TB_name;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    void setStage(Stage primaryStage, String accountNumber,Connection connection,String table){
        myStage = primaryStage;
        accLabel.setText(accountNumber);
        conn=connection;
        TB_name = table;
        welcome();
        
    }
    @FXML private void changePassword(){
        FXMLLoader mylLoader = null;
        Parent passwordScreen = null;
        try {
            mylLoader = new FXMLLoader(getClass().getResource("/layouts/changePassword.fxml"));
            passwordScreen = (Parent)mylLoader.load();
        } catch (IOException e) {
            System.err.print(e.getMessage()+ "location ::"); System.out.println("Customer.CustomerHome.changePassword()");
        }
        centerScene.setRoot(passwordScreen);
        ChangePassword passwordController = mylLoader.getController();
        passwordController.setScene(centerScene, accLabel.getText(),conn,TB_name);
        
    }
    @FXML private void transfer(){
        FXMLLoader mylLoader = null;
        Parent transferScreen = null;
        try {
            mylLoader = new FXMLLoader(getClass().getResource("/layouts/transfer.fxml"));
            transferScreen = (Parent)mylLoader.load();
        } catch (IOException e) {
            System.err.print(e.getMessage()+ "location ::"); System.out.println("Customer.CustomerHome.transfer()");
        }
        centerScene.setRoot(transferScreen);
        Transfer transferController = mylLoader.getController();
        transferController.setScene(centerScene, accLabel.getText(),conn,TB_name);
    }
    
    @FXML private void welcome(){
        FXMLLoader mylLoader = null;
        Parent welcomeScreen = null;
        try {
            mylLoader = new FXMLLoader(getClass().getResource("/layouts/welcome.fxml"));
            welcomeScreen = (Parent)mylLoader.load();
        } catch (IOException e) {
            System.err.print(e.getMessage()+ "location ::"); System.out.println("Customer.CustomerHome.welcome()");
        }
        centerScene.setRoot(welcomeScreen);
        Welcome welcomeController = mylLoader.getController();
        welcomeController.setScene(centerScene, accLabel.getText(),conn,TB_name);
    }
    @FXML private void showLogs(){
        FXMLLoader mylLoader = null;
        Parent welcomeScreen = null;
        try {
            mylLoader = new FXMLLoader(getClass().getResource("/layouts/log.fxml"));
            welcomeScreen = (Parent)mylLoader.load();
        } catch (IOException e) {
            System.err.print(e.getMessage()+ "location ::"); System.err.println("Customer.CustomerHome.showLogs()");
        }
        try {
            centerScene.setRoot(welcomeScreen);
            LogController logController = mylLoader.getController();
            logController.setScene(centerScene, accLabel.getText(),conn,TB_name);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
     }
    @FXML private void logout(){
        FXMLLoader mylLoader = null;
        Parent loginScreen = null;
       
        try {
            mylLoader = new FXMLLoader(getClass().getResource("/layouts/login.fxml"));
            loginScreen = (Parent)mylLoader.load();
            
        } catch (IOException e) {
            System.err.print(e.getMessage()+ "location ::"); System.err.println("Customer.CustomerHome.logout()");
        }
        try {
            
             Login loginController =(Login) mylLoader.getController();
             loginController.setStage(myStage);
              Scene loginScene =new Scene(loginScreen);
             myStage.setScene(loginScene);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    @FXML private void close(){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException ex) {
                System.err.println("no connection!");
            }
        }
        System.exit(0);
        
    }
    @FXML private void about(){
        Alert About = new Alert(Alert.AlertType.INFORMATION,"    Authors\nDevendra Samatia\nDivyanshu Jhanwar\nManan Agarwal\nHarshit Jindal");
        About.showAndWait();
    }
    
    
}
