package TimeManager.tests;

import TimeManager.model.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests to ensure that each entity's implementation of equals conforms to the
 * contract. See {@linkplain http://www.jqno.nl/equalsverifier/} for more
 * information.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
public class EqualsImplementationTest {

    @Test
    public void testUserEqualsContract() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        Assert.assertNotEquals("User1 and user2 are equal", user1, user2);
        Assert.assertEquals("User1 and user1 is not equal", user1, user1);
        Assert.assertNotEquals("User1 and null are equal", user1, null);
        Assert.assertNotEquals("User1 and int are equal", user1, 0);
    }

    @Test
    public void testDepartmentEqualsContract() {
        Department department1 = new Department();
        department1.setDepartmentName("Department 1");
        Department department2 = new Department();
        department2.setDepartmentName("Department 2");
        Assert.assertNotEquals("Department 1 and Department 2 are equal", department1, department2);
        Assert.assertEquals("Department 1 and Department 1 is not equal", department1, department1);
        Assert.assertNotEquals("Department 1 and null are equal", department1, null);
        Assert.assertNotEquals("Department 1 and int are equal", department1, 0);
    }

    @Test
    public void testMiniserverEqualsContract() {
        Miniserver miniserver1 = new Miniserver();
        miniserver1.setMiniserverId(42);
        Miniserver miniserver2 = new Miniserver();
        miniserver2.setMiniserverId(43);
        Assert.assertNotEquals("Miniserver 42 and Miniserver 43 are equal", miniserver1, miniserver2);
        Assert.assertEquals("Miniserver 42 and Miniserver 42 is not equal", miniserver1, miniserver1);
        Assert.assertNotEquals("Miniserver 42 and null are equal", miniserver1, null);
        Assert.assertNotEquals("Miniserver 42 and int are equal", miniserver1, 0);
}

    @Test
    public void testRoomEqualsContract() {
        Room room1 = new Room();
        room1.setRoomId(1);
        Room room2 = new Room();
        room2.setRoomId(2);
        Assert.assertNotEquals("Room 1 and Room 2 are equal", room1, room2);
        Assert.assertEquals("Room 1 and Room 1 is not equal", room1, room1);
        Assert.assertNotEquals("Room 1 and null are equal", room1, null);
        Assert.assertNotEquals("Room 1 and int are equal", room1, 0);
    }

    @Test
    public void testTeamEqualsContract() {
        Team team1 = new Team();
        team1.setTeamName("Team 1");
        Team team2 = new Team();
        team2.setTeamName("Team 2");
        Assert.assertNotEquals("Team 1 and Team 2 are equal", team1, team2);
        Assert.assertEquals("Team 1  and Team 1 is not equal", team1, team1);
        Assert.assertNotEquals("Team 1  and null are equal", team1, null);
        Assert.assertNotEquals("Team 1  and int are equal", team1, 0);
    }

    @Test
    public void testTimeFlipEqualsContract() {
        TimeFlip timeFlip1 = new TimeFlip();
        timeFlip1.setTimeFlipId(1);
        TimeFlip timeFlip2 = new TimeFlip();
        timeFlip2.setTimeFlipId(2);
        Assert.assertNotEquals("TimeFlip 1 and TimeFlip 2 are equal", timeFlip1, timeFlip2);
        Assert.assertEquals("TimeFlip 1 and TimeFlip 1 is not equal", timeFlip1, timeFlip1);
        Assert.assertNotEquals("TimeFlip 1 and null are equal", timeFlip1, null);
        Assert.assertNotEquals("TimeFlip 1 and int are equal", timeFlip1, 0);
    }

    @Test
    public void testBadgeEqualsContract() {
        Badge badge1 = new Badge();
        badge1.setBadgeId(1);
        Badge badge2 = new Badge();
        badge2.setBadgeId(2);
        Assert.assertNotEquals("Badge 1 and Badge 2 are equal", badge1, badge2);
        Assert.assertEquals("Badge 1 and Badge 1 is not equal", badge1, badge1);
        Assert.assertNotEquals("Badge 1 and null are equal", badge1, null);
        Assert.assertNotEquals("Badge 1 and int are equal", badge1, 0);
    }

    @Test
    public void testTaskEqualsContract() {
        Task task1 = new Task();
        task1.setTaskId(1);
        Task task2 = new Task();
        task2.setTaskId(2);
        Assert.assertNotEquals("Task 1 and Task 2 are equal", task1, task2);
        Assert.assertEquals("Task 1 and Task 1 is not equal", task1, task1);
        Assert.assertNotEquals("Task 1 and null are equal", task1, null);
        Assert.assertNotEquals("Task 1 and int are equal", task1, 0);
    }

    @Test
    public void testVacationEqualsContract() {
        Vacation vacation1 = new Vacation();
        vacation1.setVacationId(1);
        Vacation vacation2 = new Vacation();
        vacation2.setVacationId(2);
        Assert.assertNotEquals("Vacation 1 and Vacation 2 are equal", vacation1, vacation2);
        Assert.assertEquals("Vacation 1 and Vacation 2 is not equal", vacation1, vacation1);
        Assert.assertNotEquals("Vacation 1 and null are equal", vacation1, null);
        Assert.assertNotEquals("Vacation 1 and int are equal", vacation1, 0);
    }
}
