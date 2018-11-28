/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Disciplinas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Cursos;

/**
 *
 * @author tadeumesquita
 */
public class CursosDAO implements Serializable {

    public CursosDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cursos cursos) {
        if (cursos.getDisciplinasCollection() == null) {
            cursos.setDisciplinasCollection(new ArrayList<Disciplinas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Disciplinas> attachedDisciplinasCollection = new ArrayList<Disciplinas>();
            for (Disciplinas disciplinasCollectionDisciplinasToAttach : cursos.getDisciplinasCollection()) {
                disciplinasCollectionDisciplinasToAttach = em.getReference(disciplinasCollectionDisciplinasToAttach.getClass(), disciplinasCollectionDisciplinasToAttach.getId());
                attachedDisciplinasCollection.add(disciplinasCollectionDisciplinasToAttach);
            }
            cursos.setDisciplinasCollection(attachedDisciplinasCollection);
            em.persist(cursos);
            for (Disciplinas disciplinasCollectionDisciplinas : cursos.getDisciplinasCollection()) {
                Cursos oldFkCursoOfDisciplinasCollectionDisciplinas = disciplinasCollectionDisciplinas.getFkCurso();
                disciplinasCollectionDisciplinas.setFkCurso(cursos);
                disciplinasCollectionDisciplinas = em.merge(disciplinasCollectionDisciplinas);
                if (oldFkCursoOfDisciplinasCollectionDisciplinas != null) {
                    oldFkCursoOfDisciplinasCollectionDisciplinas.getDisciplinasCollection().remove(disciplinasCollectionDisciplinas);
                    oldFkCursoOfDisciplinasCollectionDisciplinas = em.merge(oldFkCursoOfDisciplinasCollectionDisciplinas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cursos cursos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cursos persistentCursos = em.find(Cursos.class, cursos.getId());
            Collection<Disciplinas> disciplinasCollectionOld = persistentCursos.getDisciplinasCollection();
            Collection<Disciplinas> disciplinasCollectionNew = cursos.getDisciplinasCollection();
            Collection<Disciplinas> attachedDisciplinasCollectionNew = new ArrayList<Disciplinas>();
            for (Disciplinas disciplinasCollectionNewDisciplinasToAttach : disciplinasCollectionNew) {
                disciplinasCollectionNewDisciplinasToAttach = em.getReference(disciplinasCollectionNewDisciplinasToAttach.getClass(), disciplinasCollectionNewDisciplinasToAttach.getId());
                attachedDisciplinasCollectionNew.add(disciplinasCollectionNewDisciplinasToAttach);
            }
            disciplinasCollectionNew = attachedDisciplinasCollectionNew;
            cursos.setDisciplinasCollection(disciplinasCollectionNew);
            cursos = em.merge(cursos);
            for (Disciplinas disciplinasCollectionOldDisciplinas : disciplinasCollectionOld) {
                if (!disciplinasCollectionNew.contains(disciplinasCollectionOldDisciplinas)) {
                    disciplinasCollectionOldDisciplinas.setFkCurso(null);
                    disciplinasCollectionOldDisciplinas = em.merge(disciplinasCollectionOldDisciplinas);
                }
            }
            for (Disciplinas disciplinasCollectionNewDisciplinas : disciplinasCollectionNew) {
                if (!disciplinasCollectionOld.contains(disciplinasCollectionNewDisciplinas)) {
                    Cursos oldFkCursoOfDisciplinasCollectionNewDisciplinas = disciplinasCollectionNewDisciplinas.getFkCurso();
                    disciplinasCollectionNewDisciplinas.setFkCurso(cursos);
                    disciplinasCollectionNewDisciplinas = em.merge(disciplinasCollectionNewDisciplinas);
                    if (oldFkCursoOfDisciplinasCollectionNewDisciplinas != null && !oldFkCursoOfDisciplinasCollectionNewDisciplinas.equals(cursos)) {
                        oldFkCursoOfDisciplinasCollectionNewDisciplinas.getDisciplinasCollection().remove(disciplinasCollectionNewDisciplinas);
                        oldFkCursoOfDisciplinasCollectionNewDisciplinas = em.merge(oldFkCursoOfDisciplinasCollectionNewDisciplinas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cursos.getId();
                if (findCursos(id) == null) {
                    throw new NonexistentEntityException("The cursos with id " + id + " no longer exists.");
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
            Cursos cursos;
            try {
                cursos = em.getReference(Cursos.class, id);
                cursos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cursos with id " + id + " no longer exists.", enfe);
            }
            Collection<Disciplinas> disciplinasCollection = cursos.getDisciplinasCollection();
            for (Disciplinas disciplinasCollectionDisciplinas : disciplinasCollection) {
                disciplinasCollectionDisciplinas.setFkCurso(null);
                disciplinasCollectionDisciplinas = em.merge(disciplinasCollectionDisciplinas);
            }
            em.remove(cursos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cursos> findCursosEntities() {
        return findCursosEntities(true, -1, -1);
    }

    public List<Cursos> findCursosEntities(int maxResults, int firstResult) {
        return findCursosEntities(false, maxResults, firstResult);
    }

    private List<Cursos> findCursosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cursos.class));
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

    public Cursos findCursos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cursos.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cursos> rt = cq.from(Cursos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
