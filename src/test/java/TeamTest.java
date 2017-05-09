import exceptions.PositionIsOutOfRange;
import org.junit.*;

import java.lang.reflect.Field;

public class TeamTest {

    private Team team;
    private int teamID = 123;

    @Before
    public void setUp() {
        team = new Team(teamID);
    }

    @After
    public void tearDown() throws NoSuchFieldException, IllegalAccessException {
        // Clear Map instance
        Field instance = Map.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void newTeam_teamIdMatchesWithConstructorArgument() {
        Assert.assertTrue(team.getID() == teamID);
    }

    @Test
    public void addPlayer_playersActuallyAddedToPlayersList() {

        final Player players[] = fillTeamWithPlayers();
        Assert.assertArrayEquals(players, team.getPlayerList().toArray());
    }

    @Test
    public void send_positionActuallyAddedToPlayersVisitedList() throws PositionIsOutOfRange {

        final Position pos[] = {
                new Position(10, 20),
                new Position(15, 25)
        };
        final Player players[] = fillTeamWithPlayers();

        // Generate map
        generateMap(30);

        // Set and send positions
        players[0].setPosition(pos[0]);
        players[1].setPosition(pos[1]);
        team.send(pos[0], players[0]);
        team.send(pos[1], players[1]);

        // Check that the players have each others' positions
        Assert.assertTrue(players[0].wasVisited(pos[1].getX(), pos[1].getY()));
        Assert.assertTrue(players[1].wasVisited(pos[0].getX(), pos[0].getY()));
    }

    public Player[] fillTeamWithPlayers() {

        // Initialize players
        final Player players[] = {
                new Player(0),
                new Player(1)
        };

        // Add players to team
        for (Player p : players) {
            team.addPlayer(p);
        }

        // Return the array
        return players;
    }

    private void generateMap(final int mapSize) {
        int numberOfPlayers = 3;

        // Map is needed to set the size of the map used by wasVisited
        Map map = Map.getInstance();
        Assume.assumeTrue(map.setMapSize(mapSize, mapSize, numberOfPlayers));
    }
}
