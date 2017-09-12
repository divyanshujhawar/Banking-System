/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import Bank_interface.ControlledScreen;
import Bank_interface.LoginController;
import Bank_interface.SceneController;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class DeleteAccountController implements Initializable,ControlledScreen {
    SceneController myController;
    Connection conn;
    @FXML private TextField accountField;
    @FXML private RadioButton savingButton;
    @FXML private  RadioButton currentButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        accountField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                accountField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }    

    @Override
    public void setScreenParent(SceneController screenPage) {
    myController =screenPage;
    }

    @Override
    public void refresh() {
        conn = LoginController.getConnection();
       accountField.setText("");
       savingButton.setSelected(false);
       currentButton.setSelected(false);
    }
    
    @FXML private void delete(){
        System.out.println("manager.DeleteAccountController.delete()");
        Date date= new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(accountField.getText().length()==9 && (savingButton.isSelected() || currentButton.isSelected())){
            Boolean result= false;
            String TB_name=null;
            if(currentButton.isSelected()) TB_name="account";
            else if(savingButton.isSelected()) TB_name = "saving_account";
            try {
                    
                    if (conn != null) {
                            System.out.println("Connected to the database");
                            Statement stmt=conn.createStatement();  
                            String sql = "update "+TB_name+" set status=0, closingDate='"+ sdf.format(date) +"' where accNo="+ accountField.getText();
                            if(stmt.executeUpdate(sql)==1){
                                result=true;
                            }

                    }
            } catch (SQLException ex) {
                    System.out.println("An error occurred. Maybe user/password is invalid");
                    ex.printStackTrace();
            } 
            Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
            if(result){
                resultAlert.setContentText("Account :: " + accountField.getText() + " has been deleted");
            }
            else{
                resultAlert.setContentText("Account is not deleted try again");
            }
            resultAlert.showAndWait();
            refresh();
        }
        else{
            Alert resultAlert = new Alert(Alert.AlertType.ERROR,"Incomplete or incorrect information");
            resultAlert.showAndWait();
        }
            
    }
       
    @FXML private void cancel(){
        System.out.println("manager.DeleteAccountController.cancel()");
        myController.setScreen(ManagerHomeController.wishID);
    }
}
