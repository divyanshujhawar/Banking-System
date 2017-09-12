/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bank_interface;

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
import javafx.scene.control.Alert.*;

/**
 * FXML Controller class for LoginLayout
 *
 * @author StarGaryen
 */
public class LoginController implements Initializable, ControlledScreen {

    SceneController myController;

    // login status values
    private static String userID = null;
    private static String name = null;
    private static boolean loginStatus = false;
    private static Connection conn;
    // user inputs
    @FXML
    TextField uID;
    @FXML
    PasswordField pass;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Bank_interface.LoginController.initialize()");
    }

    /**
     * Return the userId of current user
     *
     * @return userID
     */
    public static String getUserID() {
        return userID;
    }

    public static String getName() {
        return name;
    }
    public static Connection getConnection(){
        return conn;
    }
    /**
     *
     * @return
     */
    public static boolean getLoginStatus() {
        return loginStatus;
    }

    @FXML
    private void submitClicked() {

        if (uID.getText().length() == 6 && pass.getText().length() > 0) {
            String databaseURL = "jdbc:mysql://localhost:3306/bank";
            String user = "employee";
            String password = "employee";
            String TB_name = null;
            TB_name = "Employee";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                try {
                    conn = DriverManager.getConnection(databaseURL, user, password);
                } catch (SQLException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (conn != null) {
                    System.out.println("Connected to the database");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from " + TB_name + " where designation='" + StartController.getEmpType() + "' and Id=" + uID.getText() + " and password='" + pass.getText() + "' and leavingDate is null");
                    rs.last();
                    if (rs.getRow() == 1) {
                        loginStatus = true;
                        userID = uID.getText();
                        name = rs.getString("name");
                    }

                }
            } catch (ClassNotFoundException ex) {
                System.out.println("Could not find database driver class");
                ex.printStackTrace();
            } catch (SQLException ex) {
                System.out.println("An error occurred. Maybe user/password is invalid");
                ex.printStackTrace();
            }

            if (loginStatus) {
                if (StartController.getEmpType().equals("Manager")) {
                    myController.setScreen(MyBank.WelcomeManagerID);
                } else if (StartController.getEmpType().equals("Cashier")) {
                    myController.setScreen(MyBank.WelcomeCashierID);
                }
            } else {
                Alert fail = new Alert(AlertType.ERROR, "Wrong Username and password");
                fail.showAndWait();
            }
        } else {
            Alert fail = new Alert(AlertType.ERROR, "Incomplete Information");
            fail.showAndWait();
        }

    }

    @FXML
    private void canelClicked() {
        userID = null;
        name = null;
        loginStatus = false;
        myController.setScreen(MyBank.TypeID);

    }

    /**
     *
     * @param screenParent
     */
    @Override
    public void setScreenParent(SceneController screenParent) {
        myController = screenParent;
    }

    private void logout() {
        loginStatus = false;
        userID = null;
        name = null;
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void refresh() {
        if (loginStatus) {
            logout();
        }
        uID.setText("");
        pass.setText("");

    }

}
