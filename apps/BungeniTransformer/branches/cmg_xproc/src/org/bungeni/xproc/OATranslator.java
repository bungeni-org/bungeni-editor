/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.xproc;

import com.xmlcalabash.drivers.Main;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.saxon.s9api.SaxonApiException;

/**
 *
 * @author doxarchitect
 */
public class OATranslator {

    public static void main(String[] args) {
        Main m = new Main();
        String[] inputs = new String[6];
        inputs[0] = "--debug";
        
        inputs[1] = "-i";
        inputs[2] = "source=D:/BungeniProject/ProjectCode/cmg_xproc/test/testdocs/bungeni_agendaitem.xml";
        inputs[3] = "-o";
        inputs[4] = "result=D:/BungeniProject/ProjectCode/cmg_xproc/test/testresults/test_xproc__bungeni_agendaitem.xml";
        inputs[5] = "D:/BungeniProject/ProjectCode/cmg_xproc/resources/configfiles/configs/config_bungeni_parliamentaryitem.xpl";

        // -iport=D:/BungeniProject/BungeniCalabash/resources/configfiles/config/bungeni_agendaitem.xml
        // -isource=D:/BungeniProject/BungeniCalabash/resources/configfiles/config/config_bungeni_parliamentaryitem.xpl
        // -oresult=D:/BungeniProject/BungeniCalabash/resources/configfiles/config/test_bungeni_agendaitem.xml


        //String[] inputs = new String[1];
        //inputs[0] = "D:/BungeniProject/BungeniCalabash/testfiles/pipe.xpl"; //pipe.xpl git-log-summary.xpl

        //-isource=pipe.xpl -oresult=/tmp/out.xml xpl/pipe.xpl
        //inputs[0] = "--debug";
        //inputs[1] = "-isource=D:/BungeniProject/BungeniCalabash/testfiles/pipe.xpl";
        //inputs[2] = "-oresult=D:/BungeniProject/BungeniCalabash/testfiles/out.xml";
        //inputs[3] = "D:/BungeniProject/BungeniCalabash/testfiles/pipe.xpl";


        try {
            m.run(inputs);
        } catch (SaxonApiException ex) {
            Logger.getLogger(OATranslator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OATranslator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(OATranslator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
