/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bank_interface;

import Bank_interface.ControlledScreen;
import Bank_interface.LoginController;
import Bank_interface.SceneController;
import cashier.CashierHomeController;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class ChangePassController implements Initializable, ControlledScreen {

    SceneController myController;
    private Connection conn;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("cashier.ChangePassController.initialize()");
    }

    @Override
    public void setScreenParent(SceneController screenPage) {
        myController = screenPage;
    }

    public void refresh() {
        conn = LoginController.getConnection();
        oldPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }

    @FXML
    private void change() {
        Boolean result = false;
        if (!newPasswordField.getText().equals("") && !oldPasswordField.getText().equals("") && !confirmPasswordField.getText().equals("")) {
            if (newPasswordField.getText().equals(confirmPasswordField.getText())) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to change password", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait();
                if (confirm.getResult() == ButtonType.YES) {

                    String TB_name = null;

                    TB_name = "Employee";
                    try {
                        

                        if (conn != null) {
                            System.out.println("Connected to the database");
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery("select password from " + TB_name + " where Id=" + LoginController.getUserID());
                            rs.first();
                            if (oldPasswordField.getText().equals(rs.getString("password"))) {
                                stmt = conn.createStatement();
                                String sql = "UPDATE " + TB_name + " " + " SET password ='" + newPasswordField.getText() + "' WHERE id=" + LoginController.getUserID();
                                int i = stmt.executeUpdate(sql);
                                if (i == 1) {
                                    result = true;
                                }

                            }
                        }
                    } catch (SQLException ex) {
                        System.out.println("An error occurred. Maybe username/password is invalid");

                    }

                    if (result) {
                        Alert changed = new Alert(Alert.AlertType.INFORMATION, "Password changed");
                        changed.showAndWait();
                    } else {
                        Alert notChanged = new Alert(Alert.AlertType.WARNING, "Password not changed");
                        notChanged.showAndWait();
                    }
                    refresh();
                } else {
                    refresh();
                }
            } else {
                Alert passAlert = new Alert(Alert.AlertType.ERROR, "New passwords do not match");
                passAlert.showAndWait();
                newPasswordField.setText("");
                confirmPasswordField.setText("");
            }
        } else {
            Alert passAlert = new Alert(Alert.AlertType.ERROR, "Enter all credentials");
            passAlert.showAndWait();
        }
    }

    @FXML
    private void cancel() {
        refresh();
        myController.setScreen(CashierHomeController.wishID);
    }

}
