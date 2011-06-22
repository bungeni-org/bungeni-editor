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
@Table(name = "PARTIES")
@NamedQueries({
    @NamedQuery(name = "Party.findAll", query = "SELECT p FROM Party p"),
    @NamedQuery(name = "Party.findById", query = "SELECT p FROM Party p WHERE p.partyPK.id = :id"),
    @NamedQuery(name = "Party.findByPartyName", query = "SELECT p FROM Party p WHERE p.partyPK.partyName = :partyName"),
    @NamedQuery(name = "Party.findByPartyType", query = "SELECT p FROM Party p WHERE p.partyType = :partyType")})
public class Party implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PartyPK partyPK;
    @Basic(optional = false)
    @Column(name = "PARTY_TYPE")
    private String partyType;

    public Party() {
    }

    public Party(PartyPK partyPK) {
        this.partyPK = partyPK;
    }

    public Party(PartyPK partyPK, String partyType) {
        this.partyPK = partyPK;
        this.partyType = partyType;
    }

    public Party(String id, String partyName) {
        this.partyPK = new PartyPK(id, partyName);
    }

    public PartyPK getPartyPK() {
        return partyPK;
    }

    public void setPartyPK(PartyPK partyPK) {
        this.partyPK = partyPK;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partyPK != null ? partyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Party)) {
            return false;
        }
        Party other = (Party) object;
        if ((this.partyPK == null && other.partyPK != null) || (this.partyPK != null && !this.partyPK.equals(other.partyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.Party[partyPK=" + partyPK + "]";
    }

}
