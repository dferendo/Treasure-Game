import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dylan Frendo.
 */
public class MapTest {

    private Map mapInstance;

    @Before
    public void setUp() {
        mapInstance = new Map();
    }

    @Test
    public void setMapSize_sizeWasAlreadySet() {
        int mapSize = 5, players = 4, mapSize2 = 8, players2 = 4;
        if (mapInstance.setMapSize(mapSize, players)) {
            Assert.assertTrue("Size of map was already set.", !mapInstance.setMapSize(mapSize2, players2));
        } else {
            fail("setMapSize was not set before hand.");
        }
    }

    @Test
    public void setMapSize_mapSizeIsLessThenMinimumFor2To4Players() {
        int mapSize = 3, players = 3;

        Assert.assertFalse("Map size " + mapSize + " must be greater than or equal than 5 for " + players + " players",
                mapInstance.setMapSize(mapSize, players));
    }

    @Test
    public void setMapSize_mapSizeIsGreaterThanMaximumSize() {
        int mapSize = 51, players = 4;

        Assert.assertFalse("Map size " + mapSize + " is greater than the maximum map size.",
                mapInstance.setMapSize(mapSize, players));
    }

    @Test
    public void setMapSize_mapSizeIsCorrectFor2To4Players() {
        int mapSize = 5, players = 2;

        Assert.assertTrue("Map size " + mapSize + " is correct for " + players + " players",
                mapInstance.setMapSize(mapSize, players));
    }

    @Test
    public void setMapSize_mapSizeIsLessThenMinimumFor5To8Players() {
        int mapSize = 6, players = 8;

        Assert.assertFalse("Map size " + mapSize + " must be greater then 8 for " + players + " players",
                mapInstance.setMapSize(mapSize, players));
    }

    @Test
    public void setMapSize_mapSizeIsCorrectFor5To8Players() {
        int mapSize = 10, players = 6;

        Assert.assertTrue("Map size " + mapSize + "is correct for " + players + "players.",
                mapInstance.setMapSize(mapSize, players));
    }

    @Test(expected = Exception.class)
    public void generate_sizeWasNotSetBeforeHand() throws Exception {
        mapInstance.generate();
    }

    @Test(expected = Exception.class)
    public void getTileType_xIsLessThan0YIsCorrect() throws Exception {
        int size = 10, x = -1, y = 2;
        generateMap(size);

        mapInstance.getTileType(x, y);
    }

    @Test(expected = Exception.class)
    public void getTileType_xIsGreaterThanSizeYIsCorrect() throws Exception {
        int size = 10, y = 2;
        generateMap(size);

        mapInstance.getTileType(size, y);
    }

    @Test(expected = Exception.class)
    public void getTileType_xIsCorrectYIsLessThan0() throws Exception {
        int size = 10, x = 2, y = -1;
        generateMap(size);

        mapInstance.getTileType(x, y);
    }

    @Test(expected = Exception.class)
    public void getTileType_xIsCorrectYIsGreaterThanSize() throws Exception {
        int size = 10, x = 2;
        generateMap(size);

        mapInstance.getTileType(x, size);
    }

    @Test
    public void getTileType_correctInput() throws Exception {
        int size = 10, x = 2, y = 2;
        generateMap(size);

        Assert.assertTrue(mapInstance.getTileType(x, y) == Map.TILE_TYPE.GRASS
                || mapInstance.getTileType(x, y) == Map.TILE_TYPE.TREASURE
                || mapInstance.getTileType(x, y) == Map.TILE_TYPE.WATER);
    }

    @Test
    public void setInitialPlayerPosition_checkIfTileIsChange() throws Exception {
        int size = 10, x = 2, y = 2, playerID = 1;
        Player player = new Player(playerID);

        player.setPosition(new Position(x, y));
        generateMap(size);

        mapInstance.setInitialPlayerPosition(player.getPosition());
        Assert.assertTrue(mapInstance.getTileType(x, y) == Map.TILE_TYPE.GRASS);
    }

    private void generateMap(int size) {
        int numberOfPlayers = 4;
        mapInstance.setMapSize(size, numberOfPlayers);
        try {
            mapInstance.generate();
        } catch (Exception e) {
            fail("Map size inserted was incorrect.");
        }
    }

    @Test
    public void getMapSize_getterValueMatchesSetterValue() {
        int size = 30, players = 8;
        Assume.assumeTrue(mapInstance.setMapSize(size, players));
        Assert.assertTrue(mapInstance.getMapSize() == size);
    }
}