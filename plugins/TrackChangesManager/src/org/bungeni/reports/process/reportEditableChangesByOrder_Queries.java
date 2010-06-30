package org.bungeni.reports.process;

import java.util.Date;

/**
 *
 * @author Ashok Hariharan
 */
public class reportEditableChangesByOrder_Queries {


    public static String GET_NEWEST_REPORT_ID_FOR_BILL(String billId, String reportName) {
        String query = "Select top 1 report_id, report_name, generated_date, user_edited_name from generated_reports where " +
                "bill_id = '" + billId + "'  and report_name='"+ reportName +"' order by generated_date desc ";
        return query;
    }


    public static String ADD_NEW_REPORT_FOR_BILL(String reportId, String reportName, Date genDate, String billId, Boolean userEditedName) {
        String query = "insert into generated_reports (report_id, report_name, generated_date, bill_id, user_edited_name) values (" +
                "'" + reportId + "','" + reportName + "','" + (new java.sql.Timestamp(genDate.getTime())) + "','" + billId + "'," + userEditedName + ")";
        return query;
    }

    public static String GET_INFO_FOR_REPORT_ID(String reportId) {
        String query = "Select report_id, report_name, generated_date, bill_id, user_edited_name from generated_reports where " +
                "report_id = '" + reportId + "'";
        return query;
    }

    public static String GET_ROOT_SECTION_HIERARCHY (String reportId ) {
        String query = SECTION_HIERARCHY_COLUMNS() +
                " where report_id = '"+ reportId + "' and section_type='root' ";
                return query;
    }

    public static String GET_SECTION_HIERARCHY( String reportId, String parentSectionID) {
        String query =  SECTION_HIERARCHY_COLUMNS() + " where report_id = '" + reportId + "' and " +
                "section_parent = '" + parentSectionID + "' order by section_order asc";
        return query;
    }

    private static String SECTION_HIERARCHY_COLUMNS() {
        String query ="Select section_name, section_type, section_id, section_parent, section_order, doc_name " +
                "from section_hierarchy ";
        return query;
    }

    /**
     * Clears the section hierarchy 
     * @param billId Bill Identifier
     * @param docName Document Name
     * @return
     */
    public static String CLEAR_SECTION_HIERARCHY (String reportId) {
        String queries  = "delete from section_hierarchy where report_id='"+ reportId + "' " ;
        return queries;
    }

    /**
     * Inserts info about a section into the section hierarchy
     * @param billId Bill Identifer
     * @param docName Document Name
     * @param secName Section Name
     * @param secType Section Type
     * @param secId Section Identifier
     * @param secParent Section Parent
     * @param secOrder Section Order (within Parent)
     * @return
     */
    public static String ADD_SECTION_HIERARCHY (String reportId,  String docName, String secName, String secType, String secId, String secParent, Integer secOrder) {

        String queries = "insert into section_hierarchy (" +
                "report_id, doc_name, section_name, section_type, section_id, section_parent, section_order" +
                ") values " +
                "('" +
                reportId +
                "','"+
                docName +
                "','" +
                secName +
                "','" +
                secType +
                "','" +
                secId +
                "','" +
                secParent +
                "'," +
                secOrder  +
                ")";

                return queries;

    }


    public static String GET_SECTION_INFO(String reportId, String sectionId ) {
        String query = "select  section_name, section_id, section_parent, section_order from section_hierarchy where report_id = '"+ reportId +  "' and section_id = '"+ reportId+ "' ";
        return query;

    }


    public static String ADD_CHANGE_BY_ORDER(String reportId,  String docName,
                                                String changeId, String changeType,
                                                String pathStart, String pathEnd,
                                                Boolean bState, Integer orderWeight,
                                                Double manualOrder, Integer orderInDoc,
                                                String sOwner, String sChangeDate,
                                                String sChangeText, String sectionName,
                                                String sectionType, String sectionID) {
        String sQuery = "insert into changes_by_order (" +
                "report_id, " +
                "doc_name, " +
                "change_id, " +
                "change_type, " +
                "path_start, " +
                "path_end, " +
                "processed, " +
                "group_by, " +
                "order_weight, " +
                "manual_order, " +
                "order_in_doc, " +
                "owner, " +
                "change_date, " +
                "change_text, " +
                "section_name, " +
                "section_type, " +
                "section_id ) " +
                "values ("
                + "'" +
                reportId 
                + "','" +
                docName
                + "','" +
                changeId
                + "','" +
                changeType
                + "','" +
                pathStart
                + "','" +
                pathEnd
                + "'," +
                bState.toString()
                + "," +
                "''" + ", " +
                orderWeight +
                "," +
                manualOrder +
                "," +
                orderInDoc +
                ",'" + sOwner +
                "','" + sChangeDate +
                "','" + sChangeText+
                "','" + sectionName +
                "','" + sectionType +
                "','" + sectionID +
                "')";
        return sQuery;

    }

