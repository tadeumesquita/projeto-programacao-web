/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CursosDAO;
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
        cursosDAO.create(this.curso);
        
        // Instancia um novo objeto da classe Cursos, zerando o formulário
        this.curso = new Cursos();
    }
}
