

package org.bungeni.editor.selectors;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadataFieldSet;

/**
 *
 * @author Administrator
 */
public class BungeniFormContext  implements IBungeniFormContext{
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniFormContext.class.getName());
   
    // !+ACTION_RECONF (rm, jan 2012) - toolbarAction has been deprecated and all functionality
    // moved to subActionToolbar
    // private toolbarAction theAction;
    private toolbarAction theSubAction;
    private OOComponentHelper ooDocument;
    private IBungeniForm bungeniForm;
    private HashMap<String, ArrayList<Object>> fieldSets = new HashMap<String,ArrayList<Object>>();
    //move condition to container panel
    //private HashMap<String,String> conditionSets = new HashMap<String,String>();
    //private ArrayList<ooDocFieldSet> fieldSets = new ArrayList<ooDocFieldSet>(0);
    private ArrayList<ooDocMetadataFieldSet> metadataFieldSets = new ArrayList<ooDocMetadataFieldSet>(0);
    private HashMap<String,Object> preInsertMap = new HashMap<String, Object>();
   /** Creates a new instance of BungeniFormContext */
    public BungeniFormContext(){
        
    }

    // !+ACTION_RECONF  9Rm, jan 2012) - toolbarAction has been deprecated,
    // functionality moved to subActionToolbar
    // public BungeniFormContext(OOComponentHelper ooDoc,
    //        toolbarAction ta, toolbarSubAction tsa, HashMap<String,Object> pim) {
    public BungeniFormContext(OOComponentHelper ooDoc,
             toolbarAction tsa, HashMap<String,Object> pim) {
        this.setOoDocument(ooDoc);
        this.setTheSubAction(tsa);
       //  this.setTheAction(ta);
        this.setPreInsertMap(pim);
    }

    // !+ACTION_RECONF (rm, jan 2012) - toolbarAction class has been deprecated
    /**
    public toolbarAction getTheAction() {
        return theAction;
    }

    public void setTheAction(toolbarAction theAction) {
        this.theAction = theAction;
    }
    **/

    public toolbarAction getTheSubAction() {
        return theSubAction;
    }

    public void setTheSubAction(toolbarAction theSubAction) {
        this.theSubAction = theSubAction;
    }

    public OOComponentHelper getOoDocument() {
        return ooDocument;
    }

    public void setOoDocument(OOComponentHelper ooDocument) {
        this.ooDocument = ooDocument;
    }

    public IBungeniForm getBungeniForm() {
        return bungeniForm;
    }
    
    public void setBungeniForm(IBungeniForm frm){
        this.bungeniForm = frm;
    }


    public void addFieldSet(String fieldKey) {
        if (fieldSets.containsKey(fieldKey)) {
            return;
        } else {
            fieldSets.put(fieldKey, new ArrayList<Object>(0));
        }
    }

    public boolean hasFieldSet(String key) {
        if (fieldSets.containsKey(key)) {
            return true;
        } else
            return false;
    }
            
            
    public ArrayList<Object> getFieldSets(String fieldKey) {
        ArrayList<Object> theSet = null;
        try {
            theSet = fieldSets.get(fieldKey);
        } catch (Exception ex) {
            log.error("getFieldSets exception:"+ ex.getMessage());
        } finally {
            return theSet;
        }
    }
    
    public Object popObjectFromFieldSet(String fieldKey) {
        ArrayList<Object> fieldsets = getFieldSets(fieldKey);
        if( fieldsets == null ) return null;
        Object retObject = fieldsets.get(0);
        fieldsets.remove(0);
        return retObject;
    }
    /*
    public ArrayList<ooDocFieldSet> getFieldSets() {
        return fieldSets;
    }
    */
    
    public void setFieldSets(String key, ArrayList<Object> arrFieldSets) {
        this.fieldSets.put(key, arrFieldSets);
    }

    public ArrayList<ooDocMetadataFieldSet> getMetadataFieldSets() {
        return metadataFieldSets;
    }

    public void setMetadataFieldSets(ArrayList<ooDocMetadataFieldSet> metadataFieldSets) {
        this.metadataFieldSets = metadataFieldSets;
    }

    public HashMap<String, Object> getPreInsertMap() {
        return preInsertMap;
    }
    
     

    public void setPreInsertMap(HashMap<String, Object> preInsertMap) {
        this.preInsertMap = preInsertMap;
    }

 
    
}
