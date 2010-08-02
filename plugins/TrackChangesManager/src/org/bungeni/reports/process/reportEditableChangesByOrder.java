package org.bungeni.reports.process;

import java.io.File;
import java.io.FilenameFilter;
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
import org.bungeni.odfdocument.report.BungeniOdfReportHeader;
import org.bungeni.odfdocument.report.BungeniOdfReportLine;
import org.bungeni.odfdocument.report.GeneratedReport;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfChangeContext;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.bungeni.odfdom.utils.BungeniOdfDateHelper;
import org.bungeni.odfdom.utils.BungeniOdfNodeHelper;
import org.bungeni.trackchanges.ui.panelReportByOrder.TREE_LOADING_MODES;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.odftoolkit.odfdom.doc.text.OdfTextChange;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeEnd;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeStart;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.odftoolkit.odfdom.doc.text.OdfTextSection;
import org.w3c.dom.Node;

/**
 *
 * @author Ashok Hariharan
 */
public class reportEditableChangesByOrder  extends BungeniOdfDocumentReportProcess {

      private static org.apache.log4j.Logger log                  =
        Logger.getLogger(reportEditableChangesByOrder.class.getName());

    private final String[] FILTER_SECTION_TYPES = {
        "body"
    };

    GeneratedReport m_genReport = null;
    TREE_LOADING_MODES m_loadingMode = null;
    private void _prepareInputParams(BungeniClientDB db, HashMap<String, Object> paramMap) {
            m_genReport = (GeneratedReport) paramMap.get("REPORT_INFO");
            m_loadingMode = (TREE_LOADING_MODES) paramMap.get("TREE_LOADING_MODE");
            /*
             * If its a brand new report being generated add a unique report id for the bill
             */
            if (m_loadingMode == TREE_LOADING_MODES.NEW) {
                db.Connect();
                db.Update(reportEditableChangesByOrder_Queries.ADD_NEW_REPORT_FOR_BILL(
                        m_genReport.getReportId(),
                        m_genReport.getReportName(),
                        m_genReport.getReportGenerationDate(),
                        CommonFunctions.getCurrentBillID(),
                        Boolean.FALSE));
                db.EndConnect();
            }

    }



    public boolean prepareProcess(BungeniOdfDocumentHelper[] aDochelpers, HashMap<String,Object> paramMap) {
        BungeniClientDB db = BungeniClientDB.defaultConnect();
        _prepareInputParams(db, paramMap);
        /**
         * if its LOAD_RECENT we dont process further
         */
        if (m_loadingMode == TREE_LOADING_MODES.LOAD_RECENT) {
            return true;
        }

        /**
         * We rebuild the section hierarchy only if the report is being generated afresh or an existing report is being regenerated.
         */
        if (this.m_loadingMode == TREE_LOADING_MODES.NEW || this.m_loadingMode == TREE_LOADING_MODES.UPDATE_RECENT) {
            db.Connect();
            List<String> hierarchyQueries = new ArrayList<String>(0);
            try {
                hierarchyQueries = _buildHierarchy();
            } catch (Exception ex) {
                log.error("prepareProgress:buildHierarchy:" + ex.getMessage(), ex);
            }
            db.Update(hierarchyQueries, true);
            db.EndConnect();
        }
        int iOrder = 0;
        List<String> queries = new ArrayList<String>(0);
        
        for (BungeniOdfDocumentHelper aDochelper : aDochelpers) {
            final BungeniOdfTrackedChangesHelper changesHelper = aDochelper.getChangesHelper();
            List<StructuredChangeType> changes = changesHelper.getAllChanges();
            final String documentPath = aDochelper.getDocumentPath();
            /**
             * Cleanup is only requiered in UPDATE_RECENT mode for the very first execution
             */
            if (this.m_loadingMode == TREE_LOADING_MODES.UPDATE_RECENT && iOrder == 0) {
                db.Connect();
                db.Update(reportEditableChangesByOrder_Queries.CLEANUP_QUERY(m_genReport.getReportId()));
                db.EndConnect();
                iOrder++;
            }


            queries.addAll(_addInsertQueries(aDochelper, changes));
        }

        db.Connect();
        db.Update(queries, true);
        db.EndConnect();

        return true;
    }


    private List<String> _buildHierarchy() throws Exception{
        ArrayList<String> queries = new ArrayList<String>(0);
        String folderForMain = CommonFunctions.getWorkspaceForBill(CommonFunctions.getCurrentBillID()) + File.separator + "main";
        File fFolderForMain = new File(folderForMain);
        File[] files = fFolderForMain.listFiles(new FilenameFilter(){
            public boolean accept(File dir, String name) {
                if (name.endsWith(".odt")) {
                    return true;
                }
                return false;
            }
        });
        BungeniOdfDocumentHelper origDoc = new BungeniOdfDocumentHelper(files[0]);
        BungeniOdfSectionHelper secHelper = origDoc.getSectionHelper();
        OdfTextSection aSection = secHelper.getSection("bill");
        String sectionID = secHelper.getSectionID(aSection);
        queries.add(reportEditableChangesByOrder_Queries.CLEAR_SECTION_HIERARCHY( m_genReport.getReportId()));
        queries.add(reportEditableChangesByOrder_Queries.ADD_SECTION_HIERARCHY(m_genReport.getReportId(),  origDoc.getDocumentPath(), "bill", "root", sectionID, "", 0));
        _processHierarchyChildSections(origDoc, secHelper, aSection, queries);
        return queries;

    }

