/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CursosDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Cursos;

/**
 *
 * @author 2840481621030
 */
@ManagedBean
@SessionScoped
public class EditarCursosBean {
    
    private Cursos curso;
    private CursosDAO cursosDAO;

    public EditarCursosBean() {
        this.curso = new Cursos();
        this.cursosDAO = new CursosDAO(javax.persistence.Persistence.createEntityManagerFactory("ProjetoProgWebPU"));
    }
    
    public Cursos getCurso() {
        return this.curso;
    }
    
    public void setCurso(Cursos curso) {
        this.curso = curso;
    }
    
    public String carregarEdicao(Cursos curso) {
        this.setCurso(curso);
        
        return "edicao-de-cursos.xhtml?faces-redirect=true";
    }
    
    public void salvar() {
        try {
            this.cursosDAO.edit(this.curso);
        } catch (Exception ex) {
            Logger.getLogger(EditarCursosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
