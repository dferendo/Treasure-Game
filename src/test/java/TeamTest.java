import exceptions.MapWasAlreadyInitialized;
import exceptions.PositionIsOutOfRange;
import org.junit.*;

import java.lang.reflect.Field;

public class TeamTest {

    private Team team;
    private int teamID = 123;
    private MapCreator.MAP_TYPE DEFAULT_MAP_TYPE = MapCreator.MAP_TYPE.SAFE_MAP;

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

        final Player players[] = {
                new Player(0, team),
                new Player(1, team)
        };
        Assert.assertArrayEquals(players, team.getPlayerList().toArray());
    }

    @Test
    public void send_positionActuallyAddedToPlayersVisitedList() throws PositionIsOutOfRange, MapWasAlreadyInitialized {

        final Position pos[] = {
                new Position(10, 20),
                new Position(15, 25)
        };
        final Player players[] = {
                new Player(0, team),
                new Player(1, team)
        };

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

    private void generateMap(final int mapSize) throws MapWasAlreadyInitialized {
        int numberOfPlayers = 3;

        // Map is needed to set the size of the map used by wasVisited
        // Can be anything or can use mocking
        Map map = new SafeMap();
        Assume.assumeTrue(map.setMapSize(mapSize, mapSize, numberOfPlayers));
    }
}
