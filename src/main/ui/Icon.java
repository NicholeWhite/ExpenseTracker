package ui;

import com.apple.eawt.Application;
import javax.swing.*;

//Code for this class retrieved from:
//https://coderedirect.com/questions/200068/setting-the-default-application-icon-image-in-java-swing-on-os-x

// Class for the icon that will be displayed for apple applications
public class Icon extends JFrame {

    //Sets the dock icon image from repository image
    Icon() {
        setIconImage(new ImageIcon("data/coin.png").getImage());
        Application.getApplication().setDockIconImage(
                new ImageIcon("data/coin.png").getImage());
    }
}
