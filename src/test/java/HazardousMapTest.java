import exceptions.MapWasAlreadyInitialized;
import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * @author Dylan Frendo.
 */
public class HazardousMapTest {

    private Map mapInstance;
    private MapCreator.MAP_TYPE mapType = MapCreator.MAP_TYPE.HAZARDOUS_MAP;

    @Before
    public void setUp() {
        MapCreator mapCreator = new MapCreator();
        mapInstance = mapCreator.createMap(mapType);
    }

    @After
    public void tearDown() throws NoSuchFieldException, IllegalAccessException {
        // Clear Map instance
        Field instance = Map.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test(expected = MapWasAlreadyInitialized.class)
    public void hazardousMap_initialiseAMapWithOutCreatorThatAlreadyExists() throws MapWasAlreadyInitialized {
        HazardousMap hazardousMap = new HazardousMap();
    }

    @Test(expected = SizeOfMapWasNotSet.class)
    public void generate_sizeWasNotSetBeforeHand() throws SizeOfMapWasNotSet {
        mapInstance.generate();
    }

    @Test
    public void generate_checkThereExistsOneTreasure() throws SizeOfMapWasNotSet, PositionIsOutOfRange {
        int size = 10, numberOfPlayers = 3, treasureCount = 0, totalSize = 0;
        if (mapInstance.setMapSize(size, size, numberOfPlayers)) {
            mapInstance.generate();

            for (int x = 0; x < mapInstance.getMapSize(); x++) {
                for (int y = 0; y < mapInstance.getMapSize(); y++) {
                    if (mapInstance.getTileType(x, y) == Map.TILE_TYPE.TREASURE) {
                        treasureCount++;
                    }
                    totalSize++;
                }
            }

            Assert.assertTrue("There is suppose to be only 1 treasure.", treasureCount == 1);
            Assert.assertTrue("There should be n x n Tiles", totalSize ==
                    mapInstance.getMapSize() * mapInstance.getMapSize());
        } else {
            fail("Map size was suppose to be set.");
        }
    }

    @Test
    public void generate_checkThereAreBetween25to35PercentWaterTiles() throws SizeOfMapWasNotSet, PositionIsOutOfRange {
        int size = 10, numberOfPlayers = 3, waterTiles = 0, totalSize = 0, waterTilePercentage;
        if (mapInstance.setMapSize(size, size, numberOfPlayers)) {
            mapInstance.generate();
            for (int x = 0; x < mapInstance.getMapSize(); x++) {
                for (int y = 0; y < mapInstance.getMapSize(); y++) {
                    if (mapInstance.getTileType(x, y) == Map.TILE_TYPE.WATER) {
                        waterTiles++;
                    }
                    totalSize++;
                }
            }

            waterTilePercentage = (int) Math.ceil(((double) (size * size) / 100) * waterTiles);

            Assert.assertTrue("There is suppose to be around 25% to 35% water tiles.",
                    waterTilePercentage >= 25 && waterTilePercentage <= 35);
            Assert.assertTrue("There should be n x n Tiles", totalSize ==
                    mapInstance.getMapSize() * mapInstance.getMapSize());
        } else {
            fail("Map size was suppose to be set.");
        }
    }
}