package TimeManager.ui.controllers;

import TimeManager.services.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

@Component
@Scope("view")
public class VacationCreationController implements Serializable {

    @Autowired
    VacationService vacationService;

    private Date startDate;
    private Date endDate;
    private Calendar calendar = Calendar.getInstance();

    private String dlgMessage;

    public VacationCreationController(){}

    public VacationCreationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    /**
     * saves Vacation if all criteria are fulfilled
     */
    public void doSaveVacation() {
        if (startDate == null && endDate == null) {
            this.dlgMessage = "Start- und Enddatum sind nicht gegeben!";
            return;
        }
        if (startDate == null) {
            this.dlgMessage = "Startdatum ist nicht gegeben!";
            return;
        }
        if (endDate == null) {
            this.dlgMessage = "Enddatum ist nicht gegeben!";
            return;
        }
        if (new Date().compareTo(startDate) > 0) {
            this.dlgMessage = "Startdatum liegt in der Vergangenheit!";
            return;
        }
        if (new Date().compareTo(endDate) > 0) {
            this.dlgMessage = "Enddatum liegt in der Vergangenheit!";
            return;
        }
        if (endDate.compareTo(startDate) < 0) {
            this.dlgMessage = "Enddatum liegt vor dem Startdatum!";
            return;
        }
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, 1);
        Date tmpDate = calendar.getTime();
        if (endDate.equals(startDate) && vacationService.getHolidayList().contains(tmpDate)) {
            this.dlgMessage = "Der ausgewählte Tag ist ein Feiertag!";
            return;
        }
        this.dlgMessage = "Ihr Urlaub wurde erfolgreich beantragt.";
        vacationService.createVacation(calendar, startDate, endDate);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDlgMessage() {
        return dlgMessage;
    }

    public void setDlgMessage(String dlgMessage) {
        this.dlgMessage = dlgMessage;
    }

}
