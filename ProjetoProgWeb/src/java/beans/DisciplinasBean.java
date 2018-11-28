/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.DisciplinasDAO;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Disciplinas;

/**
 *
 * @author 2840481621030
 */
@ManagedBean
@ViewScoped
public class DisciplinasBean {

    private Disciplinas disciplina;
    private DisciplinasDAO disciplinasDAO;
    
    public DisciplinasBean() {
        this.disciplina = new Disciplinas();
        this.disciplinasDAO = new DisciplinasDAO(javax.persistence.Persistence.createEntityManagerFactory("ProjetoProgWebPU"));
    }
    
    public Disciplinas getDisciplina() {
        return this.disciplina;
    }
    
    public void setDisciplinas(Disciplinas disciplina) {
        this.disciplina = disciplina;
    }
    
    public void salvar() {
        this.disciplinasDAO.create(this.disciplina);
        
        // Instancia um novo objeto da classe Disciplinas, zerando o formulário
        this.disciplina = new Disciplinas();
    }
    
    public List<Disciplinas> listar() {
        return this.disciplinasDAO.findDisciplinasEntities();
    }
    
    public void excluir(Disciplinas disciplina){
        try {
            disciplinasDAO.destroy(disciplina.getId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(DisciplinasBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
