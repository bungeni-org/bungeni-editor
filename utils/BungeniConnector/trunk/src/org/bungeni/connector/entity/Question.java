/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Dave
 */
@Entity
@Table(name = "QUESTIONS")
@NamedQueries({
    @NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q"),
    @NamedQuery(name = "Question.findById", query = "SELECT q FROM Question q WHERE q.id = :id"),
    @NamedQuery(name = "Question.findByQuestionTitle", query = "SELECT q FROM Question q WHERE q.title = :title"),
    @NamedQuery(name = "Question.findByQuestionFrom", query = "SELECT q FROM Question q WHERE q.from = :from"),
    @NamedQuery(name = "Question.findByQuestionTo", query = "SELECT q FROM Question q WHERE q.to = :to"),
    @NamedQuery(name = "Question.findByQuestionText", query = "SELECT q FROM Question q WHERE q.text = :text")})
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QUESTION_TITLE")
    private String title;
    @Column(name = "QUESTION_FROM")
    private String from;
    @Column(name = "QUESTION_TO")
    private String to;
    @Column(name = "QUESTION_TEXT")
    private String text;
    public static final String QUESTION_ALIAS = "question";
    public Question() {
    }

    public Question(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionTitle() {
        return title;
    }

    public void setQuestionTitle(String title) {
        this.title = title;
    }

    public String getQuestionFrom() {
        return from;
    }

    public void setQuestionFrom(String from) {
        this.from = from;
    }

    public String getQuestionTo() {
        return to;
    }

    public void setQuestionTo(String to) {
        this.to = to;
    }

    public String getQuestionText() {
        return text;
    }

    public void setQuestionText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.Question[id=" + id + "]";
    }

}
