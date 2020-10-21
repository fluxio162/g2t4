package TimeManager.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Default TimeFlip Configuration Class with method to return default Configuration
 */
public class DefaultTimeFlipConfiguration implements Serializable {

    /**
     * Returns the a Map with the default TimeFlip configuration
     *
     * @return defaultConfiguration Map
     */
    public static Map<Integer,String> getDefaultTimeFlipConfiguration(){
        Map<Integer, String> defaultConfiguration;
        defaultConfiguration = new HashMap<>();

        defaultConfiguration.put(0, "Pause/Dienstfrei");
        defaultConfiguration.put(1, "Konzeption");
        defaultConfiguration.put(2, "Design");
        defaultConfiguration.put(3, "Implementierung");
        defaultConfiguration.put(4, "Testen");
        defaultConfiguration.put(5, "Dokumentation");
        defaultConfiguration.put(6, "Fehleranalyse und -korrektur");
        defaultConfiguration.put(7, "Meeting (intern)");
        defaultConfiguration.put(8, "Kundenbesprechung");
        defaultConfiguration.put(9, "Fortbildung");
        defaultConfiguration.put(10, "Projektmanagement");
        defaultConfiguration.put(11, "Sonstiges");

        return defaultConfiguration;
    }
}
