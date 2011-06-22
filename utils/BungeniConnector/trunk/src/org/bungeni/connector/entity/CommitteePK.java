/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Dave
 */
@Embeddable
public class CommitteePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;
    @Basic(optional = false)
    @Column(name = "COMMITTEE_URI")
    private String committeeUri;

    public CommitteePK() {
    }

    public CommitteePK(int id, String committeeUri) {
        this.id = id;
        this.committeeUri = committeeUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommitteeUri() {
        return committeeUri;
    }

    public void setCommitteeUri(String committeeUri) {
        this.committeeUri = committeeUri;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (committeeUri != null ? committeeUri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommitteePK)) {
            return false;
        }
        CommitteePK other = (CommitteePK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.committeeUri == null && other.committeeUri != null) || (this.committeeUri != null && !this.committeeUri.equals(other.committeeUri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.CommitteePK[id=" + id + ", committeeUri=" + committeeUri + "]";
    }

}
