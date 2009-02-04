/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.xml;

import java.util.Set;
import org.openoffice.odf.pkg.OdfPackage;

/**
 *
 * @author ashok
 */
public class OdfRefactor {
    OdfPackage odfPackage = null;
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OdfPackage.class.getName());

    public OdfRefactor(String odfFile) {
        try {
            odfPackage = OdfPackage.loadPackage(odfFile);
            Set<String> setOfFileEntries = odfPackage.getFileEntries();
            System.out.println(setOfFileEntries);
        } catch (Exception ex) {
          log.error("OdfRefactor() = " + odfFile);
        }
    }



}
