
package org.bungeni.odfdocument.report;

/**
 * A user report is composed of 3 things
 *  - A report object (object that generates the actual report, contains a report template object within itself
 *  - A report process object (defines the logic for generating the report
 * @author Ashok Hariharan
 */
public class BungeniOdfUserReport {
    private BungeniOdfDocumentReportTemplate reportTemplate ;
    private IBungeniOdfDocumentReportProcess reportProcess;

    public BungeniOdfUserReport (BungeniOdfDocumentReportTemplate reportTemplate, IBungeniOdfDocumentReportProcess iProcess) {
        this.reportProcess = iProcess;
        this.reportTemplate = reportTemplate;
    }

    @Override
    public String toString(){
        return this.reportTemplate.toString();
    }

    /**
     * @return the report
     */
    public BungeniOdfDocumentReportTemplate getReportTemplate() {
        return reportTemplate;
    }

    /**
     * @param report the report to set
     */
    public void setReport(BungeniOdfDocumentReportTemplate reportTemp) {
        this.reportTemplate = reportTemp;
    }

    /**
     * @return the reportProcess
     */
    public IBungeniOdfDocumentReportProcess getReportProcess() {
        return reportProcess;
    }

    /**
     * @param reportProcess the reportProcess to set
     */
    public void setReportProcess(IBungeniOdfDocumentReportProcess reportProcess) {
        this.reportProcess = reportProcess;
    }
}
