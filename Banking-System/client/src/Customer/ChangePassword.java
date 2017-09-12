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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class ChangePassword implements Initializable {

    SubScene myScene;
    private String accnumber;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField conformPasswordField;
    Connection conn;
    String TB_name;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void setScene(SubScene centerScene, String acc, Connection connection, String table) {
        myScene = centerScene;
        accnumber = acc;
        conn = connection;
        TB_name = table;
    }

    private void refresh() {
        oldPasswordField.setText("");
        newPasswordField.setText("");
        conformPasswordField.setText("");
    }

    @FXML
    private void change() {

        if (!newPasswordField.getText().equals("") && !oldPasswordField.getText().equals("") && !conformPasswordField.getText().equals("")) {
            if (newPasswordField.getText().equals(conformPasswordField.getText())) {
                Alert conform = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to change password", ButtonType.YES, ButtonType.NO);
                conform.showAndWait();
                if (conform.getResult() == ButtonType.YES) {
                    Boolean result = false;
                    try {
                        if (conn != null) {
                            System.out.println("Connected to the database");
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery("select password from " + TB_name + " where accNo=" + accnumber);
                            rs.first();
                            if (oldPasswordField.getText().equals(rs.getString("password"))) {
                                stmt = conn.createStatement();
                                String sql = "UPDATE " + TB_name + " " + " SET password ='" + newPasswordField.getText() + "' WHERE accNo=" +accnumber;
                                int i = stmt.executeUpdate(sql);
                                if (i == 1) {
                                    result = true;
                                }

                            }
                        }
                    } catch (SQLException ex) {
                        System.out.println("An error occurred. Maybe username/password is invalid");
                        ex.printStackTrace();

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
                conformPasswordField.setText("");
            }
        } else {
            Alert passAlert = new Alert(Alert.AlertType.ERROR, "Enter all credentials");
            passAlert.showAndWait();
        }
    }

    @FXML
    private void cancel() {
        FXMLLoader mylLoader = null;
        Parent welcomeScreen = null;
        try {
            mylLoader = new FXMLLoader(getClass().getResource("/layouts/welcome.fxml"));
            welcomeScreen = (Parent) mylLoader.load();
        } catch (IOException e) {
            System.err.print(e.getMessage() + "location ::" ); System.out.println("Customer.ChangePassword.cancel()");
        }
        myScene.setRoot(welcomeScreen);
        Welcome welcomeController = mylLoader.getController();
        welcomeController.setScene(myScene, accnumber,conn,TB_name);
    }
}
