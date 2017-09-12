/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import Bank_interface.ControlledScreen;
import Bank_interface.LoginController;
import Bank_interface.SceneController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.util.Date;
import java.util.Random;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class CreateAccountController implements Initializable,ControlledScreen {
    SceneController myController;
    Connection conn;
    @FXML private TextField customerId;
    @FXML private RadioButton currentButton;
    @FXML private RadioButton savingButton;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @Override
    public void setScreenParent(SceneController screenPage) {
        myController=screenPage;
    }

    @Override
    public void refresh(){
        conn= LoginController.getConnection();
        customerId.setText("");
        currentButton.setSelected(false);
        savingButton.setSelected(false);
    }
    @FXML
    private void getId(){
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/manager/Layout/newCustomer.fxml"));
        Parent loadScreen=null;
        NewCustomerController myScreenControler=null;
        try {
            loadScreen = (Parent) myLoader.load();
            myScreenControler  =  myLoader.getController();
        } catch (IOException ex) {
            System.out.println("IOException ::" + ex.getMessage());
        }
        Stage newStage = new Stage();
        Scene myScene = new Scene(loadScreen);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setScene(myScene);
        myScreenControler.setStage(newStage);
        newStage.showAndWait();
        System.err.println("waiting");
       try {
            customerId.setText(myScreenControler.getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    @FXML private void cancel(){
        myController.setScreen(ManagerHomeController.wishID);
    }
    @FXML private void submit(){
        String Password = null;
        Date date= new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(customerId.getText().length()==6 && (currentButton.isSelected() || savingButton.isSelected())){
            String acconuntNumber=null;
            Boolean result=false;
            String TB_name=null;
            if(currentButton.isSelected()) TB_name="account";
            else if(savingButton.isSelected()) TB_name = "saving_account";
            try {
                    
                    if (conn != null) {
                            System.out.println("Connected to the database");
                            Statement stmt=conn.createStatement();
                            Random rand= new Random();
                            int randomNum = rand.nextInt((9999 - 1111) + 1) + 1111;
                            Password = String.valueOf(randomNum);
                            String sql = "insert into "+TB_name+ "(password, amount,openingDate,status,custId) values("+Password+",0,'"+sdf.format(date)+ "',1,"+customerId.getText()+ ")";
                            if(stmt.executeUpdate(sql)==1){
                                result=true;
                               sql= "select accNo from " + TB_name + " where custId="+ customerId.getText() +" order by accNo desc limit 1";
                                ResultSet rs = stmt.executeQuery(sql);
                                rs.first();
                                acconuntNumber= rs.getString("accNo");
                            }

                    }
            } catch (SQLException ex) {
                    System.out.println("An error occurred. Maybe user/password is invalid");
                    ex.printStackTrace();
            }
            if(result){
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION,"Account created with account no :: " + acconuntNumber + "password::" + Password);
                successAlert.showAndWait();
                myController.setScreen(ManagerHomeController.wishID);
            }
            else{
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION,"Account not created");
                successAlert.showAndWait();
            }
                
        }
        else{
            Alert failAlert = new Alert(Alert.AlertType.ERROR,"Incorrect or incomplete information");
            failAlert.showAndWait();
        }
                
     }
    
}
