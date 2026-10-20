/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2;

import info.iut.sae2.graphs.Color;
import info.iut.sae2.viewer.GraphViewer;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author rbourqui
 */
public class Main {
    
    
    /**
    No javadoc is provided, there is no need to understand/modify this part of the code 
     */
    public static void main(String[] args) throws Exception {
        // Sorry for adding it here but we haven't seen static block yet and we need to populate the list somewhere
        Color.populate();
        // Apply a look'n feel
        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        GraphViewer myWindow = new GraphViewer("Styzinski, foussatferret");
        myWindow.setVisible(true);
    }
}