    private void _processHierarchyChildSections (BungeniOdfDocumentHelper origDoc, BungeniOdfSectionHelper secHelper, OdfTextSection aSection, ArrayList<String> queries) {
        List<OdfTextSection> sections = secHelper.getChildSections(aSection);
        String parentSectionId = secHelper.getSectionID(aSection);
        for (int i = 0; i < sections.size(); i++) {
            OdfTextSection thisSection = sections.get(i);
            String thisSecType = secHelper.getSectionType(thisSection);
            String thisSecName = thisSection.getTextNameAttribute();
            String thisSecId = secHelper.getSectionID(thisSection);
            if (thisSecId.length() > 0 ) {
                queries.add(reportEditableChangesByOrder_Queries.ADD_SECTION_HIERARCHY(m_genReport.getReportId(),  origDoc.getDocumentPath(), thisSecName, thisSecType, thisSecId,parentSectionId, i));
                _processHierarchyChildSections(origDoc, secHelper, thisSection, queries);
            }
        }
    }


    //Returns the order of a section within its parent container
   private Integer _getSectionOrder(BungeniClientDB db, String secId) {
       String query = reportEditableChangesByOrder_Queries.GET_SECTION_INFO(this.m_genReport.getReportId(), secId);
               log.debug("_getSectionOrder : " + query);
       QueryResults qr = db.ConnectAndQuery(query);

       if (qr.hasResults()) {
           return Integer.parseInt(qr.getSingleColumnResult("SECTION_ORDER")[0]) + 1;
       }
       return -1;
   }


    private void _processHeaderHiearchy (BungeniClientDB db, List<BungeniOdfReportHeader> headers, String baseSecId, BungeniOdfDocumentHelper dochelper  ) {
                    //query child sections
                QueryResults qr = db.ConnectAndQuery(reportEditableChangesByOrder_Queries.GET_SECTION_HIERARCHY(
                            this.m_genReport.getReportId(),
                            baseSecId
                            ));
                 if (qr.hasResults()) {
                     Vector<Vector<String>> results = qr.theResults();
                     for (Vector<String> aRow : results) {
                         BungeniOdfReportHeader reportHeader = new BungeniOdfReportHeader();
                         String secId = qr.getField(aRow, "SECTION_ID");
                         String secType = qr.getField(aRow, "SECTION_TYPE");
                         String secName = qr.getField(aRow, "SECTION_NAME");
                         BungeniOdfSectionHelper sechelper = dochelper.getSectionHelper();
                         
                         reportHeader.setHeaderValue("SECTION_ID", secId);
                         reportHeader.setHeaderValue("SECTION_NAME",  secName);
                         reportHeader.setHeaderValue("SECTION_TYPE",  secType);
                         reportHeader.setHeaderValue("SECTION_TYPE_INDEX", _getSectionOrder(db, secId).toString());
                         reportHeader.setHeaderValue("SECTION_TYPE_INDEX_SUFFIX", CommonFunctions.getOrdinalFor(reportHeader.getHeaderValue("SECTION_TYPE_INDEX")));
                         reportHeader.setHeaderValue("OLD_TEXT", sechelper.getSection(secName).getTextContent());
                         headers.add(reportHeader);
                         _buildReportLines (db, reportHeader);
                         _processHeaderHiearchy(db,headers, secId, dochelper);
                         //dmtParent.add(dmtSec);
                     }
                 }
        }

