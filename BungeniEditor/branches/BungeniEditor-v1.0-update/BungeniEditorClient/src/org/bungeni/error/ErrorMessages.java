/*
 * ErrorMessages.java
 *
 * Created on December 12, 2007, 3:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.error;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class ErrorMessages {
    private ArrayList<String> errorMessages= new ArrayList<String>(0);
    
    /** Creates a new instance of ErrorMessages */
    public ErrorMessages() {
        
    }
    
    public void add(String message) {
        errorMessages.add(message);    
    }

    public void start(String message) {
        errorMessages.clear();
        errorMessages.add(message);
    }
    
    public String toString(){
        StringBuffer msgs = new StringBuffer();
        Iterator<String> msgIterator = errorMessages.iterator();
        while (msgIterator.hasNext()) {
            msgs.append(msgIterator.next()+"\n");
        }
        return msgs.toString();
    }
}
