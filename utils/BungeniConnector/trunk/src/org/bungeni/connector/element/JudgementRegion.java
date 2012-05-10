
package org.bungeni.connector.element;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  Ameen Taha
 */
public class JudgementRegion {

    private Integer id;
    private List name = new ArrayList();
    private String uri;
    private String ontology;
    private String country;
    public static final String PACKAGE_ALIAS = "judgementRegions";
    public static final String CLASS_ALIAS = "judgementRegion";
    
    public JudgementRegion() {
    }

    public JudgementRegion(Integer id, Name name, String uri, String ontology, String country) {
        this.id = id;
        this.name.add(name);
        this.uri = uri;
        this.ontology = ontology;
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