    public static String CHECK_CHANGES_FOR_SECTION_NODE (String docName, String nodeFragment ){
        String sQuery = " SELECT change_id, change_type, path_start, path_end, processed " +
                "FROM CHANGES_BY_ORDER " +
                "where doc_name = '" + docName + "'  and " +
                "path_start like '" + nodeFragment + "%'";
                return sQuery;
    }
/*
   public static String GET_CHANGE_GROUPS_BILL_BY_MANUAL_ORDER(String billId) {
        String sQuery = "select  group_by from changes_by_order " +
                "where bill_id = '" + billId + "' " +
                "group by  group_by, manual_order order by manual_order ";
        return sQuery;
    }
*/

   public static String GET_CHANGES_BY_GROUP_IN_DOC_ORDER(String report_id, String parentSectionId) {
       String sQuery =
               "SELECT doc_name, change_id, change_type, path_start, path_end, order_in_doc, owner, change_date, change_text, order_weight, manual_order  FROM CHANGES_BY_ORDER " +
               "where section_id = '"+  parentSectionId + "'  and " +
               "report_id = '" + report_id  + "' " +
               "group by doc_name, change_id, change_type, path_start, path_end " +
               "order by manual_order" ;
       return sQuery;
   }


   public static String UPDATE_CHANGES_BY_ORDER_MANUAL_ORDER (String reportId, String changeId, Double manualOrder){
       String sQuery = "Update changes_by_order set manual_order = " + manualOrder.toString() +
                        " where change_id = '" + changeId + "' and report_id = '" + reportId + "'";
       return sQuery ;

   }
/*
    public static String UPDATE_CHANGES_FOR_SECTION_NODE (String billId, String docName, String nodeFragment, String sectionType, Integer iWeight, Double dOrder) {
        String sQuery = "Update CHANGES_BY_ORDER " +
                "set group_by = '" + sectionType + "' , " +
                "processed = true , " +
                "order_weight = " + iWeight + ", " +
                "manual_order = " + dOrder + " where  " +
                "path_start like '" + nodeFragment + "%' and " +
                "bill_id = '" + billId + "' and " +
                "processed = false and " +
                "doc_name = '" + docName + "'";
        return sQuery;
    }
*/
/*
    public static String UPDATE_CHANGES_FOR_DELETED_NODE (String billId, String docName, String changeId, String groupBy, Integer iWeight, Double dOrder ) {
        String sQuery = "Update changes_by_order " +
                "set group_by = '" + groupBy + "' ," +
                "processed = true ," +
                "order_weight = " + iWeight + ", " +
                "manual_order = " + dOrder + " where " +
                "bill_id = '" + billId + "' and " +
                "doc_name = '" + docName + "' and " +
                "processed = false and " +
                "change_id = '" +changeId +"' and " +
                "group_by = '' and " +
                "order_weight = 0" ;
        return sQuery;


    }
*/
    public static String CLEANUP_QUERY (String reportId ) {
        String sQuery = "delete from CHANGES_BY_ORDER where report_id = '" + reportId + "' ";
        return sQuery;
    }

    public static String CHANGES_BY_ORDER (String reportId) {
        String sQuery = "Select doc_name, change_id, group_by, order_weight from changes_by_order " +
                "where report_id = '"+ reportId +"' " +
                " group by doc_name, change_id, group_by " +
                " order by order_weight";
        return sQuery;
    }
    /*
     * Order weight 0 means the item has been deleted ... the section was never found.
     */
    /*
    public static String CHECK_DELETED (String billId, String docName) {
        String sQuery = "select doc_name, change_id, change_type, path_start, path_end, processed, group_by " +
                "from changes_by_order " +
                "where bill_id ='"+ billId + "' and " +
                "doc_name = '"+ docName +"'  and " +
                "order_weight=0 and group_by=''";
        return sQuery;
    }*/

}
