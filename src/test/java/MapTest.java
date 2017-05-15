import exceptions.MapWasAlreadyInitialized;
import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;
import org.junit.*;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Tests used to test the implementation of Map.
 *
 * @author Dylan Frendo.
 */
public class MapTest {

    private Map mapInstance;

    @Before
    public void setUp() throws MapWasAlreadyInitialized {
        // Can be anything or can use mocking
        mapInstance = new SafeMap();
    }

    @After
    public void tearDown() throws NoSuchFieldException, IllegalAccessException {
        // Clear Map instance
        Field instance = Map.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void setMapSize_xAndYAreNotEqual() {
        int x = 5, y = 6, players = 3;

        Assert.assertFalse("(x, y) of Map should be equal", mapInstance.setMapSize(x, y, players));
    }

    @Test
    public void setMapSize_sizeWasAlreadySet() {
        int x = 5, y = 5, players = 4;
        if (mapInstance.setMapSize(x, y, players)) {
            Assert.assertTrue("Size of map was already set.", !mapInstance.setMapSize(x, y, players));
        } else {
            fail("setMapSize was not set before hand.");
        }
    }

    @Test
    public void setMapSize_mapSizeIsLessThenMinimumFor2To4Players() {
        int x = 3, y = 3, players = 3;

        Assert.assertFalse("Map size " + 3 + " must be greater than or equal than 5 for " + players + " players",
                mapInstance.setMapSize(x, y, players));
    }

    @Test
    public void setMapSize_mapSizeIsGreaterThanMaximumSize() {
        int x = 51, y = 5, players = 4;

        Assert.assertFalse("Map size " + x + " is greater than the maximum map size.",
                mapInstance.setMapSize(x, y, players));
    }

    @Test
    public void setMapSize_mapSizeIsCorrectFor2To4Players() {
        int x = 5, y = 5, players = 2;

        Assert.assertTrue("Map size " + x + " is correct for " + players + " players",
                mapInstance.setMapSize(x, y, players));
    }

    @Test
    public void setMapSize_mapSizeIsLessThenMinimumFor5To8Players() {
        int x = 6, y = 6, players = 8;

        Assert.assertFalse("Map size " + x + " must be greater then 8 for " + players + " players",
                mapInstance.setMapSize(x, y, players));
    }

    @Test
    public void setMapSize_mapSizeIsCorrectFor5To8Players() {
        int x = 10, y = 10, players = 6;

        Assert.assertTrue("Map size " + x + "is correct for " + players + "players.",
                mapInstance.setMapSize(x, y, players));
    }

    @Test
    public void setMapSize_incorrectNumberOfPlayersGiven() {
        int x = 10, y = 10, players = 12;

        Assert.assertFalse("More players are given then allowed.", mapInstance.setMapSize(x, y, players));
    }

    @Test(expected = PositionIsOutOfRange.class)
    public void getTileType_xIsLessThan0YIsCorrect() throws PositionIsOutOfRange {
        int size = 10, x = -1, y = 2;
        generateMap(size);

        mapInstance.getTileType(x, y);
    }

    @Test(expected = PositionIsOutOfRange.class)
    public void getTileType_xIsGreaterThanSizeYIsCorrect() throws PositionIsOutOfRange {
        int size = 10, y = 2;
        generateMap(size);

        mapInstance.getTileType(size, y);
    }

    @Test(expected = PositionIsOutOfRange.class)
    public void getTileType_xIsCorrectYIsLessThan0() throws PositionIsOutOfRange {
        int size = 10, x = 2, y = -1;
        generateMap(size);

        mapInstance.getTileType(x, y);
    }

    @Test(expected = PositionIsOutOfRange.class)
    public void getTileType_xIsCorrectYIsGreaterThanSize() throws PositionIsOutOfRange {
        int size = 10, x = 2;
        generateMap(size);

        mapInstance.getTileType(x, size);
    }

    @Test
    public void getTileType_correctInput() throws PositionIsOutOfRange {
        int size = 10, x = 2, y = 2;
        generateMap(size);

        Assert.assertTrue(mapInstance.getTileType(x, y) == Map.TILE_TYPE.GRASS
                || mapInstance.getTileType(x, y) == Map.TILE_TYPE.TREASURE
                || mapInstance.getTileType(x, y) == Map.TILE_TYPE.WATER);
    }

    @Test
    public void setInitialPlayerPosition_checkIfTileIsGrassTile() throws PositionIsOutOfRange, SizeOfMapWasNotSet {
        int size = 10, playerID = 1;
        Player player = new Player(playerID);

        generateMap(size);

        mapInstance.setInitialPlayerPosition(player);
        Assert.assertTrue(mapInstance.getTileType(player.getPosition().getX(),
                player.getPosition().getY()) == Map.TILE_TYPE.GRASS);
    }

    @Test(expected = SizeOfMapWasNotSet.class)
    public void setInitialPlayerPosition_sizeWasNotSet() throws PositionIsOutOfRange, SizeOfMapWasNotSet {
        int playerID = 1;
        Player player = new Player(playerID);

        mapInstance.setInitialPlayerPosition(player);
    }

    @Test
    public void getMapSize_getterValueMatchesSetterValue() {
        int size = 30, players = 8;
        Assume.assumeTrue(mapInstance.setMapSize(size, size, players));
        Assert.assertTrue(Map.getSize() == size);
    }

    @Test
    public void enum_Testing() {
        Assert.assertTrue(Map.TILE_TYPE.valueOf("GRASS") == Map.TILE_TYPE.GRASS);
        Assert.assertTrue(Map.TILE_TYPE.valueOf("WATER") == Map.TILE_TYPE.WATER);
        Assert.assertTrue(Map.TILE_TYPE.valueOf("TREASURE") == Map.TILE_TYPE.TREASURE);
    }

    private void generateMap(final int mapSize) {
        int numberOfPlayers = 4;
        mapInstance.setMapSize(mapSize, mapSize, numberOfPlayers);
        try {
            mapInstance.generate();
        } catch (SizeOfMapWasNotSet e) {
            fail("Map size inserted was incorrect.");
        }
    }
}