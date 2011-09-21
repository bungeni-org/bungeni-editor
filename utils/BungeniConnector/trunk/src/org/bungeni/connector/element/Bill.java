
package org.bungeni.connector.element;

/**
 *
 * @author Dave
 */
public class Bill {

    private Integer id;
    private String name;
    private String uri;
    private String ontology;
    private String country;
    public static final String PACKAGE_ALIAS = "bills";
    public static final String CLASS_ALIAS = "bill";
    public Bill() {
    }

    public Bill(Integer id, String name, String uri, String ontology, String country) {
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
