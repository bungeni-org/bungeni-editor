package org.bungeni.odfdocument.report;

import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

/**
 * This class encompasses a report process
 * It captures the logic to generate the actual report
 * @author Ashok Hariharan
 */
public abstract class BungeniOdfDocumentReportProcess implements IBungeniOdfDocumentReportProcess {

 public  abstract BungeniOdfDocumentReport generateReport (BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper aDochelper);
 public  abstract BungeniOdfDocumentReport generateReport (BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper[] aDochelper);

 public abstract boolean prepareProcess(BungeniOdfDocumentHelper[] aDochelper);



}
