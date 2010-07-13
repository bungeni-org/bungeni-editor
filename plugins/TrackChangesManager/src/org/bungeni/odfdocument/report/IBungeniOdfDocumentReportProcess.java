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
     * @return BungeniOdfDocumentReport
     */
    public BungeniOdfDocumentReport generateReport (BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper aDochelper);
    public BungeniOdfDocumentReport generateReport (BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper[] aDochelper);

    /**
     * This API is used if you have to do some preliminary processing before report generation say for e.g. do some pre-processing in a db before report generation.
     * It does not differentiate between report types as there is no user output generated. It is upto the implementation to engage in report separation.
     * @param aDochelper
     * @param parameterMap
     * @return
     */
    public boolean prepareProcess(BungeniOdfDocumentHelper[] aDochelper, HashMap<String,Object> parameterMap);
}
