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
import org.bungeni.connector.entity.Committee;
import org.bungeni.connector.entity.CommitteePK;

/**
 *
 * @author Dave
 */
public class CommitteeJpaController {

    public CommitteeJpaController() {
        emf = Persistence.createEntityManagerFactory("BungeniConnectorPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Committee committee) throws PreexistingEntityException, Exception {
        if (committee.getCommitteePK() == null) {
            committee.setCommitteePK(new CommitteePK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(committee);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCommittee(committee.getCommitteePK()) != null) {
                throw new PreexistingEntityException("Committee " + committee + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Committee committee) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            committee = em.merge(committee);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CommitteePK id = committee.getCommitteePK();
                if (findCommittee(id) == null) {
                    throw new NonexistentEntityException("The committee with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CommitteePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Committee committee;
            try {
                committee = em.getReference(Committee.class, id);
                committee.getCommitteePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The committee with id " + id + " no longer exists.", enfe);
            }
            em.remove(committee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Committee> findCommitteeEntities() {
        return findCommitteeEntities(true, -1, -1);
    }

    public List<Committee> findCommitteeEntities(int maxResults, int firstResult) {
        return findCommitteeEntities(false, maxResults, firstResult);
    }

    private List<Committee> findCommitteeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Committee as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Committee findCommittee(CommitteePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Committee.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommitteeCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Committee as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
