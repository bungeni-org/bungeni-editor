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
import org.bungeni.connector.entity.Judge;

/**
 *
 * @author Dave
 */
public class JudgeJpaController {

    public JudgeJpaController() {
        emf = Persistence.createEntityManagerFactory("BungeniConnectorPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Judge judge) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(judge);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJudge(judge.getId()) != null) {
                throw new PreexistingEntityException("Judge " + judge + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Judge judge) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            judge = em.merge(judge);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = judge.getId();
                if (findJudge(id) == null) {
                    throw new NonexistentEntityException("The judge with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Judge judge;
            try {
                judge = em.getReference(Judge.class, id);
                judge.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The judge with id " + id + " no longer exists.", enfe);
            }
            em.remove(judge);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Judge> findJudgeEntities() {
        return findJudgeEntities(true, -1, -1);
    }

    public List<Judge> findJudgeEntities(int maxResults, int firstResult) {
        return findJudgeEntities(false, maxResults, firstResult);
    }

    private List<Judge> findJudgeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Judge as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Judge findJudge(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Judge.class, id);
        } finally {
            em.close();
        }
    }

    public int getJudgeCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Judge as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
