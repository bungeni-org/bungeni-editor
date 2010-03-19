package org.bungeni.odfdocument.report;

import java.io.File;
import java.io.IOException;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.utils.BungeniOdfFileCopy;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniOdfDocumentReportTemplate {
    String fullPathToTemplateFile;
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniOdfDocumentReportTemplate.class.getName());

    BungeniOdfDocumentHelper m_docHelper;

    public BungeniOdfDocumentReportTemplate(String pathToFile) throws Exception {
      fullPathToTemplateFile = pathToFile;
      File fTemplate=  new File(pathToFile);
      m_docHelper = new BungeniOdfDocumentHelper (fTemplate);
    }

    public void documentFromTemplate (File fOutputFile) {
        try {
            BungeniOdfFileCopy fcp = new BungeniOdfFileCopy(this.m_docHelper.getOdfDocument().getPackage());
            fcp.copyFile(fOutputFile);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

    }

}
