package TimeManager.services;

import TimeManager.model.Log;
import TimeManager.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Service for accessing and manipulating task data.
 *
 */
@Component
@Scope("application")
public class LogService {

    @Autowired
    private LogRepository logRepository;

    /**
     * gets all the logs
     * @return list includes all logs
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('DEPARTMENT_MANAGER')")
    public List<Log> getAll() {
        List<Log> list = logRepository.findAll();
        Collections.reverse(list);
        return list;
    }
}