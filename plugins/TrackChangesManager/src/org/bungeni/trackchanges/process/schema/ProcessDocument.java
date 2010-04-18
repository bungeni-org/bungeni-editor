package org.bungeni.trackchanges.process.schema;

/**
 *
 * @author Ashok Hariharan
 */
public class ProcessDocument {
    private String processId;
    private String docName;
    private String docPath;
    private String docOwner;

    public ProcessDocument(String processId, String docName, String docPath, String docOwner) {
        this.processId = processId;
        this.docName = docName;
        this.docOwner = docOwner;
        this.docPath = docPath;
    }

    @Override
    public String toString(){
        return docOwner + " (" + docName + ")";
    }

    /**
     * @return the processId
     */
    public String getProcessId() {
        return processId;
    }

    /**
     * @param processId the processId to set
     */
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    /**
     * @return the docName
     */
    public String getDocName() {
        return docName;
    }

    /**
     * @param docName the docName to set
     */
    public void setDocName(String docName) {
        this.docName = docName;
    }

    /**
     * @return the docPath
     */
    public String getDocPath() {
        return docPath;
    }

    /**
     * @param docPath the docPath to set
     */
    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    /**
     * @return the docOwner
     */
    public String getDocOwner() {
        return docOwner;
    }

    /**
     * @param docOwner the docOwner to set
     */
    public void setDocOwner(String docOwner) {
        this.docOwner = docOwner;
    }
}
