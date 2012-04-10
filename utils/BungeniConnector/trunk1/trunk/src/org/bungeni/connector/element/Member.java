package org.bungeni.connector.element;


/**
 *
 * @author Dave
 */

public class Member{
    private int id;
    private String uri;
    private String first;
    private String last;
    private String role;
    public static final String PACKAGE_ALIAS = "members";
    public static final String CLASS_ALIAS = "member";
    public Member() {
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
