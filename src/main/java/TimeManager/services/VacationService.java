package TimeManager.services;

import TimeManager.model.User;
import TimeManager.model.Vacation;
import TimeManager.repositories.UserRepository;
import TimeManager.repositories.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
@Scope("application")
public class VacationService {
    private static final Logger logger = Logger.getLogger(VacationService.class);
    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;


    List<Date> holidayList;

    /**
     * @return list of austrian holidays
     */
    public List<Date> getHolidayList() {
        return holidayList;
    }

    public void setHolidayList(List<Date> holidayList) {
        this.holidayList = holidayList;
    }

    /**
     * Gets all vacations.
     *
     * @return the all vacation
     */

    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('DEPARTMENT_MANAGER') or hasAuthority('TEAM_LEADER')")
    public Collection<Vacation> getAllVacation() {
        return vacationRepository.findAll();
    }

    /**
     * Gets all vacations from user.
     *
     * @param user the user
     * @return the all from user
     */

    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('DEPARTMENT_MANAGER')or hasAuthority('TEAM_LEADER')")
    public Collection<Vacation> getAllFromUser(User user) {
        return vacationRepository.findAllFromUser(user);
    }


    /**
     * Load vacation.
     *
     * @param vacationId the vacation id
     * @return the vacation
     */

    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('DEPARTMENT_MANAGER') or hasAuthority('TEAM_LEADER')")
    public Vacation loadVacation(int vacationId) {
        return vacationRepository.findFirstByVacationId(vacationId);
    }


    /**
     * Save vacation.
     *
     * @param vacation the vacation
     * @return the vacation
     */

    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('DEPARTMENT_MANAGER') or hasAuthority('TEAM_LEADER')")
    public Vacation saveVacation(Vacation vacation){

        if (vacation.isNew()) {
            vacation.setCreateDate(new Date());
            vacation.setCreateUser(getAuthenticatedUser());
        }

        return vacationRepository.save(vacation);
    }


    /**
     * Delete vacation.
     *
     * @param vacation the vacation
     */
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('DEPARTMENT_MANAGER') or hasAuthority('TEAM_LEADER')")
    public void deleteVacation(Vacation vacation){
        logger.info("Deleted: Vacation " + vacation.getVacationId() + " by " + getAuthenticatedUser().getUsername());
        vacationRepository.delete(vacation);
    }


