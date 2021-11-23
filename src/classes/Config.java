package classes;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Config {
    
    public  Config(JFrame frame){
        String title = "Simulateur Guichet Automatique";
        frame.setTitle(title);
        ImageIcon logo = new ImageIcon(getClass().getResource("/icons/lg.png")); 
        frame.setIconImage(logo.getImage());
        frame.setResizable(false);
    }
}
