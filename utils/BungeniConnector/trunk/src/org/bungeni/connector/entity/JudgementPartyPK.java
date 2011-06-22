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
public class JudgementPartyPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "JUDGEMENT_ID")
    private String judgementId;
    @Basic(optional = false)
    @Column(name = "PARTY_ID")
    private String partyId;

    public JudgementPartyPK() {
    }

    public JudgementPartyPK(String judgementId, String partyId) {
        this.judgementId = judgementId;
        this.partyId = partyId;
    }

    public String getJudgementId() {
        return judgementId;
    }

    public void setJudgementId(String judgementId) {
        this.judgementId = judgementId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (judgementId != null ? judgementId.hashCode() : 0);
        hash += (partyId != null ? partyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JudgementPartyPK)) {
            return false;
        }
        JudgementPartyPK other = (JudgementPartyPK) object;
        if ((this.judgementId == null && other.judgementId != null) || (this.judgementId != null && !this.judgementId.equals(other.judgementId))) {
            return false;
        }
        if ((this.partyId == null && other.partyId != null) || (this.partyId != null && !this.partyId.equals(other.partyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.JudgementPartyPK[judgementId=" + judgementId + ", partyId=" + partyId + "]";
    }

}
