import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TeamTest {

    private Team team;

    @Before
    public void setUp() {
        team = new Team();
    }

    @Test
    public void addPlayer_playersActuallyAddedToPlayersList() {

        Player players[] = {
                new Player(0),
                new Player(1)
        };
        for (Player p : players) {
            team.addPlayer(p);
        }
        Assert.assertArrayEquals(players, team.getPlayerList().toArray());
    }
}