    /**
     * Get authenticated User
     *
     * @return User
     */
    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    /**
     * Get Austrian Holidays in a Hashmap.
     * Key is the name of the holiday (String)
     * Value is the date of the holiday (Date)
     *
     * @return HashMap with Holidays
     */
    public Map<String, Date> getAustrianHolidayCalender(){
        List<Date> holidays = new ArrayList<>();

        Date date = new Date();


        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year1 = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        int year = localDate.getYear();

        Date date1 = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        LocalDate localEasterMonday = getEasterMondayDate(year).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int easterMonth = localEasterMonday.getMonthValue();
        int easterDay = localEasterMonday.getDayOfMonth();

        Date easterMonday = Date.from(localEasterMonday.atStartOfDay(ZoneId.systemDefault()).toInstant());

        LocalDate localAscensionOfChrist = localEasterMonday.plusDays(38);
        int ascensionOfChristMonth = localAscensionOfChrist.getMonthValue();
        int ascensionOfChristDay = localAscensionOfChrist.getDayOfMonth();

        Date ascensionOfChrist = Date.from(localAscensionOfChrist.atStartOfDay(ZoneId.systemDefault()).toInstant());

        LocalDate localWhitMonday = localEasterMonday.plusDays(49);
        int whitMondayMonth = localWhitMonday.getMonthValue();
        int whitMondayDay = localWhitMonday.getDayOfMonth();

        Date whitMonday = Date.from(localWhitMonday.atStartOfDay(ZoneId.systemDefault()).toInstant());

        LocalDate localCorpusChristi = localEasterMonday.plusDays(59);
        int corpusChristiMonth = localCorpusChristi.getMonthValue();
        int corpusChristiDay = localCorpusChristi.getDayOfMonth();

        Date corpusChristi = Date.from(localCorpusChristi.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Date newYearEve = new GregorianCalendar(year1, Calendar.JANUARY, 2).getTime();
        Date holyThreeKings = new GregorianCalendar(year1, Calendar.JANUARY, 7).getTime();
        Date stateHoliday = new GregorianCalendar(year1, Calendar.MAY, 2).getTime();
        Date assumptionDay = new GregorianCalendar(year1, Calendar.AUGUST, 16).getTime();

        Date nationalHoliday = new GregorianCalendar(year1, Calendar.OCTOBER, 27).getTime();
        Date allhallows = new GregorianCalendar(year1, Calendar.NOVEMBER, 2).getTime();
        Date mariaConception = new GregorianCalendar(year1, Calendar.DECEMBER, 9).getTime();
        Date christDay = new GregorianCalendar(year1, Calendar.DECEMBER, 26).getTime();
        Date stStephensDay = new GregorianCalendar(year1, Calendar.DECEMBER, 27).getTime();

        holidays.add(easterMonday);
        holidays.add(ascensionOfChrist);
        holidays.add(corpusChristi);
        holidays.add(newYearEve);
        holidays.add(stateHoliday);
        holidays.add(assumptionDay);
        holidays.add(nationalHoliday);
        holidays.add(allhallows);
        holidays.add(mariaConception);
        holidays.add(christDay);
        holidays.add(stStephensDay);

        holidayList = new ArrayList<>();
        holidayList.addAll(holidays);

        Map<String, Date> nameHoliday = new LinkedHashMap<>();
        nameHoliday.put("Neujahr", newYearEve);
        nameHoliday.put("Heilige Drei Könige", holyThreeKings);
        nameHoliday.put("Ostermontag", easterMonday);
        nameHoliday.put("Staatsfeiertag", stateHoliday);
        nameHoliday.put("Christi Himmelfahrt", ascensionOfChrist);
        nameHoliday.put("Pfingstmontag", whitMonday);
        nameHoliday.put("Fronleichnam", corpusChristi);
        nameHoliday.put("Mariä Himmelfahrt", assumptionDay);
        nameHoliday.put("Nationalfeiertag", nationalHoliday);
        nameHoliday.put("Allerheilgen", allhallows);
        nameHoliday.put("Maria Empfängnis", mariaConception);
        nameHoliday.put("Christtag", christDay);
        nameHoliday.put("Stefanitag", stStephensDay);
        return nameHoliday;
    }

    //source: https://stackoverflow.com/questions/26022233/calculate-the-date-of-easter-sunday
    /**
     * This method calculates easter monday for the year which is given as a parameter
     * @param year the year
     * @return Date of easter monday
     */
    public Date getEasterMondayDate(int year) {
        Date easterMonday;
        int a = year % 19,
                b = year / 100,
                c = year % 100,
                d = b / 4,
                e = b % 4,
                g = (8 * b + 13) / 25,
                h = (19 * a + b - d - g + 15) % 30,
                j = c / 4,
                k = c % 4,
                m = (a + 11 * h) / 319,
                r = (2 * e + 2 * j - k - h + m + 32) % 7,
                n = (h - m + r + 90) / 25,
                p = (h - m + r + n + 19) % 32;

        switch (n) {
            case 3:
                easterMonday = new GregorianCalendar(year, Calendar.MARCH, p).getTime();
                break;
            case 4:
                easterMonday = new GregorianCalendar(year, Calendar.APRIL, p).getTime();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + n);
        }
        Calendar easterCalendar = Calendar.getInstance();
        easterCalendar.setTime(easterMonday);
        easterCalendar.add(Calendar.DATE, 2);
        return easterCalendar.getTime();
    }


