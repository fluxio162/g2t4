package TimeManager.ui.controllers;

import TimeManager.model.Log;
import TimeManager.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;


@Component
@Scope("view")
public class LogListController implements Serializable {

    @Autowired
    private LogService logService;

    private List<Log> filteredLogs;


    /**
     * Returns a list of all logs.
     *
     * @return list of logs
     */
    public List<Log> getLogs() {
        return logService.getAll();
    }

    public List<Log> getFilteredLogs(){
        return this.filteredLogs;
    }

    public void setFilteredLogs(List<Log> filteredLogs){
       this.filteredLogs = filteredLogs;
    }
}
