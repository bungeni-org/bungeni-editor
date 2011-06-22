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
@Table(name = "JUDGES")
@NamedQueries({
    @NamedQuery(name = "Judge.findAll", query = "SELECT j FROM Judge j"),
    @NamedQuery(name = "Judge.findById", query = "SELECT j FROM Judge j WHERE j.id = :id"),
    @NamedQuery(name = "Judge.findByFirstName", query = "SELECT j FROM Judge j WHERE j.firstName = :firstName"),
    @NamedQuery(name = "Judge.findByLastName", query = "SELECT j FROM Judge j WHERE j.lastName = :lastName"),
    @NamedQuery(name = "Judge.findByUri", query = "SELECT j FROM Judge j WHERE j.uri = :uri")})
public class Judge implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Basic(optional = false)
    @Column(name = "URI")
    private String uri;

    public Judge() {
    }

    public Judge(Integer id) {
        this.id = id;
    }

    public Judge(Integer id, String uri) {
        this.id = id;
        this.uri = uri;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
        if (!(object instanceof Judge)) {
            return false;
        }
        Judge other = (Judge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.Judge[id=" + id + "]";
    }

}
