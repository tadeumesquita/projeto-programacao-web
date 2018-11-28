/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jscatena
 */
@Entity
@Table(name = "disciplinas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Disciplinas.findAll", query = "SELECT d FROM Disciplinas d")
    , @NamedQuery(name = "Disciplinas.findById", query = "SELECT d FROM Disciplinas d WHERE d.id = :id")
    , @NamedQuery(name = "Disciplinas.findByNome", query = "SELECT d FROM Disciplinas d WHERE d.nome = :nome")
    , @NamedQuery(name = "Disciplinas.findBySemestre", query = "SELECT d FROM Disciplinas d WHERE d.semestre = :semestre")})
public class Disciplinas implements Serializable {

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
    @Column(name = "semestre")
    private int semestre;
    @JoinColumn(name = "fk_curso", referencedColumnName = "id")
    @ManyToOne
    private Cursos fkCurso;
    @OneToMany(mappedBy = "fkDisciplina")
    private List<PlanosEnsino> planosEnsinoList;

    public Disciplinas() {
    }

    public Disciplinas(Integer id) {
        this.id = id;
    }

    public Disciplinas(Integer id, String nome, int semestre) {
        this.id = id;
        this.nome = nome;
        this.semestre = semestre;
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

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public Cursos getFkCurso() {
        return fkCurso;
    }

    public void setFkCurso(Cursos fkCurso) {
        this.fkCurso = fkCurso;
    }

    @XmlTransient
    public List<PlanosEnsino> getPlanosEnsinoList() {
        return planosEnsinoList;
    }

    public void setPlanosEnsinoList(List<PlanosEnsino> planosEnsinoList) {
        this.planosEnsinoList = planosEnsinoList;
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
        if (!(object instanceof Disciplinas)) {
            return false;
        }
        Disciplinas other = (Disciplinas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Disciplinas[ id=" + id + " ]";
    }
    
}
