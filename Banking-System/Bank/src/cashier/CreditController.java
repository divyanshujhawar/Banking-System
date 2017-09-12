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
import javafx.scene.control.*;
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
public class CreditController implements Initializable, ControlledScreen {

    private SceneController myController;  //screen switcher
    private Connection conn = null;

    @FXML TextField accountField;
    @FXML  TextField amountField;
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
        System.out.println("cashier.CreditController.initialize()");
    }

    @Override
    public void setScreenParent(SceneController screenPage) {
        myController = screenPage;
    }

    @FXML
    public void refresh() {
        conn = LoginController.getConnection();
        accountField.setText("");
        amountField.setText("");
        accountField.setEditable(true);
        currentButton.setSelected(false);
        savingButton.setSelected(false);
        debitPane.setVisible(false);
    }

    @FXML
    void credit() {
        Date date= new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        float amount = 0;
        int accountnumber = 0;
        String TB_name = null;
        Boolean result = false;
        String message = "Amount not credited";

        try {
            amount = Float.parseFloat(amountField.getText());
            accountnumber = Integer.parseInt(accountField.getText());
            if (currentButton.isSelected()) {
                TB_name = "Account";
            } else if (savingButton.isSelected()) {
                TB_name = "Saving_Account";
            }
        } catch (Exception e) {
            System.out.println("Incorrect information");
        }

        if (amount > 0 && accountnumber >= 100000000 && accountnumber <= 999999999 && TB_name != null) { // 100000000 and 999999999 because account number is 9 digit nrmber
            Alert conform = new Alert(Alert.AlertType.CONFIRMATION, "Conform the transaction\n\n Credit ₹" + amount + " to account:: " + accountField.getText(), ButtonType.YES, ButtonType.NO);
            conform.showAndWait();
            if (conform.getResult() == ButtonType.YES) {

                try {
                    System.out.println("Connected to the database");
                    Statement stmt = conn.createStatement();
                    String sql = "UPDATE " + TB_name
                            + " SET amount = amount + " + amountField.getText() + " WHERE accNo=" + accountField.getText();
                    int count = stmt.executeUpdate(sql);
                    if (count == 1) {
                        String t1="cashier:" + LoginController.getUserID();
                        String t2=null;
                            if(savingButton.isSelected()) t2 ="savAcc:" + accountField.getText();
                            else if(currentButton.isSelected())t2 ="curAcc:" + accountField.getText();
                           
                            
                            
                            sql="insert into log(fromAcc , toAcc , amount ,date, activity) values('" + t2 + "','" + t1 +"','" + amount +"','" + sdf.format(date) +"','credit')";
                            stmt.execute(sql);
                        result = true;
                        message = "Amount credited";
                    }
                } catch (SQLException sQLException) {
                    message = sQLException.getMessage();
                }

                if (result) {
                    CashierHomeController.deposited(Float.parseFloat(amountField.getText()));
                    Alert processedAlert = new Alert(Alert.AlertType.INFORMATION, message);
                    processedAlert.showAndWait();
                } else {
                    Alert failAlert = new Alert(Alert.AlertType.ERROR, message);
                    failAlert.showAndWait();
                }

            }
        } else {

            Alert failAlert = new Alert(Alert.AlertType.ERROR, "Incorrect or Incomplete information");
            failAlert.showAndWait();
        }
        refresh();
    }

    @FXML
    private void cancel() {
        refresh();
        myController.setScreen(CashierHomeController.wishID);
    }

    @FXML
    private void getSignature() {

        int accountnumber = 0;
        String TB_name = null;
        try {
            accountnumber = Integer.parseInt(accountField.getText());
            if (currentButton.isSelected()) {
                TB_name = "signaturecurrent";
            } else if (savingButton.isSelected()) {
                TB_name = "signaturesaving";
            }
        } catch (NumberFormatException e) {
            System.out.println("Incorrect information");
        }

        if (accountnumber >= 100000000 && accountnumber <= 999999999 && TB_name != null) { // 100000000 and 999999999 because account number is 9 digit nrmber
            try {
                Statement stmt = conn.createStatement();
                String sql = "Select * from " + TB_name + " where accNo=" + accountnumber;
                ResultSet rs = stmt.executeQuery(sql);
                rs.first();
                if (rs.getRow() == 1) {
                    BASE64Decoder decoder = new BASE64Decoder();
                    try {
                        ByteArrayInputStream bis = new ByteArrayInputStream(decoder.decodeBuffer(rs.getString("img_base_64")));
                        Image sinatureImage = new Image(bis);
                        sigImageView.setImage(sinatureImage);
                        bis.close();
                    } catch (IOException ex) {
                        Logger.getLogger(NewCustomerController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    
                    debitPane.setVisible(true);
                    accountField.setEditable(false);
                } else {
                    debitPane.setVisible(false);
                    accountField.setEditable(true);
                    Alert nouseAlert = new Alert(Alert.AlertType.ERROR, "No such account");
                    nouseAlert.showAndWait();
                }
            } catch (SQLException sQLException) {
                debitPane.setVisible(false);
                accountField.setEditable(true);
                System.err.println(sQLException.getMessage());
            }
        } else {
            debitPane.setVisible(false);
            accountField.setEditable(true);
            Alert failAlert = new Alert(Alert.AlertType.ERROR, "Incorrect or Incomplete information");
            failAlert.showAndWait();
        }
    }
}
