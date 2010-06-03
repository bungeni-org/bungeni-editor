
package org.bungeni.odfdocument.report;

import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

/**
 * A user report is composed of 3 things
 *  - A report object (object that generates the actual report, contains a report template object within itself
 *  - A report process object (defines the logic for generating the report
 * @author Ashok Hariharan
 */
public class BungeniOdfUserReport {

    /**
     * @return the reportType
     */
    public ReportType getReportType() {
        return reportType;
    }

    /**
     * @param reportType the reportType to set
     */
    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    /**
     * @return the reportUI
     */
    public IBungeniOdfDocumentReportUI getReportUI() {
        return reportUI;
    }

    public boolean hasReportUI(){
        return (null != reportUI);
    }

    /**
     * @param reportUI the reportUI to set
     */
    public void setReportUI(IBungeniOdfDocumentReportUI reportUI) {
        this.reportUI = reportUI;
        if (null != this.reportUI) {
            this.reportUI.setProcessHook(reportProcess);
        }
    }
    public enum ReportType {
        MultiInputSingleReport ("MultiInputSingleReport"),
        SingleInputSingleReport ("SingleInputSingleReport");

        String strRptType;

         ReportType(String rType) {
            this.strRptType = rType;
        }

        @Override
        public String toString(){
            return this.strRptType;

        }

        public static ReportType parseReportType(String rptType) {
            if (rptType.equals(MultiInputSingleReport.toString())) {
                return ReportType.MultiInputSingleReport;
            } else if (rptType.equals(SingleInputSingleReport.toString())) {
                return ReportType.SingleInputSingleReport;
            } else 
                throw new IllegalArgumentException("Unable to parse report type");
        }


  
    };

    private BungeniOdfDocumentReportTemplate reportTemplate ;
    private IBungeniOdfDocumentReportProcess reportProcess;
    private IBungeniOdfDocumentReportUI reportUI;

    private ReportType reportType;
    
    public BungeniOdfUserReport (BungeniOdfDocumentReportTemplate reportTemplate, IBungeniOdfDocumentReportProcess iProcess, IBungeniOdfDocumentReportUI reportUI, ReportType rType) {
        setReportProcess(iProcess);
        this.reportTemplate = reportTemplate;
        setReportUI(reportUI);
        this.reportType = rType;
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

    public BungeniOdfDocumentReport runProcess(BungeniOdfDocumentHelper[] inputDocs){
        BungeniOdfDocumentReport outReport = null;
        switch (reportType) {
            case MultiInputSingleReport :
                    outReport = getReportProcess().generateReport(reportTemplate, inputDocs);
                break;
            case SingleInputSingleReport :
                    outReport = getReportProcess().generateReport(reportTemplate, inputDocs[0]);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Report Type");
        }
        return outReport;
    }

    /**
     * @param reportProcess the reportProcess to set
     */
    public void setReportProcess(IBungeniOdfDocumentReportProcess reportProcess) {
        this.reportProcess = reportProcess;
        if (null != this.reportUI) {
            this.reportUI.setProcessHook(reportProcess);
        }
    }
}
