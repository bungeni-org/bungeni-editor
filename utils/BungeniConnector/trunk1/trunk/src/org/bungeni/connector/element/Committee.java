package org.bungeni.connector.element;


/**
 * This class is used to serialise the data from the
 * COMMITTEES table using the XStream library and the attributes
 * in this class match the fields in the COMMITTEES table
 * @author Reagan Mbitiru
 * 17-01-2012
 */

public class Committee{

    private String id;
    private String name;
    private String uri;
    private String country;

    public static final String PACKAGE_ALIAS = "committees";
    public static final String CLASS_ALIAS = "committee";
    public Committee() {
    }

    public Committee(String id, String name, String uri, String country) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String from) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getURI() {
        return uri;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
