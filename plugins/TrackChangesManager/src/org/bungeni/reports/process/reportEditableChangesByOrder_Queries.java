package org.bungeni.reports.process;

import org.bungeni.db.BungeniClientDB;

/**
 *
 * @author Ashok Hariharan
 */
public class reportEditableChangesByOrder_Queries {

    public static String ADD_CHANGE_BY_ORDER(String billId, String docName,
                                                String changeId, String changeType,
                                                String pathStart, String pathEnd,
                                                Boolean bState, Integer orderWeight,
                                                Double manualOrder, Integer orderInDoc,
                                                String sOwner, String sChangeDate,
                                                String sChangeText) {
        String sQuery = "insert into changes_by_order (bill_id, doc_name, change_id, " +
                "change_type, path_start, path_end, " +
                "processed, group_by, order_weight, " +
                "manual_order, order_in_doc, owner, change_date, change_text) " +
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
                "," +
                manualOrder +
                "," +
                orderInDoc +
                ",'" + sOwner +
                "','" + sChangeDate +
                "','" + sChangeText+ "')";
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

   public static String GET_CHANGES_BY_GROUP_IN_DOC_ORDER(String billId, String changeGroup) {
       String sQuery =
               "SELECT doc_name, change_id, change_type, path_start, path_end, order_in_doc, owner, change_date, change_text  FROM CHANGES_BY_ORDER " +
               "where group_by = '"+  changeGroup + "'  and " +
               "bill_id = '" + billId  + "' " +
               "group by doc_name, change_id, change_type, path_start, path_end " +
               "order by doc_name, order_in_doc" ;
       return sQuery;
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
