/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.entity.controller;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import org.bungeni.connector.entity.controller.exceptions.NonexistentEntityException;
import org.bungeni.connector.entity.controller.exceptions.PreexistingEntityException;
import org.bungeni.connector.entity.JudgementParty;
import org.bungeni.connector.entity.JudgementPartyPK;

/**
 *
 * @author Dave
 */
public class JudgementPartyJpaController {

    public JudgementPartyJpaController() {
        emf = Persistence.createEntityManagerFactory("BungeniConnectorPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(JudgementParty judgementParty) throws PreexistingEntityException, Exception {
        if (judgementParty.getJudgementPartyPK() == null) {
            judgementParty.setJudgementPartyPK(new JudgementPartyPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(judgementParty);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJudgementParty(judgementParty.getJudgementPartyPK()) != null) {
                throw new PreexistingEntityException("JudgementParty " + judgementParty + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(JudgementParty judgementParty) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            judgementParty = em.merge(judgementParty);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                JudgementPartyPK id = judgementParty.getJudgementPartyPK();
                if (findJudgementParty(id) == null) {
                    throw new NonexistentEntityException("The judgementParty with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(JudgementPartyPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JudgementParty judgementParty;
            try {
                judgementParty = em.getReference(JudgementParty.class, id);
                judgementParty.getJudgementPartyPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The judgementParty with id " + id + " no longer exists.", enfe);
            }
            em.remove(judgementParty);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<JudgementParty> findJudgementPartyEntities() {
        return findJudgementPartyEntities(true, -1, -1);
    }

    public List<JudgementParty> findJudgementPartyEntities(int maxResults, int firstResult) {
        return findJudgementPartyEntities(false, maxResults, firstResult);
    }

    private List<JudgementParty> findJudgementPartyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from JudgementParty as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public JudgementParty findJudgementParty(JudgementPartyPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(JudgementParty.class, id);
        } finally {
            em.close();
        }
    }

    public int getJudgementPartyCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from JudgementParty as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
