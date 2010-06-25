package org.bungeni.reports.process;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.QueryResults;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReport;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReportProcess;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReportTemplate;
import org.bungeni.odfdocument.report.BungeniOdfReportLine;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfChangeContext;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.bungeni.odfdom.utils.BungeniOdfNodeHelper;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.odftoolkit.odfdom.doc.text.OdfTextChange;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeEnd;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeStart;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.odftoolkit.odfdom.doc.text.OdfTextSection;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Ashok Hariharan
 */
public class reportChangesByOrder  extends BungeniOdfDocumentReportProcess {

      private static org.apache.log4j.Logger log                  =
        Logger.getLogger(reportChangesByOrder.class.getName());

    private final String[] FILTER_SECTION_TYPES = {
        "body"
    };


    public boolean prepareProcess(BungeniOdfDocumentHelper[] aDochelpers, HashMap<String,Object> paramMap) {
        BungeniClientDB db = BungeniClientDB.defaultConnect();
        for (BungeniOdfDocumentHelper aDochelper : aDochelpers) {

            final BungeniOdfTrackedChangesHelper changesHelper = aDochelper.getChangesHelper();
            List<StructuredChangeType> changes = changesHelper.getAllChanges();
            List<String> queries = new ArrayList<String>(0);
            final String documentPath = aDochelper.getDocumentPath();
            Integer iOrder = 0;
            queries.add(reportChangesByOrder_Queries.CLEANUP_QUERY(CommonFunctions.getCurrentBillID(), documentPath));
            queries = addInsertQueries(aDochelper, changes, queries) ;


            db.Connect();
            db.Update(queries, true);
            db.EndConnect();

                //now we iterate through the document ... and process all the top level nodes
                //lets do the sections first

             final BungeniOdfSectionHelper asectionHelper = aDochelper.getSectionHelper();
             Integer isecWeight = 0;
             NodeList nSections = asectionHelper.getDocumentSections();
             for (int i = 0; i < nSections.getLength(); i++) {
                    Node sectionNode = nSections.item(i);
                    OdfTextSection aSection = (OdfTextSection) sectionNode;
                    String aSectionType = asectionHelper.getSectionType(aSection);
                    //if it is a different section
                    String aSectionName = aSection.getTextNameAttribute();
                    if (!aSectionType.equals(aSectionName)) {
                        if (!isInFilter(aSectionType)) {
                            //check for this section type
                            String sectionxPath = BungeniOdfNodeHelper.getXPath(sectionNode);
                            String sCheckQuery = reportChangesByOrder_Queries.UPDATE_CHANGES_FOR_SECTION_NODE(CommonFunctions.getCurrentBillID(),
                                    documentPath,
                                    sectionxPath,
                                    aSectionType + "." + aSectionName, ++isecWeight);
                            db.Connect();
                           log.debug(sCheckQuery + ";");
                            db.Update(sCheckQuery);
                            db.EndConnect();
                        }
                    }
                }

               List<String> delQueries = new ArrayList<String>(0);
               //Now we handle the use ase
                String sQueryDeleted = reportChangesByOrder_Queries.CHECK_DELETED(CommonFunctions.getCurrentBillID(), documentPath);
                //look for the change id that was deleted
                QueryResults qr = db.ConnectAndQuery(sQueryDeleted);
                if (qr.hasResults()) {
                    for (Vector<String> aResult : qr.theResults()) {
                        String docName = qr.getField(aResult, "DOC_NAME");
                        String changeId = qr.getField(aResult, "CHANGE_ID");
                        String changeType = qr.getField(aResult,"CHANGE_TYPE");
                        if (changeType.equals("deletion")){
                            log.debug("Processing deletion node ");
                            OdfTextChangedRegion aRegion = changesHelper.getChangedRegionById(changeId);
                            StructuredChangeType scType = changesHelper.getStructuredChangeType(aRegion);
                            NodeList nodesList = changesHelper.getDeletedNodesExt(scType.elementChange.get(0), "//text:section");
                           log.debug("Found nodes : " + nodesList.getLength());
                            for (int i = 0; i < nodesList.getLength(); i++) {
                                // this is the first section ... we only want the first section since its one change
                                OdfTextSection aSection = (OdfTextSection) nodesList.item(i);
                                String sectionName = aSection.getTextNameAttribute();
                                String sectionType = asectionHelper.getSectionType(aSection);
                                //here we have to update the changes order table with the deletion info
                                String strUpdQuery  =
                                        reportChangesByOrder_Queries.UPDATE_CHANGES_FOR_DELETED_NODE(CommonFunctions.getCurrentBillID(),
                                        documentPath,
                                        scType.changeId,
                                        sectionType + "." + sectionName,
                                        0);
                                delQueries.add(strUpdQuery);
                                break;
                            }
                        }
                    }
                }
                for (String dstring : delQueries) {
                    System.out.println(dstring + " ;");
                }
                db.Connect();
                db.Update(delQueries, true);
                db.EndConnect();
        }
        return true;
    }



