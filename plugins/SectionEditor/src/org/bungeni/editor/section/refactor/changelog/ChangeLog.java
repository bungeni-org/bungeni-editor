/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.changelog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author undesa
 */
public class ChangeLog {
    
    public final static String CHANGE_TYPE_MOVE = "move";
    public final static String CHANGE_TYPE_DELETE = "delete";
    public final static String CHANGE_ACTION_MOVE_BEFORE = "moved before";
    public final static String CHANGE_ACTION_MOVE_AFTER = "moved after";
    
    private static Logger log = Logger.getLogger(ChangeLog.class.getName());

    static ChangeLog load(String fileChangeLogXML) {
        ChangeLog clObj = null;
        try {
            SAXBuilder saxb = new SAXBuilder();
            Document changeDoc = saxb.build(new File(fileChangeLogXML));
            Element changeElement = changeDoc.getRootElement();
            Element eAction = changeElement.getChild(ChangeLog.ELEM_ACTION);
            Element eChangeType = changeElement.getChild(ChangeLog.ELEM_CHANGE_TYPE);
            Element eDoc = changeElement.getChild(ChangeLog.ELEM_DOC);
            Element eMsg = changeElement.getChild(ChangeLog.ELEM_MSG);
            Element eSource = changeElement.getChild(ChangeLog.ELEM_SOURCE);
            Element eTarget =  changeElement.getChild(ChangeLog.ELEM_TARGET);
            Element eTimeStamp = changeElement.getChild(ChangeLog.ELEM_TIMESTAMP);
            ChangeLog cl = new ChangeLog(eDoc.getValue(), eChangeType.getValue(), 
                     eSource.getValue(), eTarget.getValue(), eAction.getValue(), eMsg.getValue(), eTimeStamp.getValue());
            clObj = cl;
        } catch (Exception ex) {
            log.error("create: "+ ex.getMessage());
        } finally {
            return clObj;
        }
    }
    private String sourceDocument = "";
    private String changeType = "";
    private String changeSource = "";
    private String changeTarget = "";
    private String changeAction = "";
    private String commitLog = "";
    private String changeDatetime = "";
    
    public ChangeLog(String sourceDoc, String changeType, String changeSource, String changeTarget, String changeAction, String commitLog, String timeStamp) {
        this.sourceDocument = sourceDoc;
        this.changeAction = changeAction ;
        this.changeType = changeType;
        this.changeSource = changeSource;
        this.changeTarget = changeTarget;
        this.commitLog = commitLog;
        this.changeDatetime = timeStamp;
    }
    
    public final static String ELEM_CHANGE_TYPE = "changeType";
    private Element addChangeType(){
        Element elemChangeType =  new Element(ELEM_CHANGE_TYPE);
        elemChangeType.addContent(getChangeType());
        return elemChangeType;
    }
    
    public final static String ELEM_SOURCE = "source";
    private Element addSource(){
        Element elemSource = new Element(ELEM_SOURCE);
        elemSource.addContent(getChangeSource());
        return elemSource;
    }

      public final static String ELEM_TARGET = "target";
  
    private Element addTarget(){
        Element elemSource = new Element(ELEM_TARGET);
        elemSource.addContent(getChangeTarget());
        return elemSource;
    }

    public final static String ELEM_ACTION = "action";
  
    private Element addAction(){
        Element elemAction = new Element(ELEM_ACTION);
        elemAction.addContent(getChangeAction());
        return elemAction;
    }
    
    public final static String ELEM_DOC = "doc";

    private Element addSourceDocument(){
        Element elemDoc  = new Element(ELEM_DOC);
        File f = new File (this.getSourceDocument());
        String fileName = f.getName();
        elemDoc.addContent(fileName);
        return elemDoc;
    }
    
     public final static String ELEM_MSG = "msg";
     
    private Element addCommitLog(){
        Element elemAction = new Element(ELEM_MSG);
        elemAction.addContent(this.getCommitLog());
        return elemAction;
    }
    
    public final static String ELEM_TIMESTAMP = "timestamp";
    
    private Element addTimestamp(){
        Element elemTimeStamp = new Element(ELEM_TIMESTAMP);
        elemTimeStamp.addContent(this.getChangeDateTime());
        return elemTimeStamp;
    }

    /**
     * <change>
     * <changeType />
     * <source />
     * <target />
     * <action />
     * <msg />
     * </change>
     */
    public void saveLog(){
        FileOutputStream xmlfile = null;
        try {
            Document doc = new Document(new Element("change"));
            Element change = doc.getRootElement();
            change.addContent(this.addSourceDocument()); //<change><source> ...</source></change>
            Element eChangeType = addChangeType(); 
            change.addContent(eChangeType); 
            change.addContent(this.addSource());
            change.addContent(this.addTarget());
            change.addContent(this.addAction());
            change.addContent(this.addCommitLog());
            change.addContent(this.addTimestamp());
           // change.addContent(eChangeType);
            File f = new File(this.getSourceDocument());
            String parentPath = f.getParent();
            String fileName = f.getName().replaceAll("odt", "") + ".xml";
            File fxml = new File(parentPath + File.separator + fileName);
            xmlfile = new FileOutputStream(fxml);
            XMLOutputter xmlout = new XMLOutputter();
            xmlout.output(doc, xmlfile);
            xmlfile.close();
        } catch (IOException ex) {
            log.error("saveLog : " +  ex.getMessage());
        } 
    }

    public String getSourceDocument() {
        return sourceDocument;
    }

    public String getChangeType() {
        return changeType;
    }

    public String getChangeSource() {
        return changeSource;
    }

    public String getChangeTarget() {
        return changeTarget;
    }

    public String getChangeAction() {
        return changeAction;
    }

    public String getCommitLog() {
        return commitLog;
    }
    
    public String getChangeDateTime(){
        return this.changeDatetime;
    }
}
