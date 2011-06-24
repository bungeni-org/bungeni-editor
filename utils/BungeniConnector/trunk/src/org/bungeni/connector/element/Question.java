/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.element;


/**
 *
 * @author Dave
 */

public class Question{

    private Integer id;
    private String title;
    private String from;
    private String to;
    private String text;
    public static final String PACKAGE_ALIAS = "questions";
    public static final String CLASS_ALIAS = "question";
    public Question() {
    }

    public Question(Integer id, String title, String from, String to, String text) {
        this.id = id;
        this.title = title;
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
