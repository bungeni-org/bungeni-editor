/*
 * OOCrashTest.java
 *
 * Created on 2008.08.02 - 15:19:37
 *
 */

package org.bungeni.generator;

import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.uno.XComponentContext;
import com.sun.star.comp.helper.Bootstrap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.generator.Generator;

/**
 *
 * @author undesa
 */
public class Main {
    
    /** Creates a new instance of OOCrashTest */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // get the remote office component context
              java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                    try {
                        XComponentContext xContext = Bootstrap.bootstrap();
                        Generator f = new Generator(xContext);
                        f.setVisible(true);
                        f.setAlwaysOnTop(true);
                    } catch (BootstrapException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        });
            
        }
        catch (java.lang.Exception e){
            e.printStackTrace();
        }
        finally {
        //    System.exit( 0 );
        }
    }
    
}
