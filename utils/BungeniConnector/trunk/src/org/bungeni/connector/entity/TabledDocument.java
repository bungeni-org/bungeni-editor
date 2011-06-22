/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Dave
 */
@Entity
@Table(name = "TABLED_DOCUMENTS")
@NamedQueries({
    @NamedQuery(name = "TabledDocument.findAll", query = "SELECT t FROM TabledDocument t"),
    @NamedQuery(name = "TabledDocument.findById", query = "SELECT t FROM TabledDocument t WHERE t.id = :id"),
    @NamedQuery(name = "TabledDocument.findByDocumentTitle", query = "SELECT t FROM TabledDocument t WHERE t.documentTitle = :documentTitle"),
    @NamedQuery(name = "TabledDocument.findByDocumentDate", query = "SELECT t FROM TabledDocument t WHERE t.documentDate = :documentDate"),
    @NamedQuery(name = "TabledDocument.findByDocumentSource", query = "SELECT t FROM TabledDocument t WHERE t.documentSource = :documentSource"),
    @NamedQuery(name = "TabledDocument.findByDocumentUri", query = "SELECT t FROM TabledDocument t WHERE t.documentUri = :documentUri"),
    @NamedQuery(name = "TabledDocument.findBySittingId", query = "SELECT t FROM TabledDocument t WHERE t.sittingId = :sittingId")})
public class TabledDocument implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DOCUMENT_TITLE")
    private String documentTitle;
    @Column(name = "DOCUMENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date documentDate;
    @Column(name = "DOCUMENT_SOURCE")
    private String documentSource;
    @Column(name = "DOCUMENT_URI")
    private String documentUri;
    @Column(name = "SITTING_ID")
    private String sittingId;

    public TabledDocument() {
    }

    public TabledDocument(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentSource() {
        return documentSource;
    }

    public void setDocumentSource(String documentSource) {
        this.documentSource = documentSource;
    }

    public String getDocumentUri() {
        return documentUri;
    }

    public void setDocumentUri(String documentUri) {
        this.documentUri = documentUri;
    }

    public String getSittingId() {
        return sittingId;
    }

    public void setSittingId(String sittingId) {
        this.sittingId = sittingId;
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
        if (!(object instanceof TabledDocument)) {
            return false;
        }
        TabledDocument other = (TabledDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.TabledDocument[id=" + id + "]";
    }

}
