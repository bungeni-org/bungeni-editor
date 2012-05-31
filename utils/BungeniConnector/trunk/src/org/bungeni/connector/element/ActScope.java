
package org.bungeni.connector.element;

import java.util.*;

public class ActScope {
    
    public static final String PACKAGE_ALIAS = "actScopes";
    public static final String CLASS_ALIAS = "actScope";
    private Integer id;
    private List name = new ArrayList();

    public ActScope() {
    }

    public ActScope(Integer id, Name name) {
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
 

