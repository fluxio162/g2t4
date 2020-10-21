package TimeManager.ui.beans;

import javax.annotation.ManagedBean;
import javax.faces.bean.ApplicationScoped;

/**
 * Bean for casting a long variable to and integer.
 */
@ManagedBean
@ApplicationScoped
public class LongToIntCastingBean {

    public LongToIntCastingBean(){
    }

    /**
     * Casts long to int.
     *
     * @param l long var
     * @return int var
     */
    public int toInt(long l) {
        return (int) l;
    }
}
