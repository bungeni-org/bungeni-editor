package org.bungeni.odfdocument.report;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.bungeni.trackchanges.utils.RuntimeProperties;

 public   class DocReportTemplateListModel extends AbstractListModel {
        List<BungeniOdfDocumentReportTemplate> reportTemplates = new ArrayList<BungeniOdfDocumentReportTemplate>(0);

         private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocReportTemplateListModel.class.getName());


        public DocReportTemplateListModel() {
            super();
            buildModel();
        }

        private void buildModel() {

            // get section name of reports in app.ini
            List<String> reportRefs = CommonFunctions.getAvailableReportReferences();

            reportTemplates.clear();

            for (String reportRef : reportRefs) {
                try {

                    // get the report template
                    List<String> reportTemplateFile = RuntimeProperties.getSectionProp(reportRef, "report.template");
                    String       pathToTemplateFile = CommonFunctions.getTemplateFolder() + File.separator
                                                      + reportTemplateFile.get(0);
                    BungeniOdfDocumentReportTemplate rptTemplate =
                        new BungeniOdfDocumentReportTemplate(pathToTemplateFile);

                    reportTemplates.add(rptTemplate);
                } catch (Exception ex) {
                    log.error("buildModel : " + ex.getMessage(), ex);
                }
            }
        }

        public int getSize() {
            return reportTemplates.size();
        }

        @Override
        public Object getElementAt(int arg0) {
            return reportTemplates.get(arg0);
        }
    }
