
package Bank_interface;


/**
 *
 * @author StarGaryen
 */
public interface ControlledScreen {
    
    //This method will allow the injection of the Parent ScreenPane
    public void setScreenParent(SceneController screenPage);
    public void refresh();
}
