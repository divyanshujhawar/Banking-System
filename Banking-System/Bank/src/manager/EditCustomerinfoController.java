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
public class EditCustomerinfoController implements Initializable, ControlledScreen {

    SceneController myController;
    Connection conn;
    @FXML
    private TextField cifField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField numberField;
    @FXML
    private TextArea addressArea;
    @FXML
    private AnchorPane editPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void setScreenParent(SceneController screenPage) {
        myController = screenPage;
    }

    @Override
    public void refresh() {
        conn = LoginController.getConnection();
        editPane.setVisible(false);
        cifField.setText("");
        cifField.setEditable(true);
    }

    @FXML
    private void submit() {
        if (cifField.getText().length() == 6) {
            try {

                if (conn != null) {
                    System.out.println("Connected to the database");
                    Statement stmt = conn.createStatement();
                    String sql = "select * from customer where Id=" + cifField.getText();
                    ResultSet rs = stmt.executeQuery(sql);
                    rs.last();
                    if (rs.getRow() == 1) {
                        nameField.setText(rs.getString("name"));
                        emailField.setText(rs.getString("email"));
                        numberField.setText(rs.getString("contact"));
                        addressArea.setText(rs.getString("address"));
                        editPane.setVisible(true);
                        cifField.setEditable(false);
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Incomplete information");
                        errorAlert.showAndWait();
                    }

                }
            } catch (SQLException ex) {
                System.out.println("An error occurred. Maybe user/password is invalid");
                ex.printStackTrace();
            }

        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Incomplete information");
            errorAlert.showAndWait();
        }

    }

    @FXML
    private void cancel() {
        editPane.setVisible(false);
        cifField.setEditable(true);
    }

    @FXML
    private void save() {
        Boolean result = false;
        if (!nameField.getText().isEmpty() && !emailField.getText().isEmpty() && !numberField.getText().isEmpty() && !addressArea.getText().isEmpty()) {

            String TB_name = "customer";
            try {

                if (conn != null) {
                    System.out.println("Connected to the database");
                    Statement stmt = conn.createStatement();
                    String sql = "update " + TB_name + " set name='" + nameField.getText() + "', email='"
                            + emailField.getText() + "', contact='" + numberField.getText() + "',address='" + addressArea.getText() + "' where Id=" + cifField.getText();
                    if (stmt.executeUpdate(sql) == 1) {
                        result = true;
                    }

                }
            } catch (SQLException ex) {
                System.out.println("An error occurred. Maybe user/password is invalid");
                ex.printStackTrace();
            }
            if (result) {
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION, "Changes Saved");
                errorAlert.showAndWait();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Changes not Saved");
                errorAlert.showAndWait();
            }

        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Incomplete information");
            errorAlert.showAndWait();
        }
    }
}
