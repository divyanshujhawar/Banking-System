/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bank_interface;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class StartController implements Initializable,ControlledScreen {
    SceneController myController;
    
    private static String empType=null;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Bank_interface.StartController.initialize()");
    }
    
    @FXML
    public void cashier(){
        empType = "Cashier";
        setloginScene();
    }
    @FXML
    public void manager(){
        empType = "Manager";
        setloginScene();
    }
    private void setloginScene(){
        myController.setScreen(MyBank.LoginID);
    }

    @Override
    public void setScreenParent(SceneController screenParent) {
        myController = screenParent;
    }
    
    public static String getEmpType(){
        return empType;
    }    

    @Override
    public void refresh() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