    @Override
    public BungeniOdfDocumentReport generateReport(BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper aDochelper) {
        /**
         * We iterate through the change identifiers for each document.
         * for each change identifier we capture the start and end node position in the db
         * 
         */
       final BungeniOdfTrackedChangesHelper changesHelper = aDochelper.getChangesHelper();
       List<StructuredChangeType> changes = changesHelper.getAllChanges();
       List<String> queries = new ArrayList<String>(0);
       final String documentPath = aDochelper.getDocumentPath();
       Integer iOrder = 0;
       queries.add(reportChangesByOrder_Queries.CLEANUP_QUERY(CommonFunctions.getCurrentBillID(), documentPath));
       queries = addInsertQueries(aDochelper, changes, queries) ;

       /*
       for (StructuredChangeType structuredChangeType : changes) {
            String xpathStart = "";
            String xpathEnd = "";

            if (structuredChangeType.changetype.equals(BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_DELETION__)) {
               OdfTextChange textChange = changesHelper.getChangeItem(structuredChangeType.changeId);
               xpathStart  = BungeniOdfNodeHelper.getXPath(textChange);
            } else {

               OdfTextChangeStart changeNodeStart = changesHelper.getChangeStartItem(structuredChangeType.changeId);
               OdfTextChangeEnd changeNodeEnd = changesHelper.getChangeEndItem(structuredChangeType.changeId);
               xpathStart = BungeniOdfNodeHelper.getXPath(changeNodeStart);
               xpathEnd = BungeniOdfNodeHelper.getXPath(changeNodeEnd);
            }

            String strQuery = reportChangesByOrder_Queries.ADD_CHANGE_BY_ORDER(
                    CommonFunctions.getCurrentBillID(), 
                    documentPath,
                    structuredChangeType.changeId,
                    structuredChangeType.changetype,
                    xpathStart,
                    xpathEnd,
                    Boolean.FALSE, iOrder);
            queries.add(strQuery);
            System.out.println(strQuery);
        }
        *
        */
        // first we build up a data store with the nodes and then we proces the document
        // with the nodes in the db
       for (String istring : queries) {
            log.debug("generateReport :" + istring + ";");
        }
        BungeniClientDB db = BungeniClientDB.defaultConnect();
        db.Connect();
        db.Update(queries, true);
        db.EndConnect();

        //now we iterate through the document ... and process all the top level nodes
        //lets do the sections first


       final BungeniOdfSectionHelper asectionHelper = aDochelper.getSectionHelper();
       Integer isecWeight = 0;
       NodeList nSections = asectionHelper.getDocumentSections();
        for (int i = 0; i < nSections.getLength(); i++) {
            Node sectionNode = nSections.item(i);
            OdfTextSection aSection = (OdfTextSection) sectionNode;
            String aSectionType = asectionHelper.getSectionType(aSection);
            //if it is a different section
            String aSectionName = aSection.getTextNameAttribute();
            if (!aSectionType.equals(aSectionName)) {
                if (!isInFilter(aSectionType)) {
                    //check for this section type
                    String sectionxPath = BungeniOdfNodeHelper.getXPath(sectionNode);
                    String sCheckQuery = reportChangesByOrder_Queries.UPDATE_CHANGES_FOR_SECTION_NODE(CommonFunctions.getCurrentBillID(),
                            documentPath,
                            sectionxPath,
                            aSectionType + "." + aSectionName, ++isecWeight);
                    db.Connect();
                    log.debug(sCheckQuery + ";");
                    db.Update(sCheckQuery);
                    db.EndConnect();
                }
            }
        }

       List<String> delQueries = new ArrayList<String>(0);
       //Now we handle the use ase
        String sQueryDeleted = reportChangesByOrder_Queries.CHECK_DELETED(CommonFunctions.getCurrentBillID(), documentPath);
        //look for the change id that was deleted
        QueryResults qr = db.ConnectAndQuery(sQueryDeleted);
        if (qr.hasResults()) {
            for (Vector<String> aResult : qr.theResults()) {
                String docName = qr.getField(aResult, "DOC_NAME");
                String changeId = qr.getField(aResult, "CHANGE_ID");
                String changeType = qr.getField(aResult,"CHANGE_TYPE");
                if (changeType.equals("deletion")){
                    log.debug("Processing deletion node ");
                    OdfTextChangedRegion aRegion = changesHelper.getChangedRegionById(changeId);
                    StructuredChangeType scType = changesHelper.getStructuredChangeType(aRegion);
                    NodeList nodesList = changesHelper.getDeletedNodesExt(scType.elementChange.get(0), "//text:section");
                   log.debug("Found nodes : " + nodesList.getLength());
                    for (int i = 0; i < nodesList.getLength(); i++) {
                        // this is the first section ... we only want the first section since its one change
                        OdfTextSection aSection = (OdfTextSection) nodesList.item(i);
                        String sectionName = aSection.getTextNameAttribute();
                        String sectionType = asectionHelper.getSectionType(aSection);
                        //here we have to update the changes order table with the deletion info
                        String strUpdQuery  =
                                reportChangesByOrder_Queries.UPDATE_CHANGES_FOR_DELETED_NODE(CommonFunctions.getCurrentBillID(),
                                documentPath,
                                scType.changeId,
                                sectionType + "." + sectionName,
                                0);
                        delQueries.add(strUpdQuery);
                        break;
                    }
                }
            }
        }
        for (String dstring : delQueries) {
            log.debug(dstring + " ;");
        }
        db.Connect();
        db.Update(delQueries, true);
        db.EndConnect();

        /** now generate the report from the db */

        List<BungeniOdfReportLine> reportLines = this.getReportLinesByChangeOrder(db);

         File fNewReport = BungeniOdfDocumentReport.getNewReport(aDochelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor"), aTemplate);
        BungeniOdfDocumentReport reportObject =  new BungeniOdfDocumentReport(fNewReport, aTemplate);

           reportObject.addReportVariable("BILL_NAME", CommonFunctions.getCurrentBillName());



            // reportObject.addReportVariable("REPORT_FOOTER", "Bill Amendments Report");
            //
            reportObject.addReportVariable("NO_OF_AMENDMENTS", reportLines.size());
            String sAuthor = aDochelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor");
            reportObject.addReportVariable("MEMBER_OF_PARLIAMENT", sAuthor);
            reportObject.addReportVariable(
                "OFFICIAL_DATE", aTemplate.getReportDateFormat().format(Calendar.getInstance().getTime()));
            reportObject.addReportLines(reportLines);
            reportObject.generateReport(sAuthor);

        /*
         * We do a grouped query ordered by the order_weight ...
         * the lowest order weight is the most damaging change.. followed by the others
         *
         * 
         */


        /*
        log.debug(" deletion query = " + sQueryDeleted);
        qr.resultsIterator(new IQueryResultsIterator(){

            public boolean iterateRow(QueryResults mQR, Vector<String> rowData) {
                String docName = mQR.getField(rowData, "DOC_NAME");
                String changeId = mQR.getField(rowData, "CHANGE_ID");
                String changeType = mQR.getField(rowData, "CHANGE_TYPE");
                if (changeType.equals("deletion")){
                    log.debug("Processing deletion node ");
                    OdfTextChangedRegion aRegion = changesHelper.getChangedRegionById(changeId);
                    StructuredChangeType scType = changesHelper.getStructuredChangeType(aRegion);
                    NodeList nodesList = changesHelper.getDeletedNodesExt(scType.elementChange.get(0), "//text:section");
                   log.debug("Found nodes : " + nodesList.getLength());
                    for (int i = 0; i < nodesList.getLength(); i++) {
                        // this is the first section ... we only want the first section since its one change
                        OdfTextSection aSection = (OdfTextSection) nodesList.item(i);
                        String sectionName = aSection.getTextNameAttribute();
                        String sectionType = asectionHelper.getSectionType(aSection);
                        //here we have to update the changes order table with the deletion info
                        String strUpdQuery  =
                                reportChangesByOrder_Queries.UPDATE_CHANGES_FOR_DELETED_NODE(CommonFunctions.getCurrentBillID(),
                                documentPath,
                                scType.changeId,
                                sectionType + "." + sectionName,
                                0);
                        BungeniClientDB aDb = BungeniClientDB.defaultConnect();
                        aDb.Update(strUpdQuery);
                        aDb.EndConnect();
                        log.debug(":::::: DELETED :::::::::::");
                        log.debug(" section = " + sectionName + " , sectionType = " + sectionType );

                        break;
                    }
                }
                return true;
            }

        }); */

            return reportObject;
      
    }


    private boolean isInFilter (String sectionType) {
        for (String sType : FILTER_SECTION_TYPES) {
            if (sType.equals(sectionType)) {
                return true;
            }
        }
        return false;
    }

    private List<String> addInsertQueries (BungeniOdfDocumentHelper aDochelper, List<StructuredChangeType> changes, List<String> queries) {
            BungeniOdfTrackedChangesHelper changesHelper = aDochelper.getChangesHelper();
            String documentPath = aDochelper.getDocumentPath();
            for (StructuredChangeType structuredChangeType : changes) {
            String xpathStart = "";
            String xpathEnd = "";

            if (structuredChangeType.changetype.equals(BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_DELETION__)) {
               OdfTextChange textChange = changesHelper.getChangeItem(structuredChangeType.changeId);
               xpathStart  = BungeniOdfNodeHelper.getXPath(textChange);
            } else {

               OdfTextChangeStart changeNodeStart = changesHelper.getChangeStartItem(structuredChangeType.changeId);
               OdfTextChangeEnd changeNodeEnd = changesHelper.getChangeEndItem(structuredChangeType.changeId);
               xpathStart = BungeniOdfNodeHelper.getXPath(changeNodeStart);
               xpathEnd = BungeniOdfNodeHelper.getXPath(changeNodeEnd);
            }

            String strQuery = reportChangesByOrder_Queries.ADD_CHANGE_BY_ORDER(
                    CommonFunctions.getCurrentBillID(),
                    documentPath,
                    structuredChangeType.changeId,
                    structuredChangeType.changetype,
                    xpathStart,
                    xpathEnd,
                    Boolean.FALSE, 0);
            queries.add(strQuery);
        }
        return queries;
    }


    public List<BungeniOdfReportLine> getReportLinesByChangeOrder(BungeniClientDB db) {
        List<BungeniOdfReportLine> reportLines = new ArrayList<BungeniOdfReportLine>(0);
        String sQuery = reportChangesByOrder_Queries.CHANGES_BY_ORDER(CommonFunctions.getCurrentBillID());
        QueryResults qr = db.ConnectAndQuery(sQuery);
        
        if (qr.hasResults()) {
                    log.debug("results count = " + qr.theResults().size());
            for ( Vector<String> theRow :      qr.theResults()) {

                String docName = qr.getField(theRow, "DOC_NAME");

                String changeId = qr.getField(theRow, "CHANGE_ID");

                String groupBy = qr.getField(theRow, "GROUP_BY");
                BungeniOdfDocumentHelper docHelper = null;
                try {
                    docHelper = new BungeniOdfDocumentHelper(new File(docName));
                } catch (Exception ex) {
                   log.error(ex);
                }

                if (docHelper != null) {
                    BungeniOdfChangeContext chContext = null;
                    OdfTextChangedRegion txtChangeRegion = docHelper.getChangesHelper().getChangedRegionById(changeId);
                    StructuredChangeType scType = docHelper.getChangesHelper().getStructuredChangeType(txtChangeRegion);
                    HashMap<String,Object> mapOfChange = docHelper.getChangesHelper().getChangeInfo(scType);
                    if (scType.changetype.equals("insertion")) {
                        OdfTextChangeStart chStart = docHelper.getChangesHelper().getChangeStartItem(changeId);
                        OdfTextChangeEnd chEnd = docHelper.getChangesHelper().getChangeEndItem(changeId);
                        chContext = new BungeniOdfChangeContext(chStart, chEnd, docHelper);
                    } else {
                        OdfTextChange chItem = docHelper.getChangesHelper().getChangeItem(changeId);
                        chContext = new BungeniOdfChangeContext(chItem, docHelper);
                    }
                    BungeniOdfReportLine reportLine = new BungeniOdfReportLine(chContext, mapOfChange);
                    reportLine.buildLineVariables();
                    reportLines.add(reportLine);
                }
            }
        } else {
           log.debug(" No Results");
        }
        return reportLines;
    }

    @Override
    public BungeniOdfDocumentReport generateReport(BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper[] aDochelpers) {
        BungeniClientDB db = BungeniClientDB.defaultConnect();

        /*
        for (BungeniOdfDocumentHelper aDochelper : aDochelpers) {

            final BungeniOdfTrackedChangesHelper changesHelper = aDochelper.getChangesHelper();
            List<StructuredChangeType> changes = changesHelper.getAllChanges();
            List<String> queries = new ArrayList<String>(0);
            final String documentPath = aDochelper.getDocumentPath();
            Integer iOrder = 0;
            queries.add(reportChangesByOrder_Queries.CLEANUP_QUERY(CommonFunctions.getCurrentBillID(), documentPath));
            queries = addInsertQueries(aDochelper, changes, queries) ;


            db.Connect();
            db.Update(queries, true);
            db.EndConnect();

                //now we iterate through the document ... and process all the top level nodes
                //lets do the sections first

             final BungeniOdfSectionHelper asectionHelper = aDochelper.getSectionHelper();
             Integer isecWeight = 0;
             NodeList nSections = asectionHelper.getDocumentSections();
             for (int i = 0; i < nSections.getLength(); i++) {
                    Node sectionNode = nSections.item(i);
                    OdfTextSection aSection = (OdfTextSection) sectionNode;
                    String aSectionType = asectionHelper.getSectionType(aSection);
                    //if it is a different section
                    String aSectionName = aSection.getTextNameAttribute();
                    if (!aSectionType.equals(aSectionName)) {
                        if (!isInFilter(aSectionType)) {
                            //check for this section type
                            String sectionxPath = BungeniOdfNodeHelper.getXPath(sectionNode);
                            String sCheckQuery = reportChangesByOrder_Queries.UPDATE_CHANGES_FOR_SECTION_NODE(CommonFunctions.getCurrentBillID(),
                                    documentPath,
                                    sectionxPath,
                                    aSectionType + "." + aSectionName, ++isecWeight);
                            db.Connect();
                           log.debug(sCheckQuery + ";");
                            db.Update(sCheckQuery);
                            db.EndConnect();
                        }
                    }
                }

               List<String> delQueries = new ArrayList<String>(0);
               //Now we handle the use ase
                String sQueryDeleted = reportChangesByOrder_Queries.CHECK_DELETED(CommonFunctions.getCurrentBillID(), documentPath);
                //look for the change id that was deleted
                QueryResults qr = db.ConnectAndQuery(sQueryDeleted);
                if (qr.hasResults()) {
                    for (Vector<String> aResult : qr.theResults()) {
                        String docName = qr.getField(aResult, "DOC_NAME");
                        String changeId = qr.getField(aResult, "CHANGE_ID");
                        String changeType = qr.getField(aResult,"CHANGE_TYPE");
                        if (changeType.equals("deletion")){
                            log.debug("Processing deletion node ");
                            OdfTextChangedRegion aRegion = changesHelper.getChangedRegionById(changeId);
                            StructuredChangeType scType = changesHelper.getStructuredChangeType(aRegion);
                            NodeList nodesList = changesHelper.getDeletedNodesExt(scType.elementChange.get(0), "//text:section");
                           log.debug("Found nodes : " + nodesList.getLength());
                            for (int i = 0; i < nodesList.getLength(); i++) {
                                // this is the first section ... we only want the first section since its one change
                                OdfTextSection aSection = (OdfTextSection) nodesList.item(i);
                                String sectionName = aSection.getTextNameAttribute();
                                String sectionType = asectionHelper.getSectionType(aSection);
                                //here we have to update the changes order table with the deletion info
                                String strUpdQuery  =
                                        reportChangesByOrder_Queries.UPDATE_CHANGES_FOR_DELETED_NODE(CommonFunctions.getCurrentBillID(),
                                        documentPath,
                                        scType.changeId,
                                        sectionType + "." + sectionName,
                                        0);
                                delQueries.add(strUpdQuery);
                                break;
                            }
                        }
                    }
                }
                for (String dstring : delQueries) {
                    System.out.println(dstring + " ;");
                }
                db.Connect();
                db.Update(delQueries, true);
                db.EndConnect();
        }
         *
         */
        /** now generate the report from the db */

                List<BungeniOdfReportLine> reportLines = this.getReportLinesByChangeOrder(db);

                File fNewReport = BungeniOdfDocumentReport.getNewReport("common", aTemplate);
                BungeniOdfDocumentReport reportObject =  new BungeniOdfDocumentReport(fNewReport, aTemplate);

                reportObject.addReportVariable("BILL_NAME", CommonFunctions.getCurrentBillName());
                reportObject.addReportVariable("NO_OF_AMENDMENTS", reportLines.size());
                reportObject.addReportVariable(
                        "OFFICIAL_DATE", aTemplate.getReportDateFormat().format(Calendar.getInstance().getTime()));
                reportObject.addReportLines(reportLines);
                reportObject.generateReport("common");


            return reportObject;

    }



}
