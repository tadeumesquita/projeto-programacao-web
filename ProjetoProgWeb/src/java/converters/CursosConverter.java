/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converters;

import dao.CursosDAO;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Cursos;

/**
 *
 * @author 2840481621030
 */
@FacesConverter(value = "cursosConverter", forClass = Cursos.class)
public class CursosConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            CursosDAO cursosDAO = new CursosDAO(javax.persistence.Persistence.createEntityManagerFactory("ProjetoProgWebPU"));
            
            return cursosDAO.findCursos(Integer.valueOf(value));
        }
        
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value != null && (value instanceof Cursos)) {
            return String.valueOf(((Cursos) value).getId());
        }
        
        return null;
    }
    
}
