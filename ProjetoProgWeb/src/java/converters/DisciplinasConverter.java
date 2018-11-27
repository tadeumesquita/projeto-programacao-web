/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converters;

import dao.DisciplinasDAO;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Disciplinas;

/**
 *
 * @author 2840481621030
 */
@FacesConverter(value = "disciplinasConverter", forClass = Disciplinas.class)
public class DisciplinasConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            DisciplinasDAO disciplinasDAO = new DisciplinasDAO(javax.persistence.Persistence.createEntityManagerFactory("ProjetoProgWebPU"));
            
            return disciplinasDAO.findDisciplinas(Integer.valueOf(value));
        }
        
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value != null && (value instanceof Disciplinas)) {
            return String.valueOf(((Disciplinas) value).getId());
        }
        
        return null;
    }
    
}
