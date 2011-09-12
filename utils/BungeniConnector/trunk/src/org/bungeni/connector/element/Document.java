/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.element;

/**
 *
 * @author Dave
 */
public class Document {
    private String id;
    private String uri;
    private String date;
    private String title;
    private String name;
    private String source;
    private String sitting;
    public static final String PACKAGE_ALIAS = "documents";
    public static final String CLASS_ALIAS = "document";

    public Document() {
    }
    
    public Document(String id, String date, String title, String name, String source, String sitting) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.name = name;
        this.source = source;
        this.sitting = sitting;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSitting() {
        return sitting;
    }

    public void setSitting(String sitting) {
        this.sitting = sitting;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
