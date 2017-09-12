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
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class UpdateEmployeeinfoController implements Initializable, ControlledScreen {

    SceneController mycController;
    Connection conn;

    @FXML
    private AnchorPane editPane;
    @FXML
    private TextField empIdField;
    @FXML
    private RadioButton ManagerSelect;
    @FXML
    private RadioButton CashierSelect;
    @FXML
    private RadioButton ManagerSave;
    @FXML
    private RadioButton CashierSave;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField salaryField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField numberField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @Override
    public void setScreenParent(SceneController screenPage) {
        mycController = screenPage;
    }

    @Override
    public void refresh() {
        conn = LoginController.getConnection();
        editPane.setVisible(false);
        empIdField.setEditable(true);
        empIdField.setText("");
        ManagerSelect.setSelected(false);
        CashierSelect.setSelected(false);
        ManagerSave.setSelected(false);
        CashierSave.setSelected(false);
        nameField.setText("");
        ageField.setText("");
        salaryField.setText("");
        addressField.setText("");
        emailField.setText("");
        numberField.setText("");
    }

    @FXML
    private void get() {
        System.out.println("manager.UpdateEmployeeinfoController.get()");
        if (empIdField.getText().length() == 6 && (ManagerSelect.isSelected() || CashierSelect.isSelected())) {

            Boolean result = false;
            String TB_name = "employee";
            String empType = null;
            if (ManagerSelect.isSelected()) {
                empType = "Manager";
                ManagerSave.setSelected(true);
            } else if (CashierSelect.isSelected()) {
                empType = "Cashier";
                CashierSave.setSelected(true);
            }
            try {

                if (conn != null) {
                    System.out.println("Connected to the database");
                    Statement stmt = conn.createStatement();
                    String sql = "select * from employee where Id=" + empIdField.getText() + " and designation='" + empType + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    rs.last();
                    if (rs.getRow() == 1) {

                        empIdField.setEditable(false);
                        editPane.setVisible(true);
                        nameField.setText(rs.getString("name"));
                        ageField.setText(rs.getString("age"));
                        salaryField.setText(rs.getString("salary"));
                        addressField.setText(rs.getString("address"));
                        emailField.setText(rs.getString("email"));
                        numberField.setText(rs.getString("contact"));

                    } else {
                        Alert failAlert = new Alert(Alert.AlertType.ERROR, "No such employee found");
                        failAlert.showAndWait();
                        editPane.setVisible(false);
                    }

                }
            } catch (SQLException ex) {
                System.out.println("An error occurred. Maybe user/password is invalid");
                ex.printStackTrace();
            }
        } else {
            Alert failAlert = new Alert(Alert.AlertType.ERROR, "Incorrect  or Incomplete information");
            failAlert.showAndWait();
        }
    }

    @FXML
    private void cancel() {
        System.out.println("manager.UpdateEmployeeinfoController.cancel()");
        editPane.setVisible(false);
        empIdField.setEditable(true);

    }

    @FXML
    private void save() {
        System.out.println("manager.UpdateEmployeeinfoController.save()");
        if (!empIdField.isEditable() && !nameField.getText().isEmpty() && !ageField.getText().isEmpty() && !addressField.getText().isEmpty()
                && !salaryField.getText().isEmpty() && !emailField.getText().isEmpty() && !numberField.getText().isEmpty()
                && (ManagerSave.isSelected() || CashierSave.isSelected())) {

            Boolean result = false;
            String message = "info not updated";
            String TB_name = "employee";
            String empType = null;
            if (ManagerSave.isSelected()) {
                empType = "Manager";
            } else if (CashierSave.isSelected()) {
                empType = "Cashier";
            }
            try {
                
                if (conn != null) {
                    System.out.println("Connected to the database");
                    Statement stmt = conn.createStatement();
                    String sql = "update employee set name='" + nameField.getText() + "' , age=" + ageField.getText() + " , salary='"
                            + salaryField.getText() + "' , email='" + emailField.getText() + "', contact=" + numberField.getText() + ", designation='" + empType
                            + "', address='" + addressField.getText() + "'  where Id=" + empIdField.getText();

                    if (stmt.executeUpdate(sql)== 1) {

                        result = true;
                        message = "info updated";
                    } else {
                        message = "info not updated";
                    }

                }
            } catch (SQLException ex) {
                System.out.println("An error occurred. Maybe user/password is invalid");
                ex.printStackTrace();
            }
            Alert reportAlert = new Alert(Alert.AlertType.INFORMATION, message);
            reportAlert.showAndWait();
            if (result) {
                mycController.setScreen(ManagerHomeController.wishID);
            } else {
                cancel();
            }
        }
    }
}
