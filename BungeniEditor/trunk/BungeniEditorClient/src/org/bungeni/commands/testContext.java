/*
 * testContext.java
 *
 * Created on December 21, 2007, 11:07 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.commands;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.chain.impl.ContextBase;

/**
 *
 * @author Administrator
 */
public class testContext extends ContextBase {
    private ArrayList<String> fields = new ArrayList<String>();
    private HashMap<String,String> keyValuePairs=new HashMap<String,String>();
    /** Creates a new instance of testContext */
    public testContext() {
    }

    public ArrayList<String> getFields() {
        return fields;
    }

    public void setFields(ArrayList<String> fields) {
        this.fields = fields;
    }

    public HashMap<String, String> getKeyValuePairs() {
        return keyValuePairs;
    }

    public void setKeyValuePairs(HashMap<String, String> keyValuePairs) {
        this.keyValuePairs = keyValuePairs;
    }
    
    
}
