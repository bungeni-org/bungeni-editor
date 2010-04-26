package org.bungeni.reports.process;

/**
 *
 * @author Ashok Hariharan
 */
public class reportChangesByOrder_Queries {

    public static String ADD_CHANGE_BY_ORDER(String billId, String docName, String changeId, String changeType, String pathStart, String pathEnd, Boolean bState, Integer orderWeight) {
        String sQuery = "insert into changes_by_order (bill_id, doc_name, change_id, change_type, path_start, path_end, processed, group_by, order_weight) " +
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
                + ",'', " +
                orderWeight +
                ")";
        return sQuery;

    }

    public static String CHECK_CHANGES_FOR_SECTION_NODE (String docName, String nodeFragment ){
        String sQuery = " SELECT change_id, change_type, path_start, path_end, processed " +
                "FROM CHANGES_BY_ORDER " +
                "where doc_name = '" + docName + "'  and " +
                "path_start like '" + nodeFragment + "%'";
                return sQuery;
    }

    public static String UPDATE_CHANGES_FOR_SECTION_NODE (String billId, String docName, String nodeFragment, String sectionType, Integer iWeight) {
        String sQuery = "Update CHANGES_BY_ORDER " +
                "set group_by = '" + sectionType + "' , " +
                "processed = true , " +
                "order_weight = " + iWeight + " where  " +
                "path_start like '" + nodeFragment + "%' and " +
                "bill_id = '" + billId + "' and " +
                "processed = false and" +
                "doc_name = '" + docName + "'";
        return sQuery;
    }

    public static String UPDATE_CHANGES_FOR_DELETED_NODE (String billId, String docName, String changeId, String groupBy, Integer iWeight ) {
        String sQuery = "Update changes_by_order " +
                "set group_by = '" + groupBy + "' ," +
                "processed = true ," +
                "order_weight = " + iWeight + " where " +
                "bill_id = '" + billId + "' and " +
                "doc_name = '" + docName + "' and " +
                "processed = false and " +
                "group_by = '' and " +
                "order_weight = 0" ;
        return sQuery;


    }

    public static String CLEANUP_QUERY (String billId, String docName ) {
        String sQuery = "delete from CHANGES_BY_ORDER where bill_id = '" + billId + "' " +
                "and doc_name = '" + docName + "'";
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
