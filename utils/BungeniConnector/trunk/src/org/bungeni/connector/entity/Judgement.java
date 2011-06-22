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
@Table(name = "JUDGEMENTS")
@NamedQueries({
    @NamedQuery(name = "Judgement.findAll", query = "SELECT j FROM Judgement j"),
    @NamedQuery(name = "Judgement.findById", query = "SELECT j FROM Judgement j WHERE j.judgementPK.id = :id"),
    @NamedQuery(name = "Judgement.findByName", query = "SELECT j FROM Judgement j WHERE j.judgementPK.name = :name"),
    @NamedQuery(name = "Judgement.findByJudgementDate", query = "SELECT j FROM Judgement j WHERE j.judgementDate = :judgementDate"),
    @NamedQuery(name = "Judgement.findByHearingDate", query = "SELECT j FROM Judgement j WHERE j.hearingDate = :hearingDate")})
public class Judgement implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected JudgementPK judgementPK;
    @Basic(optional = false)
    @Column(name = "JUDGEMENT_DATE")
    private String judgementDate;
    @Basic(optional = false)
    @Column(name = "HEARING_DATE")
    private String hearingDate;

    public Judgement() {
    }

    public Judgement(JudgementPK judgementPK) {
        this.judgementPK = judgementPK;
    }

    public Judgement(JudgementPK judgementPK, String judgementDate, String hearingDate) {
        this.judgementPK = judgementPK;
        this.judgementDate = judgementDate;
        this.hearingDate = hearingDate;
    }

    public Judgement(String id, String name) {
        this.judgementPK = new JudgementPK(id, name);
    }

    public JudgementPK getJudgementPK() {
        return judgementPK;
    }

    public void setJudgementPK(JudgementPK judgementPK) {
        this.judgementPK = judgementPK;
    }

    public String getJudgementDate() {
        return judgementDate;
    }

    public void setJudgementDate(String judgementDate) {
        this.judgementDate = judgementDate;
    }

    public String getHearingDate() {
        return hearingDate;
    }

    public void setHearingDate(String hearingDate) {
        this.hearingDate = hearingDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (judgementPK != null ? judgementPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Judgement)) {
            return false;
        }
        Judgement other = (Judgement) object;
        if ((this.judgementPK == null && other.judgementPK != null) || (this.judgementPK != null && !this.judgementPK.equals(other.judgementPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.Judgement[judgementPK=" + judgementPK + "]";
    }

}
