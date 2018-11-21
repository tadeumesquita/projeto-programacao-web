/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 2840481621030
 */
@Entity
@Table(name = "planos_ensino")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanosEnsino.findAll", query = "SELECT p FROM PlanosEnsino p")
    , @NamedQuery(name = "PlanosEnsino.findById", query = "SELECT p FROM PlanosEnsino p WHERE p.id = :id")
    , @NamedQuery(name = "PlanosEnsino.findByObjetivo", query = "SELECT p FROM PlanosEnsino p WHERE p.objetivo = :objetivo")
    , @NamedQuery(name = "PlanosEnsino.findByConteudo", query = "SELECT p FROM PlanosEnsino p WHERE p.conteudo = :conteudo")})
public class PlanosEnsino implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "objetivo")
    private String objetivo;
    @Column(name = "conteudo")
    private String conteudo;
    @JoinColumn(name = "fk_disciplina", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Disciplinas fkDisciplina;

    public PlanosEnsino() {
    }

    public PlanosEnsino(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Disciplinas getFkDisciplina() {
        return fkDisciplina;
    }

    public void setFkDisciplina(Disciplinas fkDisciplina) {
        this.fkDisciplina = fkDisciplina;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanosEnsino)) {
            return false;
        }
        PlanosEnsino other = (PlanosEnsino) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PlanosEnsino[ id=" + id + " ]";
    }
    
}
