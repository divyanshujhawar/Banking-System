package Customer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class Welcome implements Initializable {
    SubScene myScene;
    @FXML private Label accLabel;
    @FXML private Label nameLabel;
    @FXML private Label amountLabel;
    @FXML private Label cifNumber;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    void setScene(SubScene scene, String acc ,Connection conn,String TB_name){
        try {
            myScene = scene;
            accLabel.setText(acc);
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("select * from "+TB_name +" where "+ " accNo="+acc+" and status=1");
            rs.last();
            if(rs.getRow()==1){
                amountLabel.setText(rs.getString("amount"));
                cifNumber.setText(rs.getString("custId"));
                rs=stmt.executeQuery("select name from customer where Id="+ cifNumber.getText());
                rs.last();
                nameLabel.setText(rs.getString("name"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Welcome.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
