import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TeamTest {

    private Team team;
    private int teamID;

    @Before
    public void setUp() {
        team = new Team(teamID);
    }

    @Test
    public void newTeam_teamIdMatchesWithConstructorArgument() {
        Assert.assertTrue(team.getID() == teamID);
    }

    @Test
    public void addPlayer_playersActuallyAddedToPlayersList() {

        // Initialize players
        final Player players[] = {
                new Player(0),
                new Player(1)
        };

        // Add players to team
        for (Player p : players) {
            team.addPlayer(p);
        }

        // Players in team must be equal to players in above array
        Assert.assertArrayEquals(players, team.getPlayerList().toArray());
    }
}
