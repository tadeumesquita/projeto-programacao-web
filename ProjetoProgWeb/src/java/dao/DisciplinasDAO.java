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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Disciplinas;

/**
 *
 * @author jscatena
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
        if (disciplinas.getPlanosEnsinoList() == null) {
            disciplinas.setPlanosEnsinoList(new ArrayList<PlanosEnsino>());
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
            List<PlanosEnsino> attachedPlanosEnsinoList = new ArrayList<PlanosEnsino>();
            for (PlanosEnsino planosEnsinoListPlanosEnsinoToAttach : disciplinas.getPlanosEnsinoList()) {
                planosEnsinoListPlanosEnsinoToAttach = em.getReference(planosEnsinoListPlanosEnsinoToAttach.getClass(), planosEnsinoListPlanosEnsinoToAttach.getId());
                attachedPlanosEnsinoList.add(planosEnsinoListPlanosEnsinoToAttach);
            }
            disciplinas.setPlanosEnsinoList(attachedPlanosEnsinoList);
            em.persist(disciplinas);
            if (fkCurso != null) {
                fkCurso.getDisciplinasList().add(disciplinas);
                fkCurso = em.merge(fkCurso);
            }
            for (PlanosEnsino planosEnsinoListPlanosEnsino : disciplinas.getPlanosEnsinoList()) {
                Disciplinas oldFkDisciplinaOfPlanosEnsinoListPlanosEnsino = planosEnsinoListPlanosEnsino.getFkDisciplina();
                planosEnsinoListPlanosEnsino.setFkDisciplina(disciplinas);
                planosEnsinoListPlanosEnsino = em.merge(planosEnsinoListPlanosEnsino);
                if (oldFkDisciplinaOfPlanosEnsinoListPlanosEnsino != null) {
                    oldFkDisciplinaOfPlanosEnsinoListPlanosEnsino.getPlanosEnsinoList().remove(planosEnsinoListPlanosEnsino);
                    oldFkDisciplinaOfPlanosEnsinoListPlanosEnsino = em.merge(oldFkDisciplinaOfPlanosEnsinoListPlanosEnsino);
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
            List<PlanosEnsino> planosEnsinoListOld = persistentDisciplinas.getPlanosEnsinoList();
            List<PlanosEnsino> planosEnsinoListNew = disciplinas.getPlanosEnsinoList();
            if (fkCursoNew != null) {
                fkCursoNew = em.getReference(fkCursoNew.getClass(), fkCursoNew.getId());
                disciplinas.setFkCurso(fkCursoNew);
            }
            List<PlanosEnsino> attachedPlanosEnsinoListNew = new ArrayList<PlanosEnsino>();
            for (PlanosEnsino planosEnsinoListNewPlanosEnsinoToAttach : planosEnsinoListNew) {
                planosEnsinoListNewPlanosEnsinoToAttach = em.getReference(planosEnsinoListNewPlanosEnsinoToAttach.getClass(), planosEnsinoListNewPlanosEnsinoToAttach.getId());
                attachedPlanosEnsinoListNew.add(planosEnsinoListNewPlanosEnsinoToAttach);
            }
            planosEnsinoListNew = attachedPlanosEnsinoListNew;
            disciplinas.setPlanosEnsinoList(planosEnsinoListNew);
            disciplinas = em.merge(disciplinas);
            if (fkCursoOld != null && !fkCursoOld.equals(fkCursoNew)) {
                fkCursoOld.getDisciplinasList().remove(disciplinas);
                fkCursoOld = em.merge(fkCursoOld);
            }
            if (fkCursoNew != null && !fkCursoNew.equals(fkCursoOld)) {
                fkCursoNew.getDisciplinasList().add(disciplinas);
                fkCursoNew = em.merge(fkCursoNew);
            }
            for (PlanosEnsino planosEnsinoListOldPlanosEnsino : planosEnsinoListOld) {
                if (!planosEnsinoListNew.contains(planosEnsinoListOldPlanosEnsino)) {
                    planosEnsinoListOldPlanosEnsino.setFkDisciplina(null);
                    planosEnsinoListOldPlanosEnsino = em.merge(planosEnsinoListOldPlanosEnsino);
                }
            }
            for (PlanosEnsino planosEnsinoListNewPlanosEnsino : planosEnsinoListNew) {
                if (!planosEnsinoListOld.contains(planosEnsinoListNewPlanosEnsino)) {
                    Disciplinas oldFkDisciplinaOfPlanosEnsinoListNewPlanosEnsino = planosEnsinoListNewPlanosEnsino.getFkDisciplina();
                    planosEnsinoListNewPlanosEnsino.setFkDisciplina(disciplinas);
                    planosEnsinoListNewPlanosEnsino = em.merge(planosEnsinoListNewPlanosEnsino);
                    if (oldFkDisciplinaOfPlanosEnsinoListNewPlanosEnsino != null && !oldFkDisciplinaOfPlanosEnsinoListNewPlanosEnsino.equals(disciplinas)) {
                        oldFkDisciplinaOfPlanosEnsinoListNewPlanosEnsino.getPlanosEnsinoList().remove(planosEnsinoListNewPlanosEnsino);
                        oldFkDisciplinaOfPlanosEnsinoListNewPlanosEnsino = em.merge(oldFkDisciplinaOfPlanosEnsinoListNewPlanosEnsino);
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
                fkCurso.getDisciplinasList().remove(disciplinas);
                fkCurso = em.merge(fkCurso);
            }
            List<PlanosEnsino> planosEnsinoList = disciplinas.getPlanosEnsinoList();
            for (PlanosEnsino planosEnsinoListPlanosEnsino : planosEnsinoList) {
                planosEnsinoListPlanosEnsino.setFkDisciplina(null);
                planosEnsinoListPlanosEnsino = em.merge(planosEnsinoListPlanosEnsino);
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
