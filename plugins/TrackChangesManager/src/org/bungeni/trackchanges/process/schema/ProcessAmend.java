package org.bungeni.trackchanges.process.schema;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ashok Hariharan
 */
public class ProcessAmend {

      private static org.apache.log4j.Logger log            =
        org.apache.log4j.Logger.getLogger(ProcessAmend.class.getName());

    private ProcessDocument parentDocument;
    private String changeId;
    private String changeAction;
    private Date changeDate;
    private String changeText;
    private Boolean changeStatus;

    public ProcessAmend(){
        
    }

    public ProcessAmend (ProcessDocument pDoc, String cId, String cAction, String cDate, String cText, String cStatus) {
        Date dDate =null;
        Boolean bStatus = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat();
            dDate = df.parse(cDate);
            bStatus = Boolean.parseBoolean(cStatus);
            ProcessAmend(pDoc, cId, cAction, dDate, cText, bStatus);
        } catch (ParseException ex) {
            Logger.getLogger(ProcessAmend.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


   public void ProcessAmend (ProcessDocument pDoc, String cId, String cAction, Date cDate, String cText, Boolean cStatus) {
        this.parentDocument = pDoc;
        this.changeId = cId;
        this.changeAction = cAction;
        this.changeDate = cDate;
        this.changeText = cText;
        this.changeStatus = cStatus;
   }


    /**
     * @return the parentDocument
     */
    public ProcessDocument getParentDocument() {
        return parentDocument;
    }

    /**
     * @param parentDocument the parentDocument to set
     */
    public void setParentDocument(ProcessDocument parentDocument) {
        this.parentDocument = parentDocument;
    }

    /**
     * @return the changeId
     */
    public String getChangeId() {
        return changeId;
    }

    /**
     * @param changeId the changeId to set
     */
    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    /**
     * @return the changeAction
     */
    public String getChangeAction() {
        return changeAction;
    }

    /**
     * @param changeAction the changeAction to set
     */
    public void setChangeAction(String changeAction) {
        this.changeAction = changeAction;
    }

    /**
     * @return the changeDate
     */
    public Date getChangeDate() {
        return changeDate;
    }

    /**
     * @param changeDate the changeDate to set
     */
    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    /**
     * @return the changeText
     */
    public String getChangeText() {
        return changeText;
    }

    /**
     * @param changeText the changeText to set
     */
    public void setChangeText(String changeText) {
        this.changeText = changeText;
    }

    /**
     * @return the changeStatus
     */
    public Boolean getChangeStatus() {
        return changeStatus;
    }

    /**
     * @param changeStatus the changeStatus to set
     */
    public void setChangeStatus(Boolean changeStatus) {
        this.changeStatus = changeStatus;
    }



}
