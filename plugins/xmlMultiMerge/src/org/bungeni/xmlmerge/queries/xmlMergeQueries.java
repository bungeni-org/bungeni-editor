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
                + "','" +
                wOrder
                + "'" +
                ")";
                return query ;
    }

}
