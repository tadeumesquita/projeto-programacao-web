/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Disciplinas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Cursos;

/**
 *
 * @author 2840481621030
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
        if (cursos.getDisciplinasList() == null) {
            cursos.setDisciplinasList(new ArrayList<Disciplinas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Disciplinas> attachedDisciplinasList = new ArrayList<Disciplinas>();
            for (Disciplinas disciplinasListDisciplinasToAttach : cursos.getDisciplinasList()) {
                disciplinasListDisciplinasToAttach = em.getReference(disciplinasListDisciplinasToAttach.getClass(), disciplinasListDisciplinasToAttach.getId());
                attachedDisciplinasList.add(disciplinasListDisciplinasToAttach);
            }
            cursos.setDisciplinasList(attachedDisciplinasList);
            em.persist(cursos);
            for (Disciplinas disciplinasListDisciplinas : cursos.getDisciplinasList()) {
                Cursos oldFkCursoOfDisciplinasListDisciplinas = disciplinasListDisciplinas.getFkCurso();
                disciplinasListDisciplinas.setFkCurso(cursos);
                disciplinasListDisciplinas = em.merge(disciplinasListDisciplinas);
                if (oldFkCursoOfDisciplinasListDisciplinas != null) {
                    oldFkCursoOfDisciplinasListDisciplinas.getDisciplinasList().remove(disciplinasListDisciplinas);
                    oldFkCursoOfDisciplinasListDisciplinas = em.merge(oldFkCursoOfDisciplinasListDisciplinas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cursos cursos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cursos persistentCursos = em.find(Cursos.class, cursos.getId());
            List<Disciplinas> disciplinasListOld = persistentCursos.getDisciplinasList();
            List<Disciplinas> disciplinasListNew = cursos.getDisciplinasList();
            List<String> illegalOrphanMessages = null;
            for (Disciplinas disciplinasListOldDisciplinas : disciplinasListOld) {
                if (!disciplinasListNew.contains(disciplinasListOldDisciplinas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Disciplinas " + disciplinasListOldDisciplinas + " since its fkCurso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Disciplinas> attachedDisciplinasListNew = new ArrayList<Disciplinas>();
            for (Disciplinas disciplinasListNewDisciplinasToAttach : disciplinasListNew) {
                disciplinasListNewDisciplinasToAttach = em.getReference(disciplinasListNewDisciplinasToAttach.getClass(), disciplinasListNewDisciplinasToAttach.getId());
                attachedDisciplinasListNew.add(disciplinasListNewDisciplinasToAttach);
            }
            disciplinasListNew = attachedDisciplinasListNew;
            cursos.setDisciplinasList(disciplinasListNew);
            cursos = em.merge(cursos);
            for (Disciplinas disciplinasListNewDisciplinas : disciplinasListNew) {
                if (!disciplinasListOld.contains(disciplinasListNewDisciplinas)) {
                    Cursos oldFkCursoOfDisciplinasListNewDisciplinas = disciplinasListNewDisciplinas.getFkCurso();
                    disciplinasListNewDisciplinas.setFkCurso(cursos);
                    disciplinasListNewDisciplinas = em.merge(disciplinasListNewDisciplinas);
                    if (oldFkCursoOfDisciplinasListNewDisciplinas != null && !oldFkCursoOfDisciplinasListNewDisciplinas.equals(cursos)) {
                        oldFkCursoOfDisciplinasListNewDisciplinas.getDisciplinasList().remove(disciplinasListNewDisciplinas);
                        oldFkCursoOfDisciplinasListNewDisciplinas = em.merge(oldFkCursoOfDisciplinasListNewDisciplinas);
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<Disciplinas> disciplinasListOrphanCheck = cursos.getDisciplinasList();
            for (Disciplinas disciplinasListOrphanCheckDisciplinas : disciplinasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cursos (" + cursos + ") cannot be destroyed since the Disciplinas " + disciplinasListOrphanCheckDisciplinas + " in its disciplinasList field has a non-nullable fkCurso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
