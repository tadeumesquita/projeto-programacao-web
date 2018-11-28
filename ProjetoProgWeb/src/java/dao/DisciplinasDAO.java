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
import model.Cursos;
import model.PlanosEnsino;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Disciplinas;

/**
 *
 * @author tadeumesquita
 */
public class DisciplinasDAO implements Serializable {

    public DisciplinasDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Disciplinas disciplinas) {
        if (disciplinas.getPlanosEnsinoCollection() == null) {
            disciplinas.setPlanosEnsinoCollection(new ArrayList<PlanosEnsino>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cursos fkCurso = disciplinas.getFkCurso();
            if (fkCurso != null) {
                fkCurso = em.getReference(fkCurso.getClass(), fkCurso.getId());
                disciplinas.setFkCurso(fkCurso);
            }
            Collection<PlanosEnsino> attachedPlanosEnsinoCollection = new ArrayList<PlanosEnsino>();
            for (PlanosEnsino planosEnsinoCollectionPlanosEnsinoToAttach : disciplinas.getPlanosEnsinoCollection()) {
                planosEnsinoCollectionPlanosEnsinoToAttach = em.getReference(planosEnsinoCollectionPlanosEnsinoToAttach.getClass(), planosEnsinoCollectionPlanosEnsinoToAttach.getId());
                attachedPlanosEnsinoCollection.add(planosEnsinoCollectionPlanosEnsinoToAttach);
            }
            disciplinas.setPlanosEnsinoCollection(attachedPlanosEnsinoCollection);
            em.persist(disciplinas);
            if (fkCurso != null) {
                fkCurso.getDisciplinasCollection().add(disciplinas);
                fkCurso = em.merge(fkCurso);
            }
            for (PlanosEnsino planosEnsinoCollectionPlanosEnsino : disciplinas.getPlanosEnsinoCollection()) {
                Disciplinas oldFkDisciplinaOfPlanosEnsinoCollectionPlanosEnsino = planosEnsinoCollectionPlanosEnsino.getFkDisciplina();
                planosEnsinoCollectionPlanosEnsino.setFkDisciplina(disciplinas);
                planosEnsinoCollectionPlanosEnsino = em.merge(planosEnsinoCollectionPlanosEnsino);
                if (oldFkDisciplinaOfPlanosEnsinoCollectionPlanosEnsino != null) {
                    oldFkDisciplinaOfPlanosEnsinoCollectionPlanosEnsino.getPlanosEnsinoCollection().remove(planosEnsinoCollectionPlanosEnsino);
                    oldFkDisciplinaOfPlanosEnsinoCollectionPlanosEnsino = em.merge(oldFkDisciplinaOfPlanosEnsinoCollectionPlanosEnsino);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Disciplinas disciplinas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Disciplinas persistentDisciplinas = em.find(Disciplinas.class, disciplinas.getId());
            Cursos fkCursoOld = persistentDisciplinas.getFkCurso();
            Cursos fkCursoNew = disciplinas.getFkCurso();
            Collection<PlanosEnsino> planosEnsinoCollectionOld = persistentDisciplinas.getPlanosEnsinoCollection();
            Collection<PlanosEnsino> planosEnsinoCollectionNew = disciplinas.getPlanosEnsinoCollection();
            if (fkCursoNew != null) {
                fkCursoNew = em.getReference(fkCursoNew.getClass(), fkCursoNew.getId());
                disciplinas.setFkCurso(fkCursoNew);
            }
            Collection<PlanosEnsino> attachedPlanosEnsinoCollectionNew = new ArrayList<PlanosEnsino>();
            for (PlanosEnsino planosEnsinoCollectionNewPlanosEnsinoToAttach : planosEnsinoCollectionNew) {
                planosEnsinoCollectionNewPlanosEnsinoToAttach = em.getReference(planosEnsinoCollectionNewPlanosEnsinoToAttach.getClass(), planosEnsinoCollectionNewPlanosEnsinoToAttach.getId());
                attachedPlanosEnsinoCollectionNew.add(planosEnsinoCollectionNewPlanosEnsinoToAttach);
            }
            planosEnsinoCollectionNew = attachedPlanosEnsinoCollectionNew;
            disciplinas.setPlanosEnsinoCollection(planosEnsinoCollectionNew);
            disciplinas = em.merge(disciplinas);
            if (fkCursoOld != null && !fkCursoOld.equals(fkCursoNew)) {
                fkCursoOld.getDisciplinasCollection().remove(disciplinas);
                fkCursoOld = em.merge(fkCursoOld);
            }
            if (fkCursoNew != null && !fkCursoNew.equals(fkCursoOld)) {
                fkCursoNew.getDisciplinasCollection().add(disciplinas);
                fkCursoNew = em.merge(fkCursoNew);
            }
            for (PlanosEnsino planosEnsinoCollectionOldPlanosEnsino : planosEnsinoCollectionOld) {
                if (!planosEnsinoCollectionNew.contains(planosEnsinoCollectionOldPlanosEnsino)) {
                    planosEnsinoCollectionOldPlanosEnsino.setFkDisciplina(null);
                    planosEnsinoCollectionOldPlanosEnsino = em.merge(planosEnsinoCollectionOldPlanosEnsino);
                }
            }
            for (PlanosEnsino planosEnsinoCollectionNewPlanosEnsino : planosEnsinoCollectionNew) {
                if (!planosEnsinoCollectionOld.contains(planosEnsinoCollectionNewPlanosEnsino)) {
                    Disciplinas oldFkDisciplinaOfPlanosEnsinoCollectionNewPlanosEnsino = planosEnsinoCollectionNewPlanosEnsino.getFkDisciplina();
                    planosEnsinoCollectionNewPlanosEnsino.setFkDisciplina(disciplinas);
                    planosEnsinoCollectionNewPlanosEnsino = em.merge(planosEnsinoCollectionNewPlanosEnsino);
                    if (oldFkDisciplinaOfPlanosEnsinoCollectionNewPlanosEnsino != null && !oldFkDisciplinaOfPlanosEnsinoCollectionNewPlanosEnsino.equals(disciplinas)) {
                        oldFkDisciplinaOfPlanosEnsinoCollectionNewPlanosEnsino.getPlanosEnsinoCollection().remove(planosEnsinoCollectionNewPlanosEnsino);
                        oldFkDisciplinaOfPlanosEnsinoCollectionNewPlanosEnsino = em.merge(oldFkDisciplinaOfPlanosEnsinoCollectionNewPlanosEnsino);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = disciplinas.getId();
                if (findDisciplinas(id) == null) {
                    throw new NonexistentEntityException("The disciplinas with id " + id + " no longer exists.");
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
            Disciplinas disciplinas;
            try {
                disciplinas = em.getReference(Disciplinas.class, id);
                disciplinas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The disciplinas with id " + id + " no longer exists.", enfe);
            }
            Cursos fkCurso = disciplinas.getFkCurso();
            if (fkCurso != null) {
                fkCurso.getDisciplinasCollection().remove(disciplinas);
                fkCurso = em.merge(fkCurso);
            }
            Collection<PlanosEnsino> planosEnsinoCollection = disciplinas.getPlanosEnsinoCollection();
            for (PlanosEnsino planosEnsinoCollectionPlanosEnsino : planosEnsinoCollection) {
                planosEnsinoCollectionPlanosEnsino.setFkDisciplina(null);
                planosEnsinoCollectionPlanosEnsino = em.merge(planosEnsinoCollectionPlanosEnsino);
            }
            em.remove(disciplinas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Disciplinas> findDisciplinasEntities() {
        return findDisciplinasEntities(true, -1, -1);
    }

    public List<Disciplinas> findDisciplinasEntities(int maxResults, int firstResult) {
        return findDisciplinasEntities(false, maxResults, firstResult);
    }

    private List<Disciplinas> findDisciplinasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Disciplinas.class));
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

    public Disciplinas findDisciplinas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Disciplinas.class, id);
        } finally {
            em.close();
        }
    }

    public int getDisciplinasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Disciplinas> rt = cq.from(Disciplinas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
