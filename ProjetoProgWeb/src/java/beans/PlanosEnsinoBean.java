/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.PlanosEnsinoDAO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.PlanosEnsino;

/**
 *
 * @author 2840481621030
 */
@ManagedBean
@ViewScoped
public class PlanosEnsinoBean {

    private PlanosEnsino planoEnsino;
    private PlanosEnsinoDAO planosEnsinoDAO;
    
    public PlanosEnsinoBean() {
        this.planoEnsino = new PlanosEnsino();
        this.planosEnsinoDAO = new PlanosEnsinoDAO(javax.persistence.Persistence.createEntityManagerFactory("ProjetoProgWebPU"));
    }
    
    public PlanosEnsino getPlanoEnsino() {
        return this.planoEnsino;
    }
    
    public void setPlanosEnsino(PlanosEnsino planoEnsino) {
        this.planoEnsino = planoEnsino;
    }
    
    public void salvar() {
        this.planosEnsinoDAO.create(this.planoEnsino);
        
        // Instancia um novo objeto da classe PlanosEnsino, zerando o formulário
        this.planoEnsino = new PlanosEnsino();
    }
    
    public List<PlanosEnsino> listar() {
        return this.planosEnsinoDAO.findPlanosEnsinoEntities();
    }
}
