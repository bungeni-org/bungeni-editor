
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
    private final OBJECT_TYPE objType;
    private final DocumentChange documentChange;
    /** child nodes **/



    public GroupedChange(String sType, String sname) {
        this.sectionType = sType;
        this.sectionName = sname;
        if (sectionType.equals("root")) {
            this.objType = OBJECT_TYPE.ROOT;
        } else {
            this.objType = OBJECT_TYPE.SECTION;
        }
        this.documentChange = null;
    }

    public GroupedChange (String sType, String sname, DocumentChange docChange ) {
        this.sectionName = sname;
        this.sectionType = sType;
        this.documentChange = docChange;
        this.objType = OBJECT_TYPE.CHANGE;
    }

/**
     * @return the sectionType
     */
    public String getSectionType() {
        return sectionType;
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
