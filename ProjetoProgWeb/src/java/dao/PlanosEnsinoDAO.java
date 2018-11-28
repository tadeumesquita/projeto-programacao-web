/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Disciplinas;
import model.PlanosEnsino;

/**
 *
 * @author jscatena
 */
public class PlanosEnsinoDAO implements Serializable {

    public PlanosEnsinoDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlanosEnsino planosEnsino) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Disciplinas fkDisciplina = planosEnsino.getFkDisciplina();
            if (fkDisciplina != null) {
                fkDisciplina = em.getReference(fkDisciplina.getClass(), fkDisciplina.getId());
                planosEnsino.setFkDisciplina(fkDisciplina);
            }
            em.persist(planosEnsino);
            if (fkDisciplina != null) {
                fkDisciplina.getPlanosEnsinoList().add(planosEnsino);
                fkDisciplina = em.merge(fkDisciplina);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlanosEnsino planosEnsino) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PlanosEnsino persistentPlanosEnsino = em.find(PlanosEnsino.class, planosEnsino.getId());
            Disciplinas fkDisciplinaOld = persistentPlanosEnsino.getFkDisciplina();
            Disciplinas fkDisciplinaNew = planosEnsino.getFkDisciplina();
            if (fkDisciplinaNew != null) {
                fkDisciplinaNew = em.getReference(fkDisciplinaNew.getClass(), fkDisciplinaNew.getId());
                planosEnsino.setFkDisciplina(fkDisciplinaNew);
            }
            planosEnsino = em.merge(planosEnsino);
            if (fkDisciplinaOld != null && !fkDisciplinaOld.equals(fkDisciplinaNew)) {
                fkDisciplinaOld.getPlanosEnsinoList().remove(planosEnsino);
                fkDisciplinaOld = em.merge(fkDisciplinaOld);
            }
            if (fkDisciplinaNew != null && !fkDisciplinaNew.equals(fkDisciplinaOld)) {
                fkDisciplinaNew.getPlanosEnsinoList().add(planosEnsino);
                fkDisciplinaNew = em.merge(fkDisciplinaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = planosEnsino.getId();
                if (findPlanosEnsino(id) == null) {
                    throw new NonexistentEntityException("The planosEnsino with id " + id + " no longer exists.");
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
            PlanosEnsino planosEnsino;
            try {
                planosEnsino = em.getReference(PlanosEnsino.class, id);
                planosEnsino.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The planosEnsino with id " + id + " no longer exists.", enfe);
            }
            Disciplinas fkDisciplina = planosEnsino.getFkDisciplina();
            if (fkDisciplina != null) {
                fkDisciplina.getPlanosEnsinoList().remove(planosEnsino);
                fkDisciplina = em.merge(fkDisciplina);
            }
            em.remove(planosEnsino);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PlanosEnsino> findPlanosEnsinoEntities() {
        return findPlanosEnsinoEntities(true, -1, -1);
    }

    public List<PlanosEnsino> findPlanosEnsinoEntities(int maxResults, int firstResult) {
        return findPlanosEnsinoEntities(false, maxResults, firstResult);
    }

    private List<PlanosEnsino> findPlanosEnsinoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlanosEnsino.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PlanosEnsino findPlanosEnsino(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlanosEnsino.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlanosEnsinoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlanosEnsino> rt = cq.from(PlanosEnsino.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
