/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import java.util.ArrayList;
import java.util.Date;

/**
 * Structure converted to Xml
 * @author Ashok
 */
public class StructuralErrorLog {
    public String sourceFile;
    public Date timeStamp;
    public ArrayList<StructuralError> structuralErrors = new ArrayList<StructuralError>(0);
}
