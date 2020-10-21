package TimeManager.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Log {

    @Id
    private String DATE;

    private String LOGGER;

    private String LEVEL;

    private String MESSAGE;


    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getLOGGER() {
        return LOGGER;
    }

    public void setLOGGER(String LOGGER) {
        this.LOGGER = LOGGER;
    }

    public String getLEVEL() {
        return LEVEL;
    }

    public void setLEVEL(String LEVEL) {
        this.LEVEL = LEVEL;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }
}
