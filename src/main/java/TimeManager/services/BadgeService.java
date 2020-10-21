package TimeManager.services;

import TimeManager.model.*;
import TimeManager.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * Service for accessing and manipulating badge data.
 */
@Component
@Scope("application")
public class BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    private Collection<Task> customerMeetingTasks = new ArrayList<>();
    private Collection<Task> errorAnalysisTasks = new ArrayList<>();
    private Collection<Task> designTasks = new ArrayList<>();
    private Collection<Task> implementationTasks = new ArrayList<>();
    private Collection<Task> testingTasks = new ArrayList<>();

    /**
     * Generates badges for the past week.
     */
    public void onload() {
        Date today = new Date();
        Date aWeekAgo = new Date(today.getTime()-7*(1000 * 60 * 60 * 24));
        Collection<Badge> newestBadges = getNewestEntries(aWeekAgo);
        if (newestBadges.size()==0) {
            assignBadgesForPastWeek(aWeekAgo, today);
        }
    }

    /**
     * Assigns badges for given tasks of the same category.
     *
     * @param tasks a collection of equal tasks
     * @param title the corresponding badge title
     */
    private void initBadges(Collection<Task> tasks, String title) {
        Collection<User> users = userRepository.findAll();
        float prevTime = 0;
        float time = 0;
        User awardee = null;
        Badge badge;
        for (User u : users) {
            for (Task t : tasks) {
                if (t.getCreateUser().equals(u)) {
                    time += (t.getTaskEnd().getTime() - t.getTaskStart().getTime()) / (60 * 1000);
                }
            }
            if (time > prevTime) {
                prevTime = time;
                awardee = u;
            }
            time = 0;
        }
        if (awardee != null) {
            badge = new Badge();
            badge.setUser(awardee);
            badge.setBadgeTitle(title);
            badge.setBadgeId(generateBadgeId(badge));
            badge.setCreateDate(new Date());
            badge.setTimeSpentOnTask(prevTime/60);
            badge.setEnabled(true);
            if (title.equals(BadgeTitles.CUSTOMER_MEETING.toString())){
                badge.setImg(new File("/images/CustomerMeeting.png"));
            }
            if (title.equals(BadgeTitles.IMPLEMENTATION.toString())){
                badge.setImg(new File("/images/Implementation.png"));
            }
            if (title.equals(BadgeTitles.TESTING.toString())){
                badge.setImg(new File("/images/Testing.png"));
            }
            if (title.equals(BadgeTitles.ERROR_ANALYSIS.toString())){
                badge.setImg(new File("/images/ErrorAnalysis.png"));
            }
            if (title.equals(BadgeTitles.DESIGN.toString())){
                badge.setImg(new File("/images/Design.png"));
            }

            saveBadge(badge);
        }
    }

    /**
     * Classifies tasks by their category and calls the init badges method for each category.
     *
     * @param beginLastWeek a Date from the beginning of the previous week
     * @param endLastWeek a Date from the end of the previous week
     */
    private void assignBadgesForPastWeek(Date beginLastWeek, Date endLastWeek){
        for (Task t : taskRepository.allTasksBetweenDates(beginLastWeek, endLastWeek)) {
            if (t.getTaskCategory().toString().equals("[Kundenbesprechung]")) {
                customerMeetingTasks.add(t);
            }
            if (t.getTaskCategory().toString().equals("[Fehleranalyse und -korrektur]")) {
                errorAnalysisTasks.add(t);
            }
            if (t.getTaskCategory().toString().equals("[Design]")) {
                designTasks.add(t);
            }
            if (t.getTaskCategory().toString().equals("[Implementierung]")) {
                implementationTasks.add(t);
            }
            if (t.getTaskCategory().toString().equals("[Testen]")) {
                testingTasks.add(t);
            }
        }
        initBadges(customerMeetingTasks, BadgeTitles.CUSTOMER_MEETING.toString());
        initBadges(errorAnalysisTasks, BadgeTitles.ERROR_ANALYSIS.toString());
        initBadges(designTasks, BadgeTitles.DESIGN.toString());
        initBadges(implementationTasks, BadgeTitles.IMPLEMENTATION.toString());
        initBadges(testingTasks, BadgeTitles.TESTING.toString());
    }

    /**
     * Generates badge id.
     *
     * @param badge the badge
     * @return the int
     */
    public int generateBadgeId(Badge badge) {
        Random r = new Random();
        int low = 100000;
        int high = 999999;

        int badgeId = r.nextInt(high - low) + low;
        badge.setBadgeId(badgeId);

        if (getAllBadges().contains(badge)) {
            badgeId = generateBadgeId(badge);
        }

        return badgeId;
    }


    /**
     * Gets all badges.
     *
     * @return all badges
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    /**
     * Gets all badges from user.
     *
     * @param user the user
     * @return all badges from user
     */
    @PreAuthorize("isAuthenticated()")
    public Collection<Badge> getAllFromUser(User user) {
        return badgeRepository.findAllFromUser(user);
    }

    /**
     * Get newest entries collection.
     *
     * @param date the date
     * @return the collection
     */
    @PreAuthorize("isAuthenticated()")
    public Collection<Badge> getNewestEntries (Date date){return badgeRepository.findNewestEntries(date);}


    /**
     * Load badge.
     *
     * @param badgeId the badge id
     * @return the badge
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Badge loadBadge(int badgeId) {
        return badgeRepository.findFirstByBadgeId(badgeId);
    }


    /**
     * Save badge.
     *
     * @param badge the badge
     * @return the badge
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Badge saveBadge(Badge badge){
        return badgeRepository.save(badge);
    }


    /**
     * Delete badge.
     *
     * @param badge the badge
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteBadge(Badge badge){
        badge.setDeleteUser(getAuthenticatedUser());
        badge.setDeleteDate(new Date());
        badge.setEnabled(false);
        badgeRepository.delete(badge);
    }

    /**
     *
     * @return the signed in user
     */
    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    /**
     * Get employee badges collection.
     *
     * @return the collection
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEAM_LEADER') or hasAuthority('DEPARTMENT_MANAGER') ")
    public Collection<Badge> getEmployeeBadges(){
        Collection<Badge> employeeBadges= new ArrayList<>();
        User user = userRepository.findFirstByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.getRoles().contains(UserRole.TEAM_LEADER)) {
            Team team = teamRepository.findByTeamLeader(user);
            if (team != null) {
                for (User u : team.getTeamMember()) {
                    for (Badge b : getAllFromUser(u)) {
                        employeeBadges.add(b);
                    }
                }
            }
        }
        if (user.getRoles().contains(UserRole.DEPARTMENT_MANAGER)) {
            Department department = departmentRepository.findByDepartmentManager(user);
            if (department != null) {
                Set<Team> teams = department.getTeam();
                if (teams != null) {
                    for (Team team : teams) {
                        for (User u : team.getTeamMember()) {
                            for (Badge b : getAllFromUser(u)) {
                                employeeBadges.add(b);
                            }
                        }
                    }
                }
            }
        }
        return employeeBadges;
    }

}