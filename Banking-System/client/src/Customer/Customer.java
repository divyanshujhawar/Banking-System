package Customer;


import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author StarGaryen
 */
public class Customer extends  Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        FXMLLoader mylLoader = null;
        Parent loginScreen = null;
        try {
            mylLoader = new FXMLLoader(getClass().getResource("/layouts/login.fxml"));
            loginScreen = (Parent)mylLoader.load();
        } catch (Exception e) {
            System.err.print(e.getMessage()+ "location ::"); System.out.println("Customer.Customer.start()");
        }
        Scene loginScene = new Scene(loginScreen);
        primaryStage.setScene(loginScene);
        Login loginController =(Login) mylLoader.getController();
        loginController.setStage(primaryStage);
        primaryStage.show();
    }
    
}
