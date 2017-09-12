/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cashier;

import Bank_interface.ControlledScreen;
import Bank_interface.LoginController;
import Bank_interface.SceneController;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import manager.NewCustomerController;
import sun.misc.BASE64Decoder;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class DebitController implements Initializable,ControlledScreen {
    private  SceneController myController; //screen switcher 
    
    private Connection conn;
    
    @FXML TextField accountField;
    @FXML TextField amountField;
    @FXML RadioButton currentButton;
    @FXML RadioButton savingButton;
    @FXML ImageView sigImageView;
    @FXML AnchorPane debitPane;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("cashier.DebitController.initialize()");
    }    
    @Override
    public void setScreenParent(SceneController screenPage) {
        myController= screenPage;
    }
    @Override
     public void refresh() {
         conn = LoginController.getConnection();
        accountField.setText("");
        amountField.setText("");
        accountField.setEditable(true);
        currentButton.setSelected(false);
        savingButton.setSelected(false);
        debitPane.setVisible(false);

    }
     @FXML void debit(){
         Date date= new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         float amount=0;
         int accountnumber=0;
         String TB_name=null;
         Boolean result = false;
         String message = "Amount not debited";
         int count=0;
         try {
             amount = Float.parseFloat(amountField.getText());
             accountnumber = Integer.parseInt(accountField.getText());
             if(currentButton.isSelected()) TB_name = "Account";
             else if(savingButton.isSelected()) TB_name = "Saving_Account";
         } catch (Exception e) {
             System.out.println("Incorrect information");
         }
         if (amount >0) { // 100000000 and 999999999 because account number is 9 digit nrmber
             Alert conform = new Alert(Alert.AlertType.CONFIRMATION, "Conform the transaction\n\n Credit ₹" + amount + " to account:: " + accountField.getText(), ButtonType.YES, ButtonType.NO);
             conform.showAndWait();
             if (conform.getResult() == ButtonType.YES) {
                 System.out.println("Yes");
                 try {
                     
                     if (conn != null) {
                         System.out.println("Connected to the database");
                         Statement stmt=conn.createStatement();
                         String sql = "Select * from "+TB_name+" where accNo="+ accountnumber;
                         ResultSet rs=stmt.executeQuery(sql);
                         rs.first();
                         BigDecimal myValue = rs.getBigDecimal("amount");
                         float amt = myValue.floatValue();
                         if(amt>=amount){
                             sql = "UPDATE "+TB_name +
                                     " SET amount =amount-"+amount+" WHERE accNo="+accountnumber;
                             count = stmt.executeUpdate(sql);
                             
                         }
                         if(count==1){
                             String t1="cashier:" + LoginController.getUserID();
                             String t2=null;
                            if(savingButton.isSelected()) t2 ="savAcc:" + accountField.getText();
                            else if(currentButton.isSelected())t2 ="curAcc:" + accountField.getText();
                           
                            
                            
                            sql="insert into log(fromAcc , toAcc , amount ,date, activity) values('" + t2 + "','" + t1 +"','" + amount +"','" + sdf.format(date) +"','debit')";
                            stmt.execute(sql);
                             result=true;
                             message="Amount debited";
                         }
                     }
                 } catch (SQLException ex) {
                     System.out.println("An error occurred. Maybe user/password is invalid");
                 } finally {
                     if (conn != null) {
                         try {
                             conn.close();
                         } catch (SQLException ex) {
                         }
                     }
                 }
                 
                 if (result) {
                     CashierHomeController.wiithdrawn(Float.parseFloat(amountField.getText()));
                     Alert processedAlert = new Alert(Alert.AlertType.INFORMATION, message);
                     processedAlert.showAndWait();
                 } else {
                     Alert failAlert = new Alert(Alert.AlertType.ERROR, message);
                     failAlert.showAndWait();
                 }
                 
             } else {
                 System.out.println("No");
             }
         } else {
             
             Alert failAlert = new Alert(Alert.AlertType.ERROR, "Incorrect or Incomplete information");
             failAlert.showAndWait();
         }
         refresh();
    }
     
     
     @FXML
     private  void cancel(){
         refresh();
         myController.setScreen(CashierHomeController.wishID);
     }
     
     
     
     @FXML
     private void getSignature(){
         int accountnumber=0;
         String TB_name=null;
         try {
             accountnumber = Integer.parseInt(accountField.getText());
             if(currentButton.isSelected()) TB_name = "signaturecurrent";
             else if(savingButton.isSelected()) TB_name = "signaturesaving";
         } catch (NumberFormatException e) {
             System.out.println("Incorrect information");
         }
         if (accountnumber>=100000000 && accountnumber <=999999999 && TB_name!=null) { // 100000000 and 999999999 because account number is 9 digit nrmber
             try {
                 
                 if (conn != null) {
                     System.out.println("Connected to the database");
                     Statement stmt=conn.createStatement();
                     String sql = "Select * from "+TB_name+" where accNo="+ accountnumber;
                     ResultSet rs=stmt.executeQuery(sql);
                     rs.first();
                     if(rs.getRow()==1){
                         BASE64Decoder decoder = new BASE64Decoder();
                         try {
                             ByteArrayInputStream bis = new ByteArrayInputStream(decoder.decodeBuffer(rs.getString("img_base_64")));
                             Image sinatureImage = new Image(bis);
                             sigImageView.setImage(sinatureImage);
                             bis.close();
                         } catch (IOException ex) {
                             Logger.getLogger(NewCustomerController.class.getName()).log(Level.SEVERE, null, ex);
                         }catch(SQLException e){
                             System.err.println(e.getMessage());
                         }
                         
                         debitPane.setVisible(true);
                         accountField.setEditable(false);
                     }
                     else{
                         Alert nouseAlert = new Alert(Alert.AlertType.ERROR, "No such account");
                         nouseAlert.showAndWait();
                     }
                 }
             } catch (SQLException ex) {
                 System.out.println("An error occurred. Maybe user/password is invalid");
             }
         } else {
             
             Alert failAlert = new Alert(Alert.AlertType.ERROR, "Incorrect or Incomplete information");
             failAlert.showAndWait();
         }
         
     }
}
