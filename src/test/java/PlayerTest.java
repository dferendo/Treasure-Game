import exceptions.InitialPlayerPositionWasNotSet;
import exceptions.PositionIsOutOfRange;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Miguel Dingli
 */
public class PlayerTest {

    private Player player;
    private final int startX = 5, startY = 10, id = 1;

    @Before
    public void init() {
        player = new Player(id);
    }

    @Test
    public void setPosition_nullArgument() {
        Assert.assertFalse(player.setPosition(null));
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
    public void wasVisited_startPositionShouldBeVisited() throws PositionIsOutOfRange {
        setStartPosition();
        int mapSize = 20;

        generateMap(mapSize);
        Assert.assertTrue(player.wasVisited(startX, startY));
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

    @Test
    public void enum_Testing() {
        Assert.assertTrue(Player.MOVE_DIRECTION.valueOf("RIGHT") == Player.MOVE_DIRECTION.RIGHT);
        Assert.assertTrue(Player.MOVE_DIRECTION.valueOf("UP") == Player.MOVE_DIRECTION.UP);
        Assert.assertTrue(Player.MOVE_DIRECTION.valueOf("LEFT") == Player.MOVE_DIRECTION.LEFT);
        Assert.assertTrue(Player.MOVE_DIRECTION.valueOf("DOWN") == Player.MOVE_DIRECTION.DOWN);
    }

    private void generateMap(final int mapSize) {
        int numberOfPlayers = 3;

        // Map is needed to set the size of the map used by wasVisited
        Map map = new Map();
        Assume.assumeTrue(map.setMapSize(mapSize, mapSize, numberOfPlayers));
    }

    private void setStartPosition() {
        Assume.assumeTrue(player.setPosition(new Position(startX, startY)));
    }
}