/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cashier;

import Bank_interface.ControlledScreen;
import Bank_interface.LoginController;
import Bank_interface.SceneController;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class EditInfoController implements Initializable, ControlledScreen {

    SceneController myController;
    Connection conn;
    @FXML
    private Label nameLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label salaryLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label designationLabel;
    @FXML
    private Label joinDateLabel;
    @FXML
    private TextField numberField;
    @FXML
    private TextField emailField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("cashier.EditInfoController.initialize()");
        designationLabel.setText("Cashier");
        numberField.setPromptText("new mobile number");
        emailField.setPromptText("new mail Id");

    }

    @Override
    public void setScreenParent(SceneController screenPage) {
        myController = screenPage;
    }

    @Override
    public void refresh() {
        conn = LoginController.getConnection();
        nameLabel.setText(LoginController.getName());
        idLabel.setText(LoginController.getUserID());
        String TB_name = null;
        TB_name = "Employee";
        try {

            if (conn != null) {
                System.out.println("Connected to the database");
                Statement stmt = conn.createStatement();
                String sql = "Select * from " + TB_name + " WHERE id=" + LoginController.getUserID();
                ResultSet rs = stmt.executeQuery(sql);
                rs.last();
                if (rs.getRow() == 1) {
                    salaryLabel.setText(rs.getString("salary"));
                    addressLabel.setText(rs.getString("address"));
                    ageLabel.setText(rs.getString("age"));
                    joinDateLabel.setText(rs.getString("joiningDate"));
                    numberField.setText(rs.getString("contact"));
                    emailField.setText(rs.getString("email"));
                } else {

                }
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid 3");
            ex.printStackTrace();
        }

    }

    @FXML
    private void cancel() {
        numberField.setText("");
        emailField.setText("");
        myController.setScreen(CashierHomeController.wishID);
    }

    @FXML
    void save() {
        Boolean result = false;
        if (!emailField.getText().equals("") && !numberField.getText().equals("")) {
            Alert conform = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to change info", ButtonType.YES, ButtonType.NO);
            conform.showAndWait();
            if (conform.getResult() == ButtonType.YES) {
                
                String TB_name = null;
                TB_name = "Employee";
                try {
                    if (conn != null) {
                        System.out.println("Connected to the database");
                        Statement stmt = conn.createStatement();
                        String sql = "UPDATE " + TB_name
                                + " SET contact =" + numberField.getText() + " , email='" + emailField.getText() + "' WHERE id=" + LoginController.getUserID();
                        if (stmt.executeUpdate(sql) == 1) {
                            result = true;
                        }

                    }
                } catch (SQLException ex) {
                    System.out.println("An error occurred. Maybe user/password is invalid");
                }

                if (result) {
                    Alert changed = new Alert(Alert.AlertType.INFORMATION, "info changed");
                    changed.showAndWait();
                } else {
                    Alert notChanged = new Alert(Alert.AlertType.WARNING, "info not changed");
                    notChanged.showAndWait();
                }
            }
        } else {
            Alert passAlert = new Alert(Alert.AlertType.ERROR, "Enter all credentials");
            passAlert.showAndWait();
        }

    }
}
