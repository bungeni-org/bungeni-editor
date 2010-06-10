
package org.bungeni.trackchanges.ui;

/**
 *
 * @author Ashok Hariharan
 */
public class DocumentChange {

    private final String changeId;
    private final String changeType;
    private final String pathStart;
    private final String pathEnd;
    private final String orderInDoc;
    private final String changeOwner;
    private final String documentURI;
    private final String changeDate;
    private final String changeText;

    public DocumentChange(String dURI, String cId, String cType, String pStart, String pEnd, String orderInDoc, String cOwner, String changeDate, String changeText) {
        this.documentURI = dURI;
        this.changeId = cId;
        this.changeType = cType;
        this.pathStart  = pStart;
        this.pathEnd = pEnd;
        this.orderInDoc = orderInDoc;
        this.changeOwner = cOwner;
        this.changeDate = changeDate;
        this.changeText = changeText;
    }

    /**
     * @return the changeId
     */
    public String getChangeId() {
        return changeId;
    }

    /**
     * @return the changeType
     */
    public String getChangeType() {
        return changeType;
    }

    /**
     * @return the pathStart
     */
    public String getPathStart() {
        return pathStart;
    }

    /**
     * @return the pathEnd
     */
    public String getPathEnd() {
        return pathEnd;
    }

    /**
     * @return the orderInDoc
     */
    public String getOrderInDoc() {
        return orderInDoc;
    }

    /**
     * @return the changeOwner
     */
    public String getChangeOwner() {
        return changeOwner;
    }

    /**
     * @return the documentURI
     */
    public String getDocumentURI() {
        return documentURI;
    }

    @Override
    public String toString(){
        return "[" + changeOwner + " - " + changeDate + " ]";
    }

    /**
     * @return the changeDate
     */
    public String getChangeDate() {
        return changeDate;
    }

    /**
     * @return the changeText
     */
    public String getChangeText() {
        return changeText;
    }

}
