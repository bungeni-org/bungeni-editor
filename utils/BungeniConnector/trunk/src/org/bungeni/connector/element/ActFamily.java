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
public class ActFamily {
    public static final String PACKAGE_ALIAS = "actFamilies";
    public static final String CLASS_ALIAS = "actFamily";
    private Integer id;
    private List name = new ArrayList();
    private List subFamily = new ArrayList();

    public ActFamily() {
    }

    public ActFamily(Integer id, Name name, SubFamily subFamily) {
        this.id = id;
        this.name.add(name);
        this.subFamily.add(subFamily);
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
    
    public void addSubFamily(SubFamily subFamily) {
        this.subFamily.add(subFamily);
    }
    
    public List getSubFamilies() {
            return subFamily;
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
    
    public String getSubFamilyByLang(String lang) {
         String value=null;
         for(SubFamily objSubFamily : (List<SubFamily>)subFamily){
             if(objSubFamily.getLang().equalsIgnoreCase(lang)){
                 value = objSubFamily.getValue();
                 break;
             }
         }
            return value;
    }
    
     public List<SubFamily> getSubFamiliesByLang(String lang) {
         List<SubFamily> subFamiliesList= new ArrayList<SubFamily>();
         for(SubFamily objSubFamily : (List<SubFamily>)subFamily){
             if(objSubFamily.getLang().equalsIgnoreCase(lang)){
                 subFamiliesList.add(objSubFamily);
             }
         }
            return subFamiliesList;
    }
    
}
