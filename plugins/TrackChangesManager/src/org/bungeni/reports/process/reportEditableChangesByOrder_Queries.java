package org.bungeni.reports.process;

/**
 *
 * @author Ashok Hariharan
 */
public class reportEditableChangesByOrder_Queries {


    public static String GET_ROOT_SECTION_HIERARCHY (String billId ) {
        String query = SECTION_HIERARCHY_COLUMNS() +
                " where bill_id = '"+ billId+ "' and section_type='root' ";
                return query;
    }

    public static String GET_SECTION_HIERARCHY( String billId, String parentSectionID) {
        String query =  SECTION_HIERARCHY_COLUMNS() + " where bill_id = '" + billId + "' and " +
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
    public static String CLEAR_SECTION_HIERARCHY (String billId, String docName) {
        String queries  = "delete from section_hierarchy where bill_id ='"+ billId + "' and doc_name='" + docName + "' " ;
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
    public static String ADD_SECTION_HIERARCHY (String billId, String docName, String secName, String secType, String secId, String secParent, Integer secOrder) {

        String queries = "insert into section_hierarchy (" +
                "bill_id, doc_name, section_name, section_type, section_id, section_parent, section_order" +
                ") values " +
                "('" +
                billId +
                "','" +
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

    public static String ADD_CHANGE_BY_ORDER(String billId, String docName,
                                                String changeId, String changeType,
                                                String pathStart, String pathEnd,
                                                Boolean bState, Integer orderWeight,
                                                Double manualOrder, Integer orderInDoc,
                                                String sOwner, String sChangeDate,
                                                String sChangeText, String sectionName,
                                                String sectionType, String sectionID) {
        String sQuery = "insert into changes_by_order (" +
                "bill_id, " +
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
                billId
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

   public static String GET_CHANGE_GROUPS_BILL_BY_MANUAL_ORDER(String billId) {
        String sQuery = "select  group_by from changes_by_order " +
                "where bill_id = '" + billId + "' " +
                "group by  group_by, manual_order order by manual_order ";
        return sQuery;
    }

   public static String GET_CHANGES_BY_GROUP_IN_DOC_ORDER(String billId, String parentSectionId) {
       String sQuery =
               "SELECT doc_name, change_id, change_type, path_start, path_end, order_in_doc, owner, change_date, change_text, order_weight, manual_order  FROM CHANGES_BY_ORDER " +
               "where section_id = '"+  parentSectionId + "'  and " +
               "bill_id = '" + billId  + "' " +
               "group by doc_name, change_id, change_type, path_start, path_end " +
               "order by doc_name, manual_order" ;
       return sQuery;
   }


   public static String UPDATE_CHANGES_BY_ORDER_MANUAL_ORDER (String billId, String changeId, Double manualOrder){
       String sQuery = "Update changes_by_order set manual_order = " + manualOrder.toString() +
                        " where change_id = '" + changeId + "' and bill_id = '" + billId + "'";
       return sQuery ;

   }

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

    public static String CLEANUP_QUERY (String billId, String docName ) {
        String sQuery = "delete from CHANGES_BY_ORDER where bill_id = '" + billId + "' " +
                "and doc_name = '" + docName + "'";
        return sQuery;
    }

    public static String CHANGES_BY_ORDER (String billId) {
        String sQuery = "Select doc_name, change_id, group_by, order_weight from changes_by_order " +
                "where bill_id = '"+ billId +"' " +
                " group by doc_name, change_id, group_by " +
                " order by order_weight";
        return sQuery;
    }
    /*
     * Order weight 0 means the item has been deleted ... the section was never found.
     */
    public static String CHECK_DELETED (String billId, String docName) {
        String sQuery = "select doc_name, change_id, change_type, path_start, path_end, processed, group_by " +
                "from changes_by_order " +
                "where bill_id ='"+ billId + "' and " +
                "doc_name = '"+ docName +"'  and " +
                "order_weight=0 and group_by=''";
        return sQuery;
    }

}
