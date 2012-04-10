
package org.bungeni.connector.element;

/**
 *
 * @author Dave
 */
public class Act {

    private Integer id;
    private String name;
    public static final String PACKAGE_ALIAS = "acts";
    public static final String CLASS_ALIAS = "act";
    
    public Act() {
    }

    public Act(Integer id, String name) {
        this.id = id;
        this.name = name;
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
}