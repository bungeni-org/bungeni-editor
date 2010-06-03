package org.bungeni.odfdocument.report;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bungeni.odfdocument.report.BungeniOdfUserReport.ReportType;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.bungeni.trackchanges.utils.RuntimeProperties;

/**
 * Facotory class to Generate User report objects.
 * Uses entries in app.ini to load available reports
 * @author Ashok Hariharan
 */
public class BungeniOdfDocumentReportFactory {

    private static org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(BungeniOdfDocumentReportFactory.class.getName());

    /**
     * Returns an array of user report objects
     * @return
     */
    public static List<BungeniOdfUserReport> getAllReports() {
        List<BungeniOdfUserReport> reportObjects = new ArrayList<BungeniOdfUserReport>(0);
        List<String> reportRefs = CommonFunctions.getAvailableReportReferences();
        for (String reportRef : reportRefs) {
            try {
                // get the report template
                List<String> reportTemplateFile = RuntimeProperties.getSectionProp(reportRef, "report.template");
                List<String> reportProcessClass = RuntimeProperties.getSectionProp(reportRef, "report.process");
                List<String> reportUIClass  = RuntimeProperties.getSectionProp(reportRef, "report.ui");
                List<String> reportProcessType = RuntimeProperties.getSectionProp(reportRef, "report.process_type");

                BungeniOdfDocumentReportTemplate rptTemplate = getTemplateFromFile(reportTemplateFile.get(0));
                IBungeniOdfDocumentReportProcess iReportProcess = getReportObjectFromClass(reportProcessClass.get(0));
                IBungeniOdfDocumentReportUI iReportUI = getReportUIFromClass(reportUIClass.get(0));
                ReportType objReportType = ReportType.parseReportType(reportProcessType.get(0));
                BungeniOdfUserReport report = new BungeniOdfUserReport(rptTemplate, iReportProcess, iReportUI, objReportType);
                reportObjects.add(report);
            } catch (Exception ex) {
                log.error("getAllReports" + ex.getMessage());
            }
        }
        return reportObjects;

    }

    /**
     * Gets a report template object from a odf report template file
     * @param templateFile
     * @return
     * @throws Exception
     */
    private static BungeniOdfDocumentReportTemplate getTemplateFromFile(String templateFile) throws Exception {
        String pathToTemplateFile = CommonFunctions.getTemplateFolder() + File.separator + templateFile;
        BungeniOdfDocumentReportTemplate rptTemplate = new BungeniOdfDocumentReportTemplate(pathToTemplateFile);
        return rptTemplate;

    }

    /**
     * Creates a report object instance from a class
     * @param reportClass
     * @return
     */
    public static IBungeniOdfDocumentReportProcess getReportObjectFromClass(String reportClass) {
        IBungeniOdfDocumentReportProcess iProcess = null;
        try {
            Class clsReport = Class.forName(reportClass);
            iProcess = (IBungeniOdfDocumentReportProcess) clsReport.newInstance();
        } catch (InstantiationException ex) {
            log.error("getReportObjectFromClass " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error("getReportObjectFromClass " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            log.error("getReportObjectFromClass " + ex.getMessage());
        }
        return iProcess;
    }

    /**
     * Creates a report object instance from a class
     * @param reportClass
     * @return
     */
    public static IBungeniOdfDocumentReportUI getReportUIFromClass(String reportClass) {
        IBungeniOdfDocumentReportUI iProcess = null;
        try {
            if (reportClass.length() > 0 ) {
            Class clsReport = Class.forName(reportClass);
            iProcess = (IBungeniOdfDocumentReportUI) clsReport.newInstance();
            }
        } catch (InstantiationException ex) {
            log.error("getReportObjectFromClass " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error("getReportObjectFromClass " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            log.error("getReportObjectFromClass " + ex.getMessage());
        }
        return iProcess;
    }

}
