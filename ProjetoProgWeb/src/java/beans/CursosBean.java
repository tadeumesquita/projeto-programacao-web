/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CursosDAO;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Cursos;

/**
 *
 * @author 2840481621030
 */
@ManagedBean
@ViewScoped
public class CursosBean {

    private Cursos curso;
    private CursosDAO cursosDAO;
    
    public CursosBean() {
        this.curso = new Cursos();
        this.cursosDAO = new CursosDAO(javax.persistence.Persistence.createEntityManagerFactory("ProjetoProgWebPU"));
    }
    
    public Cursos getCurso() {
        return this.curso;
    }
    
    public void setCurso(Cursos curso) {
        this.curso = curso;
    }
    
    public void salvar() {
        this.cursosDAO.create(this.curso);
        
        // Instancia um novo objeto da classe Cursos, zerando o formulário
        this.curso = new Cursos();
    }
    
    public List<Cursos> listar() {
        return this.cursosDAO.findCursosEntities();
    }
    //peguei no exemplo do Jean
    /*public void alterar(Cursos curso){
    try {
    cursosDAO.edit(curso);
    } catch (NonexistentEntityException ex) {
    Logger.getLogger(CursosBean.class.getName()).log(Level.SEVERE, null, ex);
    } catch (Exception ex) {
    Logger.getLogger(CursosBean.class.getName()).log(Level.SEVERE, null, ex);
    }
    }*/
    
    public void excluir(Cursos curso){
        try {
            cursosDAO.destroy(curso.getId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CursosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
