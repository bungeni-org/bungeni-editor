/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.changelog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
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
 
      
    String sourceDocument = "";
    String changeType  = "";
    String changeSource  = "";
    String changeTarget = "";
    String changeAction = "";
    String commitLog = "";
    
    public ChangeLog(String sourceDoc, String changeType, String changeSource, String changeTarget, String changeAction, String commitLog) {
        this.sourceDocument = sourceDoc;
        this.changeAction = changeAction ;
        this.changeType = changeType;
        this.changeSource = changeSource;
        this.changeTarget = changeTarget;
        this.commitLog = commitLog;
    }
    
    private Element addChangeType(){
        Element elemChangeType =  new Element(changeType);
        return elemChangeType;
    }
    
    private Element addSource(){
        Element elemSource = new Element("source");
        elemSource.addContent(changeSource);
        return elemSource;
    }

    private Element addTarget(){
        Element elemSource = new Element("target");
        elemSource.addContent(changeTarget);
        return elemSource;
    }

    private Element addAction(){
        Element elemAction = new Element("action");
        elemAction.addContent(changeAction);
        return elemAction;
    }
    
    private Element addSourceDocument(){
        Element elemDoc  = new Element("doc");
        File f = new File (this.sourceDocument);
        String fileName = f.getName();
        elemDoc.addContent(fileName);
        return elemDoc;
    }
    
    private Element addCommitLog(){
        Element elemAction = new Element("msg");
        elemAction.addContent(this.commitLog);
        return elemAction;
    }

    public void saveLog(){
        FileOutputStream xmlfile = null;
        try {
            Document doc = new Document(new Element("change"));
            Element change = doc.getRootElement();
            change.addContent(this.addSourceDocument());
            Element eChangeType = addChangeType();
            change.addContent(eChangeType);
            eChangeType.addContent(this.addSource());
            eChangeType.addContent(this.addTarget());
            eChangeType.addContent(this.addAction());
            eChangeType.addContent(this.addCommitLog());
            change.addContent(eChangeType);
            File f = new File(this.sourceDocument);
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
    
}
