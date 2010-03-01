/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.odfdocument.docinfo;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniDocAuthor {
    String authorName = "";
    String authorURN = "";

    public BungeniDocAuthor (String aname, String urn) {
        authorName = aname;
        authorURN = urn;
    }

    @Override
    public String toString(){
        return authorName;
    }

    @Override
    public int hashCode(){
        return authorName.hashCode() + authorURN.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BungeniDocAuthor other = (BungeniDocAuthor) obj;
        if ((this.authorName == null) ? (other.authorName != null) : !this.authorName.equals(other.authorName)) {
            return false;
        }
        if ((this.authorURN == null) ? (other.authorURN != null) : !this.authorURN.equals(other.authorURN)) {
            return false;
        }
        return true;
    }
}
