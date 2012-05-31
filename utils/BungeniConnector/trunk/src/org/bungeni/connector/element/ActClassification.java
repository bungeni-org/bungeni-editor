/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.element;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bzuadmin
 */
public class ActClassification {
     public static final String PACKAGE_ALIAS = "actClassifications";
    public static final String CLASS_ALIAS = "actClassification";
    private Integer id;
    private List name = new ArrayList();

    public ActClassification() {
    }

    public ActClassification(Integer id, Name name) {
        this.id = id;
        this.name.add(name);
    }

    public Integer getId() {
            return this.id;
    }

    public void setId(Integer id) {
            this.id = id;
    }

    public void addName(Name name) {
        this.name.add(name);
    }
    
    public List getNames() {
            return name;
    }
    
    public String getNameByLang(String lang) {
         String value=null;
         for(Name objName : (List<Name>)name){
             if(objName.getLang().equalsIgnoreCase(lang)){
                 value = objName.getValue();
                 break;
             }
         }
            return value;
    }
}
