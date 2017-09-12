package Customer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class Transfer implements Initializable {
    private SubScene myScene;
    private String myAccount;
    @FXML TextField amount;
    @FXML TextField account;
    @FXML RadioButton saving;
    @FXML RadioButton current;
    Connection conn;
    String TB_name;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    void setScene(SubScene centerScene, String text,Connection connection ,String table) {
        myScene = centerScene;
        myAccount = text;
        conn = connection;
        TB_name=table;
    }
    
    @FXML private void send(){
        Date date= new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String targetTable = null;
        String v = null;
           if(saving.isSelected() && !(TB_name.equals("saving_account") && myAccount.equals(account.getText()))){
               targetTable= "saving_account";
               v ="saving";
               
           }
           else if(current.isSelected()&& !(TB_name.equals("account") && myAccount.equals(account.getText()))){
               targetTable="account";
               v="current";
           }
       if(account.getText().length()==9 && !amount.getText().isEmpty() && targetTable!=null){
         
           Alert conform = new Alert(Alert.AlertType.CONFIRMATION, "Confirm the transaction\n\n Transfer ₹" + amount.getText() + " to " +v +" account:: " + account.getText(), ButtonType.YES, ButtonType.NO);
            conform.showAndWait();
           if (conform.getResult()==ButtonType.YES) {
               Boolean result = false;
               String message = null;
               
               Statement stmt;               
               try {
                   stmt = conn.createStatement();
                   
                   String sql = "Select * from " + TB_name + " where accNo=" + myAccount + " and status=1";
                   ResultSet rs = stmt.executeQuery(sql);
                   rs.first();
                   BigDecimal myValue = rs.getBigDecimal("amount");
                   float amt = myValue.floatValue();
                   float amountFloat = Float.parseFloat(amount.getText());
                   if (amt >= amountFloat && amountFloat > 0) {
                       sql = "UPDATE " + TB_name
                               + " SET amount =amount-" + amountFloat + " WHERE accNo=" + myAccount;
                       if (stmt.executeUpdate(sql) == 1) {
                           sql = "UPDATE " + targetTable
                                   + " SET amount = amount + " + amountFloat + " WHERE accNo=" + account.getText() + " and status=1";
                           if (stmt.executeUpdate(sql) != 1) {
                               sql = "UPDATE " + TB_name
                                       + " SET amount =amount+" + amountFloat + " WHERE accNo=" + myAccount;
                               stmt.executeUpdate(sql);
                               message = "no such account\nAmount not transferred";
                           } else {
                               String t1 = null;
                               String t2 = null;
                               if (saving.isSelected()) {
                                   t2 = "savAcc:" + account.getText();
                               } else if (current.isSelected()) {
                                   t2 = "curAcc:" + account.getText();
                               }
                               if (TB_name.equals("saving_account")) {
                                   t1 = "savAcc:" + myAccount;
                               } else if (TB_name.equals("account")) {
                                   t1 = "curAcc:" + myAccount;
                               }
                               
                               sql = "insert into log(fromAcc , toAcc , amount ,date, activity) values('" + t1 + "','" + t2 + "','" + amountFloat + "','" + sdf.format(date) + "','debit')";
                               stmt.execute(sql);
                               sql = "insert into log(fromAcc , toAcc , amount ,date, activity) values('" + t2 + "','" + t1 + "','" + amountFloat + "','" + sdf.format(date) + "','credit')";
                               stmt.execute(sql);
                               result = true;
                               message = "amount transferred";
                           }
                       }
                       
                   } else {
                       message = "not enough amount";
                   }
               } catch (SQLException ex) {
                   System.out.println("An error occurred. Maybe user/password is invalid");
                   ex.printStackTrace();
                   
               }               
               
               Alert resultAlert = new Alert(Alert.AlertType.ERROR, message);
               if (result) {
                   resultAlert.setAlertType(Alert.AlertType.INFORMATION);
               }
               resultAlert.showAndWait();
           }
       }else{
           Alert  resultAlert = new Alert(Alert.AlertType.ERROR, "incomplete or incorrect info");
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
            System.err.print(e.getMessage()+ "location ::"); System.err.println("Customer.Transfer.cancel()");
        }
        myScene.setRoot(welcomeScreen);
        Welcome welcomeController = mylLoader.getController();
       welcomeController.setScene(myScene, myAccount,conn,TB_name);
    }

    
}
