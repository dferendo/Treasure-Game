import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * @author Dylan Frendo.
 */
public class MapCreatorTest {

    @After
    public void tearDown() throws NoSuchFieldException, IllegalAccessException {
        // Clear Map instance
        Field instance = Map.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void createMap_createSafeMap() {
        MapCreator mapCreator = new MapCreator();
        Map map = mapCreator.createMap(MapCreator.MAP_TYPE.SAFE_MAP);

        Assert.assertThat(map, CoreMatchers.instanceOf(SafeMap.class));
    }

    @Test
    public void createMap_createHazardousMap() {
        MapCreator mapCreator = new MapCreator();
        Map map = mapCreator.createMap(MapCreator.MAP_TYPE.HAZARDOUS_MAP);

        Assert.assertThat(map, CoreMatchers.instanceOf(HazardousMap.class));
    }

    @Test
    public void createMap_mapWasAlreadyInitialised() {
        MapCreator mapCreator = new MapCreator();
        Map map = mapCreator.createMap(MapCreator.MAP_TYPE.HAZARDOUS_MAP);
        Map secondMapCreator = mapCreator.createMap(MapCreator.MAP_TYPE.HAZARDOUS_MAP);
        // If a map is already initialised, the program will return that map.
        Assert.assertTrue(secondMapCreator == map);
    }

    @Test
    public void createMap_mapOfDifferentTypeWasAlreadyInitialised() {
        MapCreator mapCreator = new MapCreator();
        Map map = mapCreator.createMap(MapCreator.MAP_TYPE.HAZARDOUS_MAP);
        Map secondMapCreator = mapCreator.createMap(MapCreator.MAP_TYPE.SAFE_MAP);
        // If a map is already initialised, the program will return that map regardless
        // if it is another map
        Assert.assertTrue(secondMapCreator == map);
    }

    @Test
    public void enum_Testing() {
        Assert.assertTrue(MapCreator.MAP_TYPE.valueOf("HAZARDOUS_MAP") == MapCreator.MAP_TYPE.HAZARDOUS_MAP);
        Assert.assertTrue(MapCreator.MAP_TYPE.valueOf("SAFE_MAP") == MapCreator.MAP_TYPE.SAFE_MAP);
    }
}