/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.frbr;

/**
 *
 * @author undesa
 */
public class FRBRentity {
    
    class FRBRAuthor {
        String Name;
        String URI;
    }
    FRBRAuthor Author;
    
    class FRBRDate {
        String CalendarDate;
        String DateName;
    }
    FRBRDate Date;
    String FRBRfullURI;
}
