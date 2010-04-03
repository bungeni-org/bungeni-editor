
package org.bungeni.odfdocument.report;

import java.io.File;
import java.text.SimpleDateFormat;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

/**
 *
 * @author Ashok Hariharan
 */
class BungeniReportBase {

       BungeniOdfDocumentHelper m_docHelper;
       SimpleDateFormat sdfReportDateFormat = null;
       String reportDocumentPath = "";
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniReportBase.class.getName());

      public BungeniReportBase(){
      
      }

      public BungeniReportBase (BungeniOdfDocumentHelper reportDoc) {
          this.m_docHelper = reportDoc;
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
        if (this.sdfReportDateFormat == null) {
            String sDateFormat = m_docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniReportDateFormat");
            if (sDateFormat != null)
                this.sdfReportDateFormat = new SimpleDateFormat(sDateFormat);
        }
        return this.sdfReportDateFormat;
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
