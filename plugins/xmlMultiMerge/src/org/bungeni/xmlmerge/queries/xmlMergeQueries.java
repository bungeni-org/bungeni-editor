package org.bungeni.xmlmerge.queries;

/**
 *
 * @author Ashok Hariharan
 */
public class xmlMergeQueries {

    public static String GET_ALL_CHANGES() {
        String query = "Select DOC_NAME, CHANGE_ID, CHANGE_TYPE, CHANGE_POS_BEFORE, CHANGE_POS_AFTER, FRAGMENT, W_ORDER" +
                " from DOCUMENT_CHANGES";
        return query;
    }

    public static String GET_ALL_CHANGES_W_ORDER(int wOrder) {
        String query = GET_ALL_CHANGES() +
                " where w_order = " + wOrder ;
        return query;
    }

    public static String escapeQuotes (String field) {
        return field.replace("'", "''");
    }

    public static String ADD_DOCUMENT_FRAGMENT (String docName, String fragmentName, String clobFragment) {
        String query = "insert into DOCUMENT_FRAGMENTS ( DOC_NAME, FRAGMENT_NAME, FRAGMENT) values (" +
                        "'" +
                        docName +
                        "','" +
                        fragmentName +
                        "','" +
                        escapeQuotes(clobFragment) +
                        "')";
        return query;
    }

    public static String GET_DOCUMENT_FRAGMENT (String docName, String fragmentName) {
        String query = "Select DOC_NAME, FRAGMENT_NAME, FRAGMENT from DOCUMENT_CHANGES " +
                "WHERE DOC_NAME = '" + docName + "' and FRAGMENT_NAME = '" + fragmentName + "'";
        return query;
    }


    public static String DELETE_ALL_CHANGES(){
        String query = "Delete from document_changes";
        return query;
    }
    public static String ADD_CHANGE( String docName, String changeId, String changeType,
            String changeBefore, String changeAfter, String fragMent, String wOrder) {
        String query = "insert into DOCUMENT_CHANGES (DOC_NAME, CHANGE_ID, CHANGE_TYPE, CHANGE_POS_BEFORE, CHANGE_POS_AFTER, FRAGMENT, W_ORDER) " +
                "values ("+
                "'" +
                docName
                + "','" +
                changeId
                + "','" +
                changeType
                + "','" +
                changeBefore
                + "','" +
                changeAfter
                + "','" +
                fragMent
                + "'," +
                wOrder
                + ")";
                return query ;
    }

}
