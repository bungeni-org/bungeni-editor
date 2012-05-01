/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.element;

import java.util.Locale;


/**
 *
 * @author bzuadmin
 */
public class Name {
    public static final String PACKAGE_ALIAS = "names";
    public static final String CLASS_ALIAS = "name";

    private String lang;
    private String value;

    public Name(){
    }
     
    public Name(String lang, String value){
        this.lang = lang;
        this.value = value;
    }
    
    public Name(String value){
        this.lang = Locale.getDefault().getLanguage();
        this.value = value;
    }
     

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLang() {
            return lang;
    }

    public String getValue() {
            return value;
    }
    
  
}

