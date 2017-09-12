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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class CreateEmployeeAccountController implements Initializable,ControlledScreen {
    SceneController myController;
    Connection conn;

    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField addressField;
    @FXML private TextField salaryField;
    @FXML private TextField emailField;
    @FXML private TextField mobileField;
    @FXML private RadioButton managerButton;
    @FXML private RadioButton cashierButton;
    @FXML private TableView myTableView;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameField.setPromptText("name of employee");
        ageField.setPromptText("age of employee");
        addressField.setPromptText("address of employee");
        salaryField.setPromptText("salary");
        emailField.setPromptText("email address");
        mobileField.setPromptText("mobile number");
        ageField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                ageField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        salaryField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                salaryField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        mobileField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                mobileField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }    

    @Override
    public void setScreenParent(SceneController screenPage) {
    myController=screenPage;
    }

    @Override
    public void refresh() {
        conn=LoginController.getConnection();
        
        nameField.setText("");
        ageField.setText("");
        addressField.setText("");    
        salaryField.setText(""); 
        emailField .setText("");
        mobileField .setText("");
        managerButton.setSelected(false);
        cashierButton.setSelected(false);
    }
    
    
    @FXML private  void cancel(){
        System.out.println("manager.CreateEmployeeAccountController.cancel()");
        myController.setScreen(ManagerHomeController.wishID);
    }
    @FXML private void submit(){
        System.out.println("manager.CreateEmployeeAccountController.submit()");
        Date date= new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!nameField.getText().equals("")      && ageField.getText().length()==2    &&
              !addressField.getText().equals("") && !salaryField.getText().equals("")  &&
              !emailField.getText().equals("")  && mobileField .getText().length()==10 &&
              (managerButton.isSelected() ||cashierButton.isSelected()) ){
            Boolean result =false;
            String empid=null;
            String Password=null;
            String TB_name="employee";
            String empType=null;
            if(managerButton.isSelected()) empType="Manager";
            else if(cashierButton.isSelected()) empType="Cashier";
                     try {
                    
                    if (conn != null) {
                            System.out.println("Connected to the database");
                            Statement stmt=conn.createStatement();
                             Random rand= new Random();
                            int randomNum = rand.nextInt((9999 - 1111) + 1) + 1111;
                            Password = String.valueOf(randomNum);
                            String sql = "insert into "+ TB_name + "(name,password,joiningDate,contact,email,salary,age,designation,address) values('" +
                                    nameField.getText()+ "','"+Password + "','"+ sdf.format(date) +  "','" + mobileField.getText()+"','" + emailField.getText()+"','" + salaryField.getText() + "','" +ageField.getText() + "','" +empType+ "','" + addressField.getText() +"')";
                            if(stmt.executeUpdate(sql)==1){
                                result=true;
                               sql= "select Id from " + TB_name + " order by Id desc limit 1";
                                ResultSet rs = stmt.executeQuery(sql);
                                rs.first();
                                empid = rs.getString("Id");
                            }

                    }
            } catch (SQLException ex) {
                    System.out.println("An error occurred. Maybe user/password is invalid");
                    ex.printStackTrace();
            } 
            if(result){
            Alert infoAlert = new Alert( Alert.AlertType.INFORMATION , "Account created\n\tEMployeeid :: " + empid +"\n\tPassword :: " + Password);
            infoAlert.showAndWait();
            myController.setScreen(ManagerHomeController.wishID);}
            else{
                Alert infoAlert = new Alert( Alert.AlertType.ERROR, "Account not created");
            infoAlert.showAndWait();
            }
            
        }
        else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,"Incomplete or incorrect info");
            errorAlert.showAndWait();
        }
        
    }
}
