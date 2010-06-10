
package org.bungeni.trackchanges.ui;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ashok Hariharan
 */
public class GroupedChange {

    /** node information **/

    String sectionType; 
    String sectionName ;

    /** child nodes **/

    // List<GroupedChange> childChanges = new ArrayList<GroupedChange>(0);

    DocumentChange documentChange = null;

    public GroupedChange(){

    }

    public GroupedChange(String sType, String sname, DocumentChange docChange) {
        this.sectionType = sType;
        this.sectionName = sname;
        this.documentChange = docChange;
    }

    
}
