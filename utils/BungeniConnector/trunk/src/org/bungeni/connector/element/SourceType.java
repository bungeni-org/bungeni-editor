
package org.bungeni.connector.element;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dave
 */
public class SourceType {

    private Integer id;
    private List name = new ArrayList();
    public static final String PACKAGE_ALIAS = "sourceTypes";
    public static final String CLASS_ALIAS = "sourceType";
    
    public SourceType() {
    }

    public SourceType(Integer id, Name name) {
        this.id = id;
        this.name.add(name);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List getNames() {
        return name;
    }

    public void addName(Name name) {
        this.name.add(name);
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