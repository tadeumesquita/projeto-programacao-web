/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 2840481621030
 */
@Entity
@Table(name = "cursos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cursos.findAll", query = "SELECT c FROM Cursos c")
    , @NamedQuery(name = "Cursos.findById", query = "SELECT c FROM Cursos c WHERE c.id = :id")
    , @NamedQuery(name = "Cursos.findByNome", query = "SELECT c FROM Cursos c WHERE c.nome = :nome")
    , @NamedQuery(name = "Cursos.findByQtde", query = "SELECT c FROM Cursos c WHERE c.qtde = :qtde")})
public class Cursos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "qtde")
    private int qtde;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkCurso")
    private List<Disciplinas> disciplinasList;

    public Cursos() {
    }

    public Cursos(Integer id) {
        this.id = id;
    }

    public Cursos(Integer id, String nome, int qtde) {
        this.id = id;
        this.nome = nome;
        this.qtde = qtde;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    @XmlTransient
    public List<Disciplinas> getDisciplinasList() {
        return disciplinasList;
    }

    public void setDisciplinasList(List<Disciplinas> disciplinasList) {
        this.disciplinasList = disciplinasList;
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
        if (!(object instanceof Cursos)) {
            return false;
        }
        Cursos other = (Cursos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Cursos[ id=" + id + " ]";
    }
    
}
