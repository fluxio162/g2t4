package TimeManager.ui.controllers;

import TimeManager.services.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
@Scope("view")
public class AustrianHolidayController implements Serializable {

    @Autowired
    VacationService vacationService;

    private Map<String, Date> holiday;

    public AustrianHolidayController() {
    }

    public AustrianHolidayController(VacationService vacationService) {
        this.vacationService = vacationService;

    }

    /**
     * return a Map of austrian holidays
     *
     * @return map of austrian holidays
     */
    public Map<String, Date> getHoliday() {
        return vacationService.getAustrianHolidayCalender();
    }

    public void setHoliday(Map<String, Date> holiday) {
        this.holiday = holiday;
    }
}
