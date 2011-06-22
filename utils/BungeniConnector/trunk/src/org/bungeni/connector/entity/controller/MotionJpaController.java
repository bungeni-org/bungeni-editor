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
import org.bungeni.connector.entity.Motion;
import org.bungeni.connector.entity.MotionPK;

/**
 *
 * @author Dave
 */
public class MotionJpaController {

    public MotionJpaController() {
        emf = Persistence.createEntityManagerFactory("BungeniConnectorPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Motion motion) throws PreexistingEntityException, Exception {
        if (motion.getMotionPK() == null) {
            motion.setMotionPK(new MotionPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(motion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMotion(motion.getMotionPK()) != null) {
                throw new PreexistingEntityException("Motion " + motion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Motion motion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            motion = em.merge(motion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MotionPK id = motion.getMotionPK();
                if (findMotion(id) == null) {
                    throw new NonexistentEntityException("The motion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MotionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Motion motion;
            try {
                motion = em.getReference(Motion.class, id);
                motion.getMotionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The motion with id " + id + " no longer exists.", enfe);
            }
            em.remove(motion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Motion> findMotionEntities() {
        return findMotionEntities(true, -1, -1);
    }

    public List<Motion> findMotionEntities(int maxResults, int firstResult) {
        return findMotionEntities(false, maxResults, firstResult);
    }

    private List<Motion> findMotionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Motion as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Motion findMotion(MotionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Motion.class, id);
        } finally {
            em.close();
        }
    }

    public int getMotionCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Motion as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
