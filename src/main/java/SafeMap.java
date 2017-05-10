import exceptions.MapWasAlreadyInitialized;
import exceptions.SizeOfMapWasNotSet;

import java.util.Random;

/**
 * @author Dylan Frendo.
 */
public class SafeMap extends Map {

    public SafeMap() throws MapWasAlreadyInitialized {
        // Check if there is already an instance, if there is throw the error
        // Since multiple instances are not allowed.
        setInstance(this);
        size = 0;
    }

    @Override
    void generate() throws SizeOfMapWasNotSet {
        final int MAX_WATER_TILE = 10;

        int x, y, counter = 0, totalAmountOfWaterTiles;
        Random rand;

        if (size == 0) {
            throw new SizeOfMapWasNotSet();
        }
        map = new TILE_TYPE[size][size];
        rand = new Random();

        // Fill the whole map with Grass Tile.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = TILE_TYPE.GRASS;
            }
        }
        // Generate random points, size is exclusive but 0 is inclusive.
        x = rand.nextInt(size);
        y = rand.nextInt(size);

        // Generate random tile location.
        map[x][y] = TILE_TYPE.TREASURE;

        // There will be only around 10% water Tiles (rounded to the next Integer)
        totalAmountOfWaterTiles = (int) Math.ceil(((double) (size * size) / 100) * MAX_WATER_TILE);

        while (counter++ < totalAmountOfWaterTiles) {
            y = rand.nextInt(size);
            x = rand.nextInt(size);
            // If the location is already water or treasure, it does not count as a new water Tile.
            if (map[x][y] == TILE_TYPE.TREASURE || map[x][y] == TILE_TYPE.WATER) {
                counter--;
                continue;
            }
            map[x][y] = TILE_TYPE.WATER;
        }
    }
}
