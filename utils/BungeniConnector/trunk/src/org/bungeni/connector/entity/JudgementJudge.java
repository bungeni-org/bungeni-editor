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
@Table(name = "JUDGEMENT_JUDGES")
@NamedQueries({
    @NamedQuery(name = "JudgementJudge.findAll", query = "SELECT j FROM JudgementJudge j"),
    @NamedQuery(name = "JudgementJudge.findByJudgeId", query = "SELECT j FROM JudgementJudge j WHERE j.judgementJudgePK.judgeId = :judgeId"),
    @NamedQuery(name = "JudgementJudge.findByJudgementId", query = "SELECT j FROM JudgementJudge j WHERE j.judgementJudgePK.judgementId = :judgementId")})
public class JudgementJudge implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected JudgementJudgePK judgementJudgePK;

    public JudgementJudge() {
    }

    public JudgementJudge(JudgementJudgePK judgementJudgePK) {
        this.judgementJudgePK = judgementJudgePK;
    }

    public JudgementJudge(int judgeId, String judgementId) {
        this.judgementJudgePK = new JudgementJudgePK(judgeId, judgementId);
    }

    public JudgementJudgePK getJudgementJudgePK() {
        return judgementJudgePK;
    }

    public void setJudgementJudgePK(JudgementJudgePK judgementJudgePK) {
        this.judgementJudgePK = judgementJudgePK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (judgementJudgePK != null ? judgementJudgePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JudgementJudge)) {
            return false;
        }
        JudgementJudge other = (JudgementJudge) object;
        if ((this.judgementJudgePK == null && other.judgementJudgePK != null) || (this.judgementJudgePK != null && !this.judgementJudgePK.equals(other.judgementJudgePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.JudgementJudge[judgementJudgePK=" + judgementJudgePK + "]";
    }

}
