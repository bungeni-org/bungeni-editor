package org.bungeni.odfdocument.report;

import java.util.HashMap;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

/**
 *
 * @author Ashok Hariharan
 */
public interface IBungeniOdfDocumentReportProcess {
    /**
     * The report generation requires 3 inputs
     * - The report template
     * - The input document the report process is supposed to process to generate the report
     * @return
     */
    public BungeniOdfDocumentReport generateReport (BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper aDochelper);
    public BungeniOdfDocumentReport generateReport (BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper[] aDochelper);

    public boolean prepareProcess(BungeniOdfDocumentHelper[] aDochelper, HashMap<String,Object> parameterMap);
}
