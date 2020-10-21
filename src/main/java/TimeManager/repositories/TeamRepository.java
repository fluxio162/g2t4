package TimeManager.repositories;

import TimeManager.model.Team;
import TimeManager.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for managing {@link  Team} entities.
 */
public interface TeamRepository extends AbstractRepository<Team, Integer>{

    /**
     * Find first team by team name.
     *
     * @param teamName the team name
     * @return the team
     */
    Team findFirstByTeamName(String teamName);

    /**
     * Find all teams.
     *
     * @return the list of teams
     */
    @Query("SELECT t FROM Team t WHERE t.enabled = true")
    List<Team> findAll();

    /**
     * Find team by its leader.
     *
     * @param teamLeader the team leader
     * @return the team
     */
    @Query("SELECT t FROM Team t WHERE t.teamLeader = :teamLeader AND t.enabled = true")
    Team findByTeamLeader(@Param("teamLeader") User teamLeader);

    /**
     * Find a team by its name.
     *
     * @param teamName the team name
     * @return the team
     */
    @Query("SELECT t FROM Team t WHERE t.teamName = :teamName")
    Team findTeamByTeamName(@Param("teamName") String teamName);

    /**
     * Find a team by a corresponding member.
     *
     * @param teamMember the team member
     * @return the list of teams
     */
    @Query("SELECT t FROM Team t WHERE :teamMember MEMBER OF t.teamMember AND t.enabled = true")
    List<Team> findTeamsByTeamMember(@Param("teamMember") User teamMember);
}

