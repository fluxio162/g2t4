package TimeManager.ui.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Basic request scoped bean to test bean initialization.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("request")
public class HelloWorldBean {

    /**
     * Returns a hello-world-string.
     *
     * @return hello-world-string
     */
    public String getHello() {
        return "Hello from a JSF-managed bean!";
    }
}
