/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import Bank_interface.LoginController;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
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
import javafx.fxml.Initializable;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javax.imageio.ImageIO;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class NewCustomerController implements Initializable {

    Stage myStage;
    Connection conn;
    private String customerID;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField contactField;
    @FXML
    private TextArea addressArea;
    @FXML
    private Label fileLocation;
    @FXML
    private ImageView sigImageView;
    private String signature;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        customerID = "";
        conn = LoginController.getConnection();
    }

    String getId() {
        return customerID;
    }

    @FXML
    private void submit() {
        if (!nameField.getText().isEmpty() && !emailField.getText().isEmpty()
                && !contactField.getText().isEmpty() && !addressArea.getText().isEmpty() && !signature.isEmpty()) {
            boolean result = false;
            customerID = null;

            String TB_name = "customer";
            try {

                if (conn != null) {
                    System.out.println("Connected to the database");
                    Statement stmt = conn.createStatement();
                    String sql = "insert into " + TB_name + "(name,contact,email,img_base_64,address) values('" + nameField.getText() + "','" + contactField.getText() + "','"
                            + emailField.getText() + "','" + signature + "','" + addressArea.getText() + "')";
                    if (stmt.executeUpdate(sql) == 1) {
                        result = true;
                        sql = "select Id from " + TB_name + " order by Id desc limit 1";
                        ResultSet rs = stmt.executeQuery(sql);
                        rs.first();
                        customerID = rs.getString("Id");
                    }

                }
            } catch (SQLException ex) {
                System.out.println("An error occurred. Maybe user/password is invalid");
                ex.printStackTrace();
            }

            Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
            if (result) {
                resultAlert.setContentText("Customer Account created.\n\tCustomerID:: " + customerID );
            } else {
                resultAlert.setContentText("Account not created");
            }
            resultAlert.showAndWait();
            myStage.close();
        } else {
            Alert resultAlert = new Alert(Alert.AlertType.ERROR, "Incomplete or incorrect information");
            resultAlert.showAndWait();
        }
    }

    @FXML
    private void cancel() {
        customerID = "";
        myStage.close();
    }

    void setStage(Stage stag) {
        myStage = stag;
    }

    @FXML
    private void browse() {
        FileChooser myFileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("image", "*.jpg", "*.png", "*.jpeg");
        myFileChooser.getExtensionFilters().add(imageFilter);
        myFileChooser.setSelectedExtensionFilter(imageFilter);
        File imageFile = myFileChooser.showOpenDialog(myStage);
        if (imageFile != null) {
            Image sinatureImage = new Image(imageFile.toURI().toString());
            fileLocation.setText(imageFile.toURI().toString());
            sigImageView.setImage(sinatureImage);
            FileInputStream imageStream = null;
            byte[] imagedata = new byte[(int) imageFile.length()];
            try {
                imageStream = new FileInputStream(imageFile);
                imageStream.read(imagedata);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
            BASE64Encoder encoder = new BASE64Encoder();
            signature = encoder.encode(imagedata);
        }
    }
}
