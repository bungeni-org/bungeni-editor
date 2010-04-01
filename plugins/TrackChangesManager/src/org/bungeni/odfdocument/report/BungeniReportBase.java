
package org.bungeni.odfdocument.report;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

/**
 *
 * @author Ashok Hariharan
 */
class BungeniReportBase {

       BungeniOdfDocumentHelper m_docHelper;

      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniReportBase.class.getName());

      public BungeniReportBase(){
      
      }

      public BungeniReportBase (String pathToFile) {
        try {
            File fTemplate = new File(pathToFile);
            m_docHelper = new BungeniOdfDocumentHelper(fTemplate);
        } catch (Exception ex) {
                log.error("BungeniReportBase : " + ex.getMessage());
        }
      }


       public SimpleDateFormat getReportDateFormat(){
        String sDateFormat = m_docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniReportDateFormat");
        SimpleDateFormat sdf = new SimpleDateFormat(sDateFormat);
        return sdf;
    }

    @Override
    public String toString(){
        String reportName =  m_docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniReportName");
        if (reportName == null) {
            return "Unknown Report, BungeniReportName is not set";
        }
        if (reportName.length() == 0 ) {
            return "Unknown Report, BungeniReportName is not set";
        }
        return reportName;
    }


}
