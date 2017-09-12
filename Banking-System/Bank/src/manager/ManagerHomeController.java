/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import Bank_interface.ControlledScreen;
import Bank_interface.LoginController;
import Bank_interface.MyBank;
import Bank_interface.SceneController;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class ManagerHomeController implements Initializable, ControlledScreen {

    SceneController myController;
    SceneController ManagerController;
    Connection conn;

    @FXML
    SubScene centerScene;
    @FXML
    private Label uidLabel;
    @FXML
    private Label unameLabel;

    public static String wishID = "wish";
    public static String createID = "createCustomer";
    public static String updateID = "updateCustomer";
    public static String deleteID = "deleteAccount";
    public static String createEmpID = "createEmployee";
    public static String updateEmpID = "updateEmployee";
    public static String deleteEmpID = "deleteEmployee";
    public static String passChangeID = "change password";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("manager.ManagerHomeController.initialize()");

        Group root = null;
        try {
            ManagerController = new SceneController();
            ManagerController.loadScreen(wishID, "/Bank_interface/Layout/wish.fxml");
            ManagerController.loadScreen(createID, "/manager/Layout/createAccount.fxml");
            ManagerController.loadScreen(updateID, "/manager/Layout/editCustomerinfo.fxml");
            ManagerController.loadScreen(deleteID, "/manager/Layout/deleteAccount.fxml");
            ManagerController.loadScreen(createEmpID, "/manager/Layout/createEmployeeAccount.fxml");
            ManagerController.loadScreen(updateEmpID, "/manager/Layout/updateEmployeeinfo.fxml");
            ManagerController.loadScreen(deleteEmpID, "/manager/Layout/deleteEmployeeAccount.fxml");
            ManagerController.loadScreen(passChangeID, "/Bank_interface/Layout/changePass.fxml");
            root = new Group();
            root.getChildren().addAll(ManagerController);
        } catch (Exception e) {
            System.err.println("Error in loading fxml" + e.getMessage());
        }
        centerScene.setRoot(root);
    }

    @Override
    public void setScreenParent(SceneController screenPage) {
        myController = screenPage;
    }

    @FXML
    private void close() {
        Platform.exit();
    }

    @FXML
    private void help() {
        Alert helpAlert = new Alert(Alert.AlertType.INFORMATION, "Nothing can stop the man with the right mental \nattitude from achieving his goal\n nothing on earth can help the man with\n the wrong mental attitude.");
        helpAlert.showAndWait();
    }

    @FXML
    private void about() {
        Alert helpAlert = new Alert(Alert.AlertType.INFORMATION, "    Authors\nDevendra Samatia\nDivyanshu Jhanwar\nManan Agarwal\nHarshit Jindal");
        helpAlert.showAndWait();
    }

    @FXML
    private void logout() {
        myController.setScreen(MyBank.TypeID);
    }

    @Override
    public void refresh() {
        conn= LoginController.getConnection();
        ManagerController.setScreen(wishID);
        uidLabel.setText(LoginController.getUserID());
        unameLabel.setText(LoginController.getName());
        System.out.println("manager.ManagerHomeController.refresh()");
    }

    @FXML
    private void updateCustomerinfo() {
        ManagerController.setScreen(updateID);
        System.out.println("manager.ManagerHomeController.updateCustomerinfo()");

    }

    @FXML
    private void createCustomerAccount() {
        ManagerController.setScreen(createID);
        System.out.println("manager.ManagerHomeController.createCustomerAccount()");
    }

    @FXML
    private void deleteCustomerAccount() {
        ManagerController.setScreen(deleteID);
        System.out.println("manager.ManagerHomeController.deleteCustomerAccount()");
    }

    @FXML
    private void updateEmployeeinfo() {
        ManagerController.setScreen(updateEmpID);
        System.out.println("manager.ManagerHomeController.updateEmployeeinf0()");
    }

    @FXML
    private void createEmployeeAccount() {
        ManagerController.setScreen(createEmpID);
        System.out.println("manager.ManagerHomeController.createEmployeeAccount()");
    }

    @FXML
    private void deleteEmployeeAccount() {
        ManagerController.setScreen(deleteEmpID);
        System.out.println("manager.ManagerHomeController.deleteEmployeeAccount()");
    }

    @FXML
    private void passChange() {
        ManagerController.setScreen(passChangeID);
        System.out.println("manager.ManagerHomeController.passChange()");
    }

    @FXML
    private void updateInterest() {
        Boolean result = false;
        String message = "interest not updated";
        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("manager.ManagerHomeController.updateInterest()");
        Alert conformAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to credit interest", ButtonType.YES, ButtonType.NO);
        conformAlert.showAndWait();
        if (conformAlert.getResult() == ButtonType.YES) {

            String TB_name = null;
            TB_name = "Saving_account";
            String TB_name1 = "interestRate";
            float savingIntRate = 0;
            try {

                if (conn != null) {
                    System.out.println("Connected to the database");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from interestrate where accType='saving'");
                    rs.first();
                    savingIntRate = Float.parseFloat(rs.getString("rate"));
                    
                    if (!rs.getString("last").equals(sdf.format(date))) {
                        stmt.executeUpdate("update saving_account set interest=interest + (amount*" + savingIntRate + ")");
                        stmt.executeUpdate("update interestrate set last='" + sdf.format(date) + "' where accType='saving'");
                        result = true;
                        message = "interest updated";
                    } else {
                        message = "interest already updated";
                    }

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
    }
}
