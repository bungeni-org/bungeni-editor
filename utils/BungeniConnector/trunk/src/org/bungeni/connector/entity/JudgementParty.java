/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.entity;

import java.io.Serializable;
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
@Table(name = "JUDGEMENT_PARTIES")
@NamedQueries({
    @NamedQuery(name = "JudgementParty.findAll", query = "SELECT j FROM JudgementParty j"),
    @NamedQuery(name = "JudgementParty.findByJudgementId", query = "SELECT j FROM JudgementParty j WHERE j.judgementPartyPK.judgementId = :judgementId"),
    @NamedQuery(name = "JudgementParty.findByPartyId", query = "SELECT j FROM JudgementParty j WHERE j.judgementPartyPK.partyId = :partyId")})
public class JudgementParty implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected JudgementPartyPK judgementPartyPK;

    public JudgementParty() {
    }

    public JudgementParty(JudgementPartyPK judgementPartyPK) {
        this.judgementPartyPK = judgementPartyPK;
    }

    public JudgementParty(String judgementId, String partyId) {
        this.judgementPartyPK = new JudgementPartyPK(judgementId, partyId);
    }

    public JudgementPartyPK getJudgementPartyPK() {
        return judgementPartyPK;
    }

    public void setJudgementPartyPK(JudgementPartyPK judgementPartyPK) {
        this.judgementPartyPK = judgementPartyPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (judgementPartyPK != null ? judgementPartyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JudgementParty)) {
            return false;
        }
        JudgementParty other = (JudgementParty) object;
        if ((this.judgementPartyPK == null && other.judgementPartyPK != null) || (this.judgementPartyPK != null && !this.judgementPartyPK.equals(other.judgementPartyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.JudgementParty[judgementPartyPK=" + judgementPartyPK + "]";
    }

}
