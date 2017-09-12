/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.*;

/**
 *
 * @author StarGaryen
 */
public class SceneController extends StackPane{
    private  HashMap<String, FXMLLoader> screens;
    public SceneController(){
        super();
        screens = new HashMap<>();
    }
    
    public void addScreen(String name, FXMLLoader screen) {
        screens.put(name, screen);
    }
    
    public Node getScreen(String name) {
        FXMLLoader myLoader = screens.get(name);
        Parent loadScreen=null;
        ControlledScreen myScreenControler=null;
        try {
            loadScreen = (Parent) myLoader.load();
            myScreenControler = ((ControlledScreen) myLoader.getController());
            myScreenControler.setScreenParent(this);
        } catch (IOException ex) {
            System.out.println("already made");
            loadScreen= myLoader.getRoot();
            myScreenControler = ((ControlledScreen) myLoader.getController());
        }
        myScreenControler.refresh();
        return loadScreen;
    }
    
    
    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            addScreen(name, myLoader);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("File Name:: "+ resource);
            return false;
        }
    }
    
    public boolean setScreen(final String name) {
        
        if (screens.get(name) != null) {   //screen loaded
            if (!getChildren().isEmpty()) {    //if there is more than one screen
                        getChildren().remove(0);                    //remove the displayed screen
                        getChildren().add(getScreen(name));    //add the screen
                        
            }else{
                getChildren().add(getScreen(name));
                
               
            }
            return true;
        } else{
            System.out.println(name + " screen hasn't been loaded!!! \n");
            return false;
        }
    }
    
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println(name + " screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
}
