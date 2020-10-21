package TimeManager.utils;

import java.util.Map;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * Custom scope implementation for spring to enable JSF2-like view-scoped beans.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
public class ViewScope implements Scope {

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        // retrieve the view map of the faces context
        Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();

        // get the bean by its name from the view map
        Object bean = viewMap.get(name);
        if (bean == null) {
            // if the bean has not been initialized, do so now and add it to the view map
            bean = objectFactory.getObject();
            viewMap.put(name, bean);
        }
        return bean;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        // nothing to do
    }

    @Override
    public Object remove(String name) {
        // retrieve the view map of the faces context
        Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();
        // remove the object from the view map
        return viewMap.remove(name);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

}
