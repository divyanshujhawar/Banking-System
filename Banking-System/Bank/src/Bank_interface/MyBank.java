/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bank_interface;

import  javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
/**
 *
 * @author StarGaryen
 */
public class MyBank extends Application{
    
    // screens  for main  stage
    public static String TypeID = "Employee Type Select";
    public static String LoginID = "Login";
    public static String WelcomeCashierID = "Welcome Cashier";
    public static String WelcomeManagerID = "Welcome Manager";
    
    public static  Stage mainWindow;
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainWindow =primaryStage;
        // controller for main stage
        SceneController mainContainer = new SceneController();
        mainContainer.loadScreen(MyBank.TypeID,"/Bank_interface/Layout/startFXML.fxml");
        mainContainer.loadScreen(MyBank.LoginID,"/Bank_interface/Layout/LoginLayout.fxml");
        mainContainer.loadScreen(MyBank.WelcomeCashierID,"/cashier/Layout/CashierHome.fxml");
        mainContainer.loadScreen(MyBank.WelcomeManagerID, "/manager/Layout/ManagerHome.fxml");
        mainContainer.setScreen(MyBank.TypeID);
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        mainContainer.prefWidthProperty().bind(scene.widthProperty());
        mainContainer.prefHeightProperty().bind(scene.heightProperty());
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
