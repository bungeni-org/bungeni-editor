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
import org.bungeni.connector.entity.JudgementJudge;
import org.bungeni.connector.entity.JudgementJudgePK;

/**
 *
 * @author Dave
 */
public class JudgementJudgeJpaController {

    public JudgementJudgeJpaController() {
        emf = Persistence.createEntityManagerFactory("BungeniConnectorPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(JudgementJudge judgementJudge) throws PreexistingEntityException, Exception {
        if (judgementJudge.getJudgementJudgePK() == null) {
            judgementJudge.setJudgementJudgePK(new JudgementJudgePK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(judgementJudge);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJudgementJudge(judgementJudge.getJudgementJudgePK()) != null) {
                throw new PreexistingEntityException("JudgementJudge " + judgementJudge + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(JudgementJudge judgementJudge) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            judgementJudge = em.merge(judgementJudge);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                JudgementJudgePK id = judgementJudge.getJudgementJudgePK();
                if (findJudgementJudge(id) == null) {
                    throw new NonexistentEntityException("The judgementJudge with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(JudgementJudgePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JudgementJudge judgementJudge;
            try {
                judgementJudge = em.getReference(JudgementJudge.class, id);
                judgementJudge.getJudgementJudgePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The judgementJudge with id " + id + " no longer exists.", enfe);
            }
            em.remove(judgementJudge);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<JudgementJudge> findJudgementJudgeEntities() {
        return findJudgementJudgeEntities(true, -1, -1);
    }

    public List<JudgementJudge> findJudgementJudgeEntities(int maxResults, int firstResult) {
        return findJudgementJudgeEntities(false, maxResults, firstResult);
    }

    private List<JudgementJudge> findJudgementJudgeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from JudgementJudge as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public JudgementJudge findJudgementJudge(JudgementJudgePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(JudgementJudge.class, id);
        } finally {
            em.close();
        }
    }

    public int getJudgementJudgeCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from JudgementJudge as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
