/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Dave
 */
@Entity
@Table(name = "COMMITTEES")
@NamedQueries({
    @NamedQuery(name = "Committee.findAll", query = "SELECT c FROM Committee c"),
    @NamedQuery(name = "Committee.findById", query = "SELECT c FROM Committee c WHERE c.committeePK.id = :id"),
    @NamedQuery(name = "Committee.findByCommitteeName", query = "SELECT c FROM Committee c WHERE c.committeeName = :committeeName"),
    @NamedQuery(name = "Committee.findByCommitteeUri", query = "SELECT c FROM Committee c WHERE c.committeePK.committeeUri = :committeeUri"),
    @NamedQuery(name = "Committee.findByCountry", query = "SELECT c FROM Committee c WHERE c.country = :country")})
public class Committee implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CommitteePK committeePK;
    @Basic(optional = false)
    @Column(name = "COMMITTEE_NAME")
    private String committeeName;
    @Column(name = "COUNTRY")
    private String country;

    public Committee() {
    }

    public Committee(CommitteePK committeePK) {
        this.committeePK = committeePK;
    }

    public Committee(CommitteePK committeePK, String committeeName) {
        this.committeePK = committeePK;
        this.committeeName = committeeName;
    }

    public Committee(int id, String committeeUri) {
        this.committeePK = new CommitteePK(id, committeeUri);
    }

    public CommitteePK getCommitteePK() {
        return committeePK;
    }

    public void setCommitteePK(CommitteePK committeePK) {
        this.committeePK = committeePK;
    }

    public String getCommitteeName() {
        return committeeName;
    }

    public void setCommitteeName(String committeeName) {
        this.committeeName = committeeName;
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
        hash += (committeePK != null ? committeePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Committee)) {
            return false;
        }
        Committee other = (Committee) object;
        if ((this.committeePK == null && other.committeePK != null) || (this.committeePK != null && !this.committeePK.equals(other.committeePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.Committee[committeePK=" + committeePK + "]";
    }

}
