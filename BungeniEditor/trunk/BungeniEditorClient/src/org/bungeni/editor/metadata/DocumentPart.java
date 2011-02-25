

package org.bungeni.editor.metadata;

/**
 *
 * @author Ashok Hariharan
 */
public class DocumentPart implements Comparable {
   
    private String PartName;
    private String PartDescription;
    
    public DocumentPart(){
        PartName = PartDescription = "";
    }
    public DocumentPart(String pName, String pDesc) {
        PartName = pName;
        PartDescription = pDesc;
    }
    
    @Override
    public String toString() {
        return getPartDescription();
    }

    //AH-10-02-11 made it a comparable / sortable object
    public int compareTo(Object t) {
        DocumentPart lt = (DocumentPart) t;
        return this.getPartName().compareTo(lt.getPartName());
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object t){
        DocumentPart lt = (DocumentPart) t;
        if (getPartName().equals(lt.getPartName())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.PartName != null ? this.PartName.hashCode() : 0);
        return hash;
    }

    /**
     * @return the PartName
     */
    public String getPartName() {
        return PartName;
    }

    /**
     * @return the PartDescription
     */
    public String getPartDescription() {
        return PartDescription;
    }
}
