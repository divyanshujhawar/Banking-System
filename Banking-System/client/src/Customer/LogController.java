/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Customer;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author StarGaryen
 */
public class LogController implements Initializable {

    private SubScene myScene;
    private String myAccount;
    private String TB_name;
    private Connection conn;
    @FXML
    TableView tableView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Customer.LogController.initialize()");
        //CONNECTION DATABASE

    }

    void setScene(SubScene centerScene, String acc, Connection connection, String table) {
        System.out.println("Customer.LogController.setScene()");
        myScene = centerScene;
        myAccount = acc;
        conn = connection;
        TB_name = table;
        ObservableList<Object> data = FXCollections.observableArrayList();
        try {
            
            //SQL FOR SELECTING ALL OF CUSTOMER
            
            String SQL = null;
            if(TB_name.equals("account")) SQL="SELECT toAcc, amount ,date,activity from log where fromAcc='curAcc:" + myAccount +"'";
            else if(TB_name.equals("saving_account")) SQL = "SELECT toAcc, amount ,date,activity from log where fromAcc='savAcc:" + myAccount + "'";
            //ResultSet
            ResultSet rs = conn.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableView.setItems(data);
        } catch (SQLException e) {
            System.out.println("Error on Building Data");
        }
    }

}
