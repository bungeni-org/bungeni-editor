package org.bungeni.odfdocument.report;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

 public   class DocUserReportsListModel extends AbstractListModel {
        List<BungeniOdfUserReport> reportTemplates = new ArrayList<BungeniOdfUserReport>(0);

         private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocUserReportsListModel.class.getName());


        public DocUserReportsListModel() {
            super();
            buildModel();
        }

        private void buildModel() {
            reportTemplates.clear();
            reportTemplates = BungeniOdfDocumentReportFactory.getAllReports();
        }

        public int getSize() {
            return reportTemplates.size();
        }

        @Override
        public Object getElementAt(int arg0) {
            return reportTemplates.get(arg0);
        }
    }
