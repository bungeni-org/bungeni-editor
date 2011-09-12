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
    private String title;
    private String date;
    private String source;
    private String uri;
    private String sitting;
    public static final String PACKAGE_ALIAS = "documents";
    public static final String CLASS_ALIAS = "document";

    public Document() {
    }

    public Document(String id, String title, String date, String source, String uri, String sitting) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.source = source;
        this.uri = uri;
        this.sitting = sitting;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
