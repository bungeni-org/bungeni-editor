package org.bungeni.reports.process;

/**
 *
 * @author Ashok Hariharan
 */
public class reportChangesByOrder_Queries {

    public static String ADD_CHANGE_BY_ORDER(String docName, String changeId, String changeType, String pathStart, String pathEnd) {
        String sQuery = "insert into changes_by_order (doc_name, change_id, change_type, path_start, path_end) " +
                "values ("
                + "'" +
                docName
                + "','" +
                changeId
                + "','" +
                changeType
                + "','" +
                pathStart
                + "','" +
                pathEnd
                + "'" +
                ")";
        return sQuery;
    }

}
