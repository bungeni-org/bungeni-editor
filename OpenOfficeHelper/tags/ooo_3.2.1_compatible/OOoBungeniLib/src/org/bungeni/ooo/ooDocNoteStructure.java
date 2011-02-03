/*
 * ooDocNoteStructure.java
 *
 * Created on August 22, 2007, 11:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo;

/**
 *
 * @author Administrator
 */
public class ooDocNoteStructure {
    
    String noteText;
    String noteDate;
    String noteAuthor;
    
    /** Creates a new instance of ooDocNoteStructure */
    public ooDocNoteStructure(String date, String author, String text) {
        noteText = text.replaceAll("[\\r\\n\\f]","");
        noteDate = date;
        noteAuthor = author;
    }

    public String toString() {
        return getNoteDate() + " ["+ getNoteAuthor()+ "]";
    }
    
    public String getNoteText() {
        return noteText;
    }
    public String getNoteDate() {
        return noteDate;
    }
    public String getNoteAuthor(){
        return noteAuthor;
    }
}
