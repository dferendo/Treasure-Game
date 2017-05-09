import exceptions.InitialPlayerPositionWasNotSet;
import exceptions.PositionIsOutOfRange;
import org.junit.*;

import java.lang.reflect.Field;

/**
 * @author Miguel Dingli
 */
public class PlayerTest {

    private Player player;
    private final int startX = 5, startY = 10, id = 1;
    Map map;

    @Before
    public void setUp() {
        player = new Player(id);
    }

    @After
    public void tearDown() throws NoSuchFieldException, IllegalAccessException {
        // Clear Map instance
        Field instance = Map.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void setPosition_nullArgument() {
        Assert.assertFalse(player.setPosition(null));
    }
    @Test
    public void addPosition_nullArgument() {
        Assert.assertFalse(player.addPosition(null));
    }

    @Test(expected = InitialPlayerPositionWasNotSet.class)
    public void getPosition_valueBeforeSettingInitialPositionIsNull() {
        player.getPosition();
    }

    @Test
    public void getPosition_setterValueMatchesGetterValue() {
        setStartPosition();
        Assert.assertTrue(player.getPosition().getX() == startX);
        Assert.assertTrue(player.getPosition().getY() == startY);
    }

    @Test
    public void getId_constructorValueMatchesGetterValue() {
        Assert.assertTrue(player.getID() == id);
    }

    @Test
    public void getTeam_constructorValueMatchesGetterValue() {

        final int teamID = 10;
        final Team team = new Team(teamID);
        player = new Player(id, team);
        Assert.assertTrue(player.getTeam().getID() == teamID);
    }

    @Test
    public void getTeam_noTeamMeansThatGetterReturnsNull() {

        player = new Player(id);
        Assert.assertTrue(player.getTeam() == null);
    }

    @Test
    public void wasVisited_startPositionShouldBeVisited() throws PositionIsOutOfRange {
        setStartPosition();
        int mapSize = 20;

        generateMap(mapSize);
        Assert.assertTrue(player.wasVisited(startX, startY));
    }

    @Test
    public void wasVisited_addedPositionShouldBeVisited() throws PositionIsOutOfRange {
        setStartPosition();
        int mapSize = 20;
        Position posToAdd = new Position(5, 10);

        generateMap(mapSize);
        Assert.assertTrue(player.wasVisited(posToAdd.getX(), posToAdd.getY()));
    }

    @Test
    public void wasVisited_nonStartPositionShouldNotBeVisited() throws PositionIsOutOfRange {
        setStartPosition();
        int mapSize = 20;

        generateMap(mapSize);
        Assert.assertFalse(player.wasVisited(startX + 1, startY + 1));
    }

    @Test(expected = PositionIsOutOfRange.class)
    public void wasVisited_negativeCoordinates() throws PositionIsOutOfRange {
        setStartPosition();
        int mapSize = 20;

        generateMap(mapSize);
        player.wasVisited(-1, -1);
    }

    @Test(expected = PositionIsOutOfRange.class)
    public void wasVisited_xPositionGreaterThanMapSize() throws PositionIsOutOfRange {
        setStartPosition();
        int mapSize = 20;

        generateMap(mapSize);
        player.wasVisited(mapSize + 1, 1);
    }

    @Test(expected = PositionIsOutOfRange.class)
    public void wasVisited_yPositionGreaterThanMapSize() throws PositionIsOutOfRange {
        setStartPosition();
        int mapSize = 20;

        generateMap(mapSize);
        player.wasVisited(1, mapSize + 1);
    }

    @Test
    public void backToStartPosition_positionAfterCallEqualToStartPosition() {
        setStartPosition();
        player.backToStartPosition();
        Assert.assertTrue(player.getPosition().equals(new Position(startX, startY)));
    }

    @Test
    public void backToStartPosition_positionAfterCallUnequalToNonStartPosition() {
        setStartPosition();
        Assume.assumeTrue(player.setPosition(new Position(startX + 1, startY + 1)));
        player.backToStartPosition();
        Assert.assertTrue(player.getPosition().equals(new Position(startX, startY)));
    }

    private void generateMap(final int mapSize) {
        int numberOfPlayers = 3;

        // Map is needed to set the size of the map used by wasVisited
        Map map = Map.getInstance();
        Assume.assumeTrue(map.setMapSize(mapSize, mapSize, numberOfPlayers));
    }

    private void setStartPosition() {
        Assume.assumeTrue(player.setPosition(new Position(startX, startY)));
    }
}