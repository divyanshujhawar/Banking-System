/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bank_interface;

import Bank_interface.ControlledScreen;
import Bank_interface.SceneController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class WishController implements Initializable, ControlledScreen {
    SceneController mycController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("cashier.WishController.initialize()");
    }    

    @Override
    public void setScreenParent(SceneController screenPage) {
        mycController = screenPage;
    }

    @Override
    public void refresh() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