    private void _buildReportLines (BungeniClientDB db, BungeniOdfReportHeader hdr) {
        List<BungeniOdfReportLine> reportLines = new ArrayList<BungeniOdfReportLine>(0);
        QueryResults qr = db.ConnectAndQuery(reportEditableChangesByOrder_Queries.GET_CHANGES_BY_GROUP_IN_DOC_ORDER(this.m_genReport.getReportId(), hdr.getHeaderValue("SECTION_ID")));
        if (qr.hasResults()) {
            for (Vector<String> aRow : qr.theResults()) {
                //SELECT doc_name, change_id, change_type, path_start, path_end, order_in_doc, owner, change_date, change_text, order_weight, manual_order
                String docName = qr.getField(aRow, "DOC_NAME");
                String changeId = qr.getField(aRow, "CHANGE_ID");
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

        }
        hdr.setReportLines(reportLines);

    }

       


    @Override
    public BungeniOdfDocumentReport generateReport(BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper aDochelper) {
        return null;
      
    }


    private boolean _isInFilter (String sectionType) {
        for (String sType : FILTER_SECTION_TYPES) {
            if (sType.equals(sectionType)) {
                return true;
            }
        }
        return false;
    }


    private OdfTextSection _getNearestSectionToChange(Node aChange) {
        Node aParentNode = aChange.getParentNode();
        while (aParentNode != null) {
            if (aParentNode.getNodeName().equals(OdfTextSection.ELEMENT_NAME.getQName())) {
                //this is a section 
                return (OdfTextSection) aParentNode;
            } else {
                aParentNode = aParentNode.getParentNode();
            }
        }
        return null;
    }

    private List<String> _addInsertQueries (BungeniOdfDocumentHelper aDochelper, List<StructuredChangeType> changes) {
            List<String> queries = new ArrayList<String>(0);
            BungeniOdfTrackedChangesHelper changesHelper = aDochelper.getChangesHelper();
            String documentPath = aDochelper.getDocumentPath();
            //String changesOwner = aDochelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor");
            Integer iOrderInDoc = 0;
            for (StructuredChangeType structuredChangeType : changes) {
            String xpathStart = "";
            String xpathEnd = "";
            HashMap<String,Object> changeInfo = changesHelper.getChangeInfo(structuredChangeType);
            OdfTextSection nearSection = null;
            if (structuredChangeType.changetype.equals(BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_DELETION__)) {
               OdfTextChange changeNode = changesHelper.getChangeItem(structuredChangeType.changeId);
               xpathStart  = BungeniOdfNodeHelper.getXPath(changeNode);

                nearSection = _getNearestSectionToChange(changeNode);

            } else {

               OdfTextChangeStart changeNodeStart = changesHelper.getChangeStartItem(structuredChangeType.changeId);
               OdfTextChangeEnd changeNodeEnd = changesHelper.getChangeEndItem(structuredChangeType.changeId);
               xpathStart = BungeniOdfNodeHelper.getXPath(changeNodeStart);
               xpathEnd = BungeniOdfNodeHelper.getXPath(changeNodeEnd);

               nearSection = _getNearestSectionToChange(changeNodeStart);
            }
            BungeniOdfSectionHelper secHelper = aDochelper.getSectionHelper();
            String secId = secHelper.getSectionID(nearSection);
            String secType = secHelper.getSectionType(nearSection);
            String secName = nearSection.getTextNameAttribute();

            ++iOrderInDoc;

            String strQuery = reportEditableChangesByOrder_Queries.ADD_CHANGE_BY_ORDER(
                    this.m_genReport.getReportId(),
                    documentPath,
                    structuredChangeType.changeId,
                    structuredChangeType.changetype,
                    xpathStart,
                    xpathEnd,
                    //we set the manual order to the same as the order in the document
                    //so that we can display the tree initially in manual order and still
                    //show it in natural order initially
                    Boolean.FALSE, 0, iOrderInDoc.doubleValue(), iOrderInDoc,
                    changeInfo.get("dcCreator").toString(),
                    BungeniOdfDateHelper.odfDateToFormattedJavaDate(changeInfo.get("dcDate").toString()) ,
                    BungeniClientDB.escapeQuotes(changeInfo.get("changeText").toString()),
                    secName,
                    secType,
                    secId);
            log.info("addInsertQueries : " + strQuery);
            queries.add(strQuery);
        }
        return queries;
    }



    @Override
    public BungeniOdfDocumentReport generateReport(BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper[] aDochelpers) {
        BungeniClientDB db = BungeniClientDB.defaultConnect();
        /**
         * 
         * THe report is generated from the db
         */

         /**
          * This array captures the report headers
          */
         List<BungeniOdfReportHeader> headers = new ArrayList<BungeniOdfReportHeader>(0);
            /**
             * Start with the root section
             */
         String rQuery = reportEditableChangesByOrder_Queries.GET_ROOT_SECTION_HIERARCHY(this.m_genReport.getReportId());
         QueryResults qr = db.ConnectAndQuery(rQuery);
         if (qr.hasResults()) {
             //Select section_name, section_type, section_id, section_parent, section_order, doc_name
                Vector<Vector<String>> rows = qr.theResults();
                String[] idColumns = qr.getSingleColumnResult("SECTION_ID");
                String[] docNames = qr.getSingleColumnResult("DOC_NAME");

                String rootSectionId = idColumns[0];
                String docName = docNames[0];
                BungeniOdfDocumentHelper docHelper = null;
            try {
                docHelper = new BungeniOdfDocumentHelper(new File(docName));
            } catch (Exception ex) {
                log.error(ex);
            }
                //now iterate through the hieararchy
            _processHeaderHiearchy(db, headers, rootSectionId, docHelper);
            /**
             * Now the reportHeaders and reportLines have been populated
             */

                /**
                 * Start outputtting the report
                 */
                File fNewReport = BungeniOdfDocumentReport.getNewReport("common", aTemplate);
                BungeniOdfDocumentReport reportObject =  new BungeniOdfDocumentReport(fNewReport, aTemplate);

                reportObject.addReportVariable("BILL_NAME", CommonFunctions.getCurrentBillName());
              //  reportObject.addReportVariable("NO_OF_AMENDMENTS", reportLines.size());
                reportObject.addReportVariable(
                        "OFFICIAL_DATE", aTemplate.getReportDateFormat().format(Calendar.getInstance().getTime()));
                reportObject.addReportHeaders(headers);
                reportObject.generateReport("common");


            return reportObject;

    }

    return null;

}


}
