
package alvis301;

import java.awt.Dimension;

/**
 *
 * @author SavithaSam
 */

//This is the class to be run - do not modify the code
public class Alvis30 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        HomeWindow Main = new HomeWindow();
        ColorMap cm = new ColorMap();
        ColorMap.setInstance(cm);
        Main.setVisible(true);
        Main.setTitle("AlVis 3.0");
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Main.setLocation(screenSize.width/3, screenSize.height/3);
        
    }
    
}
