package Customer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class Login implements Initializable {
    private Stage myStage;
    @FXML private TextField accField;
    @FXML private PasswordField passwordField;
    @FXML private RadioButton savingButton;
    @FXML private RadioButton currentButton;
    private Connection conn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    void setStage(Stage primaryStage){
        myStage=primaryStage;
    }
    @FXML
    private void login(){
        Boolean result =false;
        if(accField.getText().length()==9 && !passwordField.getText().isEmpty() &&(savingButton.isSelected() || currentButton.isSelected())){
            String databaseURL = "jdbc:mysql://localhost:3306/bank";
            String user = "customer";
            String password = "customer";
            String TB_name=null;
            TB_name=null;
            if(savingButton.isSelected()) TB_name="saving_account";
            else if(currentButton.isSelected()) TB_name="account";
        	try {
            		Class.forName("com.mysql.jdbc.Driver");
                    try {
                        conn = DriverManager.getConnection(databaseURL, user, password);
                    } catch (SQLException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    }
            		if (conn != null) {
                		System.out.println("Connected to the database");
				Statement stmt=conn.createStatement();  
				ResultSet rs=stmt.executeQuery("select * from "+TB_name+" where "+ " accNo="+accField.getText()+" and password='"+passwordField.getText()+"' and status=1");
                                rs.last();
				if(rs.getRow()==1){
                                    result=true;
                                }
                                    
            		}
        	} catch (ClassNotFoundException ex) {
            		System.out.println("Could not find database driver class");
            		ex.printStackTrace();
        	} catch (SQLException ex) {
            		System.out.println("An error occurred. Maybe user/password is invalid");
            		ex.printStackTrace();
        	}
            if(result){
                FXMLLoader mylLoader = null;
                Parent customerScreen = null;
                try {
                    mylLoader = new FXMLLoader(getClass().getResource("/layouts/customerHome.fxml"));
                    customerScreen = (Parent)mylLoader.load();
                } catch (IOException e) {
                    System.err.print(e.getMessage()+ "location ::"); System.err.println("Customer.Login.login()");
                }
                Scene customerScene = new Scene(customerScreen);
                myStage.setScene(customerScene);
                CustomerHome myCustomer =(CustomerHome) mylLoader.getController();
                myCustomer.setStage(myStage,accField.getText(),conn,TB_name);
            }
            else{
                Alert failAlert = new Alert(Alert.AlertType.ERROR , "Incorrect account number or password");
                failAlert.showAndWait();
            }  
        }
        else{
            Alert failAlert = new Alert(Alert.AlertType.ERROR , "Incomplete information");
                failAlert.showAndWait();
        }
    }
}
