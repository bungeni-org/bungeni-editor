package org.bungeni.editor.selectors.debaterecord.speech;

/**
 *
 * @author Ashok Hariharan
 */
public class ObjectPerson {
    String personId ;
    String firstName;
    String lastName;
    String personURI;
    String personRole;
    
    public ObjectPerson(String pId, String fName, String lName, String uri, String role) {
        this.personId = pId;
        this.firstName = fName;
        this.lastName = lName;
        this.personURI = uri;
        this.personRole = role;
    }
    
    @Override
    public String toString(){
        return lastName + ", " + firstName;
    }
}
