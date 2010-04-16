package org.bungeni.trackchanges.queries;

/**
 *
 * @author Ashok Hariharan
 */
public class ProcessQueries {

    public static String INSERT_PROCESS (String processId, String billId) {
        return "insert into process ( process_id, bill_id) " +
                "values ('"+
                            processId
                            + "','" +
                            billId
                 + "')";
    }


    public static String INSERT_PROCESS_DOCS (String processId, String filename, String documentPath, String docAuthor) {
        return "insert into process_documents " +
                "( process_id, doc_name, doc_path, doc_owner) values ('"+
                 processId
                 + "','" +
                 filename
                 + "','" +
                 documentPath
                 + "','" +
                 docAuthor
                 + "')";
    }

}
