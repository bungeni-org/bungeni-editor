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
import org.bungeni.connector.entity.Judgement;
import org.bungeni.connector.entity.JudgementPK;

/**
 *
 * @author Dave
 */
public class JudgementJpaController {

    public JudgementJpaController() {
        emf = Persistence.createEntityManagerFactory("BungeniConnectorPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Judgement judgement) throws PreexistingEntityException, Exception {
        if (judgement.getJudgementPK() == null) {
            judgement.setJudgementPK(new JudgementPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(judgement);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJudgement(judgement.getJudgementPK()) != null) {
                throw new PreexistingEntityException("Judgement " + judgement + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Judgement judgement) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            judgement = em.merge(judgement);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                JudgementPK id = judgement.getJudgementPK();
                if (findJudgement(id) == null) {
                    throw new NonexistentEntityException("The judgement with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(JudgementPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Judgement judgement;
            try {
                judgement = em.getReference(Judgement.class, id);
                judgement.getJudgementPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The judgement with id " + id + " no longer exists.", enfe);
            }
            em.remove(judgement);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Judgement> findJudgementEntities() {
        return findJudgementEntities(true, -1, -1);
    }

    public List<Judgement> findJudgementEntities(int maxResults, int firstResult) {
        return findJudgementEntities(false, maxResults, firstResult);
    }

    private List<Judgement> findJudgementEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Judgement as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Judgement findJudgement(JudgementPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Judgement.class, id);
        } finally {
            em.close();
        }
    }

    public int getJudgementCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Judgement as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
