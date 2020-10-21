package TimeManager.utils;

import TimeManager.model.*;
import TimeManager.repositories.DepartmentRepository;
import TimeManager.repositories.TeamRepository;
import TimeManager.repositories.TimeFlipRepository;
import TimeManager.services.BadgeService;
import TimeManager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Only for development and testing.
 * Generates data which cannot be initialized in data.sql
 */
@Component
@Order(0)
class InitializeMissingDataSQL implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private TimeFlipRepository timeFlipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private BadgeService badgeService;

    /**
     * Method will be triggered after SpringBoot is fully initialized and started.
     *
     * @param event ApplicationReadyEvent
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        defaultTimeFlipConfiguration();
        fixDepartmentManager();
        fixTeamLeader();
        calculateBadges();
    }

    /**
     * Sets every TimeFlip to default side configuration.
     */
    private void defaultTimeFlipConfiguration(){
        List<TimeFlip> timeFlipList = new ArrayList<>(timeFlipRepository.findAll());
        for(TimeFlip timeFlip : timeFlipList){
            if(timeFlip.getTimeFlipConfiguration().isEmpty()){
                timeFlip.setTimeFlipConfiguration(DefaultTimeFlipConfiguration.getDefaultTimeFlipConfiguration());
                timeFlipRepository.save(timeFlip);
            }
        }
    }

    /**
     * Adds department manager to the respective Department.
     */
    private void fixDepartmentManager(){
        List<Department> departmentList = new ArrayList<>(departmentRepository.findAll());
        for(Department department : departmentList){
            if(department.getDepartmentManager() != null){
                User departmentManger = department.getDepartmentManager();
                departmentManger.setDepartment(department);
                userRepository.save(departmentManger);
            }
        }
    }

    /**
     * Adds team leader to the respective Team and Department
     */
    private void fixTeamLeader(){
        List<Team> teamList = new ArrayList<>(teamRepository.findAll());
        for(Team team : teamList){
            if(team.getTeamLeader() != null){
                User teamLeader = team.getTeamLeader();
                teamLeader.setTeam(team);
                userRepository.save(teamLeader);
            }
        }
        List<Department> departmentList = new ArrayList<>(departmentRepository.findAll());
        for(Department department : departmentList){
            if(!(department.getTeam().isEmpty())){
                List<Team> teamsInDepartment = new ArrayList<>(department.getTeam());
                for(Team team : teamsInDepartment){
                    User teamLeader = team.getTeamLeader();
                    teamLeader.setDepartment(department);
                    userRepository.save(teamLeader);
                }
            }
        }
    }


    /**
     * Calculates Badges if not in database.
     */
    private void calculateBadges(){
        badgeService.onload();
    }
}