    /**
     * Creates a vacation between startDate and endDate.
     * This method is successful if none the parameters are null and the dates are not in the past.
     * Sends an e-mail to the department leader and team leader to inform them, also to the user who created this vacation.
     *
     * @param calendar calendar
     * @param startDate start of vacation
     * @param endDate end of vacation
     * @return vacation the created vacation
     */
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('DEPARTMENT_MANAGER') or hasAuthority('TEAM_LEADER')")
    public Vacation createVacation(Calendar calendar, Date startDate, Date endDate){
        Vacation vacation = new Vacation();
        if (startDate == null && endDate == null) {
            return null;
        }
        if (startDate == null) {
            return null;
        }
        if (endDate == null) {
            return null;
        }
        if (new Date().compareTo(startDate) > 0) {
            return null;
        }
        if (new Date().compareTo(endDate) > 0) {
            return null;
        }
        if (endDate.compareTo(startDate) < 0) {
            return null;
        }
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, 1);
        Date tmpDate = calendar.getTime();
        if (endDate.equals(startDate) && this.holidayList.contains(tmpDate)) {
            return null;
        }

        vacation.setVacationId(generateVacationId());

        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, 1);
        vacation.setVacationEnd(calendar.getTime());

        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, 1);
        vacation.setVacationStart(calendar.getTime());

        vacation.setCreateUser(getAuthenticatedUser());


        vacation.setEnabled(true);

        vacation = saveVacation(vacation);



        mailService.parallelMessaging(getAuthenticatedUser(), "Beantragung Ihres Urlaubs #" + vacation.getVacationId(),
                "Sehr geehrte/r Frau/ Herr " + vacation.getCreateUser().getLastName() + ",\n\n" +
                        "Ihr Urlaub wurde erfolgreich beantragt. Nach der Bestätigung durch Ihren Vorgesetzten erhalten Sie eine Nachricht.\n" +
                        "Ihre Urlaubsanträge können Sie im TimeManager System einsehen. \n\n" +
                        "Mit freundlichen Grüßen, \n" +
                        "Ihr TimeManager Team");

        if(getAuthenticatedUser().getTeam() != null){
            mailService.parallelMessaging(getAuthenticatedUser().getTeam().getTeamLeader(), "Neuer Urlaubsantrag #" + vacation.getVacationId(),
                    "Sehr geehrte/r Frau/ Herr " + getAuthenticatedUser().getTeam().getTeamLeader().getLastName() + ",\n\n" +
                            "Sie haben einen neuen Urlaubsantrag von " + getAuthenticatedUser().getFirstName() + " " + getAuthenticatedUser().getLastName() + ".\n" +
                            "Die an Sie gestellten Urlaubsanträge können Sie im TimeManager System einsehen. \n\n" +
                            "Mit freundlichen Grüßen, \n" +
                            "Ihr TimeManager Team");
        }


        if(getAuthenticatedUser().getDepartment() != null){
            mailService.parallelMessaging(getAuthenticatedUser().getDepartment().getDepartmentManager(), "Neuer Urlaubsantrag #" + vacation.getVacationId(),
                    "Sehr geehrte/r Frau/ Herr " + getAuthenticatedUser().getDepartment().getDepartmentManager().getLastName() + ",\n\n" +
                            "Sie haben einen neuen Urlaubsantrag von " + getAuthenticatedUser().getFirstName() + " " + getAuthenticatedUser().getLastName() + ".\n" +
                            "Die an Sie gestellten Urlaubsanträge können Sie im TimeManager System einsehen. \n\n" +
                            "Mit freundlichen Grüßen, \n" +
                            "Ihr TimeManager Team");
        }

        logger.info("Created: Vacation " + vacation.getVacationId() + " by " + getAuthenticatedUser().getUsername());
        return vacation;
    }


    /**
     * Generates a unique id for the vacation model.
     * @return vacationId
     */
    public int generateVacationId() {
        Random r = new Random();
        int vacationId;
        int low = 100000;
        int high = 999999;

        vacationId = r.nextInt(high - low) + low;

        if (vacationRepository.findFirstByVacationId(vacationId) != null) {
            vacationId = generateVacationId();
        }

        return vacationId;
    }

}