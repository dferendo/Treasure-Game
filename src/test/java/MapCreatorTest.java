import exceptions.GameWasNotInitialized;
import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.fail;

/**
 * @author Dylan Frendo.
 */
public class MapCreatorTest {

    private Game game;

    @After
    public void tearDown() throws NoSuchFieldException, IllegalAccessException {
        // Clear Map instance
        Field instance = Map.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void createMap_createSafeMap() throws SizeOfMapWasNotSet, GameWasNotInitialized {
        MapCreator mapCreator = new MapCreator();
        // No collaborative mode, 2 players, safe mode, map size 5
        setUpGameForMap("0\n2\n0\n5\n");
        Map map = mapCreator.createMap(MapCreator.MAP_TYPE.SAFE_MAP, game);

        Assert.assertThat(map, CoreMatchers.instanceOf(SafeMap.class));
    }

    @Test
    public void createMap_createHazardousMap() throws SizeOfMapWasNotSet, GameWasNotInitialized {
        MapCreator mapCreator = new MapCreator();
        // No collaborative mode, 2 players, hazardous mode, map size 5
        setUpGameForMap("0\n2\n1\n5\n");
        Map map = mapCreator.createMap(MapCreator.MAP_TYPE.HAZARDOUS_MAP, game);

        Assert.assertThat(map, CoreMatchers.instanceOf(HazardousMap.class));
    }

    @Test
    public void createMap_mapWasAlreadyInitialised() throws SizeOfMapWasNotSet, GameWasNotInitialized {
        MapCreator mapCreator = new MapCreator();
        // No collaborative mode, 2 players, hazardous mode, map size 5
        setUpGameForMap("0\n2\n1\n5\n");
        Map map = mapCreator.createMap(MapCreator.MAP_TYPE.HAZARDOUS_MAP, game);
        Map secondMapCreator = mapCreator.createMap(MapCreator.MAP_TYPE.HAZARDOUS_MAP, game);
        // If a map is already initialised, the program will return that map.
        Assert.assertTrue(secondMapCreator == map);
    }

    @Test
    public void createMap_mapOfDifferentTypeWasAlreadyInitialised() throws SizeOfMapWasNotSet, GameWasNotInitialized {
        MapCreator mapCreator = new MapCreator();
        // No collaborative mode, 2 players, hazardous mode, map size 5
        setUpGameForMap("0\n2\n1\n5\n");
        Map map = mapCreator.createMap(MapCreator.MAP_TYPE.HAZARDOUS_MAP, game);
        Map secondMapCreator = mapCreator.createMap(MapCreator.MAP_TYPE.SAFE_MAP, game);
        // If a map is already initialised, the program will return that map regardless
        // if it is another map
        Assert.assertTrue(secondMapCreator == map);
    }

    @Test
    public void enum_Testing() {
        Assert.assertTrue(MapCreator.MAP_TYPE.valueOf("HAZARDOUS_MAP") == MapCreator.MAP_TYPE.HAZARDOUS_MAP);
        Assert.assertTrue(MapCreator.MAP_TYPE.valueOf("SAFE_MAP") == MapCreator.MAP_TYPE.SAFE_MAP);
    }

    /**
     * Set the input stream to be used to simulate the user input and attempt to set up the game.
     */
    private void setUpGameForMap(final String input) {
        // Game can be mocked.
        game = new Game(new ByteArrayInputStream(input.getBytes()));
        try {
            // Set-up game
            game.setup();
        } catch (GameWasNotInitialized e1) {
            fail("Game was not initialized.");
        } catch (SizeOfMapWasNotSet e2) {
            fail("Size of map was not set.");
        } catch (PositionIsOutOfRange e3) {
            fail("A position was out of range.");
        } catch (NoSuchElementException e4) {
            fail("Not enough valid input was present.");
        }
    }
}