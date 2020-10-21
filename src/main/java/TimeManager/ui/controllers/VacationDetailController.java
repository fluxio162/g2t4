package TimeManager.ui.controllers;

import TimeManager.services.VacationService;
import TimeManager.model.Vacation;
import TimeManager.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Controller for the Vacation detail view.
 *
 */
@Component
@Scope("view")
public class VacationDetailController implements Serializable {

    @Autowired
    private VacationService vacationService;

    private MailService mailService = new MailService();

    public VacationDetailController(){}

    public VacationDetailController(VacationService vacationServiceService){
        this.vacationService = vacationServiceService;
    }


    /**
     * Attribute to cache the currently displayed Vacation
     */
    private Vacation vacation;


    /**
     * Sets the currently displayed Vacation and reloads it form db. This Vacation is
     * targeted by any further calls of
     * {@link #doReloadVacation()}, {@link #doSaveVacation()} and
     * {@link #doDeleteVacation()}.
     *
     * @param vacation the vacation
     */
    public void setVacation(Vacation vacation) {
        this.vacation = vacation;
        doReloadVacation();
    }

    /**
     * Returns the currently displayed Vacation.
     *
     * @return the vacation
     */
    public Vacation getVacation() {
        return vacation;
    }

    /**
     * Action to force a reload of the currently displayed Vacation.
     */
    public void doReloadVacation() {
        vacation = vacationService.loadVacation(vacation.getVacationId());
    }

    /**
     * Action to save the currently displayed Vacation.
     */
    public void doSaveVacation() {
        vacation = this.vacationService.saveVacation(vacation);
    }

    /**
     * Sending an email if an vacation gets accepted or denied.
     *
     * And sets the new status "Abgelehnt" or "Bestätigt" in the vacation.
     *
     * @param status the status
     */
    public void doSaveStatusAndVacation(String status){
        vacation.setStatus(status);
        if (status.equals("Bestätigt")){
            mailService.parallelMessaging(vacation.getCreateUser(), "Bestätigung Ihres Urlaubs #" + vacation.getVacationId(),
                    "Sehr geehrte/r Frau/ Herr " + this.vacation.getCreateUser().getLastName() + ",\n\n" +
                            "Ihr Urlaub wurde soeben durch einen Vorgesetzten bestätigt. \n" +
                            "Ihre Urlaubsanträge können Sie im TimeManager System einsehen. \n\n" +
                            "Mit freundlichen Grüßen, \n" +
                            "Ihr TimeManager Team");
        }
        if (status.equals("Abgelehnt")){
            mailService.parallelMessaging(vacation.getCreateUser(), "Ablehnung Ihres Urlaubs #" + vacation.getVacationId(),
                    "Sehr geehrte/r Frau/ Herr " + this.vacation.getCreateUser().getLastName() + ",\n\n" +
                            "Ihr Urlaubsanfrage wurde leider abgelehnt. \n" +
                            "Ihre Urlaubsanträge können Sie im TimeManager System einsehen. \n\n" +
                            "Mit freundlichen Grüßen, \n" +
                            "Ihr TimeManager Team");
        }
        doSaveVacation();

    }

    /**
     * saves the vacation persistent
     *
     * @param vacations the vacation
     */
    public void save(Vacation vacations){
        this.vacationService.saveVacation(vacations);
    }

    /**
     * Action to delete the currently displayed Vacation.
     */
    public void doDeleteVacation() {
        this.vacationService.deleteVacation(vacation);
        vacation = null;
    }


}
