import org.junit.After;
import org.junit.Assert;
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

}