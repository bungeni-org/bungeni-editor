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
@Table(name = "BILLS")
@NamedQueries({
    @NamedQuery(name = "Bill.findAll", query = "SELECT b FROM Bill b"),
    @NamedQuery(name = "Bill.findById", query = "SELECT b FROM Bill b WHERE b.id = :id"),
    @NamedQuery(name = "Bill.findByBillName", query = "SELECT b FROM Bill b WHERE b.name = :name"),
    @NamedQuery(name = "Bill.findByBillUri", query = "SELECT b FROM Bill b WHERE b.uri = :uri"),
    @NamedQuery(name = "Bill.findByBillOntology", query = "SELECT b FROM Bill b WHERE b.ontology = :ontology"),
    @NamedQuery(name = "Bill.findByCountry", query = "SELECT b FROM Bill b WHERE b.country = :country")})
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "BILL_NAME")
    private String name;
    @Id
    @Basic(optional = false)
    @Column(name = "BILL_URI")
    private String uri;
    @Basic(optional = false)
    @Column(name = "BILL_ONTOLOGY")
    private String ontology;
    @Column(name = "COUNTRY")
    private String country;
    public static final String BILL_ALIAS = "bill";
    public Bill() {
    }

    public Bill(String uri) {
        this.uri = uri;
    }

    public Bill(String uri, String name, String ontology) {
        this.uri = uri;
        this.name = name;
        this.ontology = ontology;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillName() {
        return name;
    }

    public void setBillName(String name) {
        this.name = name;
    }

    public String getBillUri() {
        return uri;
    }

    public void setBillUri(String uri) {
        this.uri = uri;
    }

    public String getBillOntology() {
        return ontology;
    }

    public void setBillOntology(String ontology) {
        this.ontology = ontology;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uri != null ? uri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bill)) {
            return false;
        }
        Bill other = (Bill) object;
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.Bill[uri=" + uri + "]";
    }
}
