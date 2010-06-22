
package org.bungeni.trackchanges.ui;

/**
 *
 * @author Ashok Hariharan
 */
public class GroupedChange {

    

    /** node information **/

    public enum OBJECT_TYPE { ROOT, SECTION, CHANGE }
    private final String sectionType;
    private final String sectionName;
    private final String sectionID;
    private final OBJECT_TYPE objType;
    private final DocumentChange documentChange;
    private boolean sectionDeleteChange ;

    /** child nodes **/



    public GroupedChange(String sType, String sname, String sId) {
        this.sectionType = sType;
        this.sectionName = sname;
        this.sectionID = sId;
        if (sectionType.equals("root")) {
            this.objType = OBJECT_TYPE.ROOT;
        } else {
            this.objType = OBJECT_TYPE.SECTION;
        }
        this.documentChange = null;
        this.sectionDeleteChange = false;
    }

    public GroupedChange (String sType, String sname, String sId, DocumentChange docChange ) {
        this.sectionName = sname;
        this.sectionType = sType;
        this.sectionID = sId;
        this.documentChange = docChange;
        this.objType = OBJECT_TYPE.CHANGE;
    }

/**
     * @return the sectionType
     */
    public String getSectionType() {
        return sectionType;
    }

    public String getSectionID(){
        return this.sectionID;
    }

    public boolean getSectionDeleteChange(){
        return sectionDeleteChange;
    }

    public void setSectionDeleteChange(boolean dchange){
        this.sectionDeleteChange = dchange;
    }

    /**
     * @return the sectionName
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * @return the objType
     */
    public OBJECT_TYPE getObjType() {
        return objType;
    }

    /**
     * @return the documentChange
     */
    public DocumentChange getDocumentChange() {
        return documentChange;
    }

    @Override
    public String toString(){
        if (this.objType == OBJECT_TYPE.ROOT || this.objType == OBJECT_TYPE.SECTION) {
            return sectionType + "(" + sectionName + ")";
        } else {
            return documentChange.toString();
        }
    }

    
}
