
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
    private Double manualOrder;
    private final String changeOwner;
    private final String documentURI;
    private final String changeDate;
    private final String changeText;
    private final Integer orderWeight;

    public DocumentChange(String dURI, String cId, String cType, String pStart, String pEnd, String orderInDoc, String cOwner, String changeDate, String changeText, Integer orderW, Double mOrder) {
        this.documentURI = dURI;
        this.changeId = cId;
        this.changeType = cType;
        this.pathStart  = pStart;
        this.pathEnd = pEnd;
        this.orderInDoc = orderInDoc;
        this.changeOwner = cOwner;
        this.changeDate = changeDate;
        this.changeText = changeText;
        this.orderWeight = orderW;
        this.manualOrder = mOrder;
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
        return "[" + changeOwner + " - " + changeType + " ]";
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
        if (changeText == null)
            return new String("");
        return changeText;
    }

    /**
     * @return the orderWeight
     */
    public Integer getOrderWeight() {
        return orderWeight;
    }

    /**
     * @return the manualOrder
     */
    public Double getManualOrder() {
        return manualOrder;
    }

    /**
     * @param manualOrder the manualOrder to set
     */
    public void setManualOrder(Double manualOrder) {
        this.manualOrder = manualOrder;
    }



}
