import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;

import java.util.Random;

/**
 * @author Dylan Frendo.
 */
public class Map {

    public enum TILE_TYPE {
        GRASS,
        WATER,
        TREASURE
    }

    private static int size;
    private TILE_TYPE[][] map;

    public boolean setMapSize(final int x, final int y, final int numberOfPlayers) {
        int MIN_MAP_SIZE_FOR_2_TO_4_PLAYERS = 5;
        int MIN_MAP_SIZE_FOR_5_TO_8_PLAYERS = 8;
        int MAX_MAP_SIZE = 50;

        // Map should be squared.
        if (x != y) {
            return false;
        } else if (size != 0) {
            // Size can only be changed once since once the game is played, it will exits.
            return false;
        } else if (x < MIN_MAP_SIZE_FOR_2_TO_4_PLAYERS || x > MAX_MAP_SIZE) {
            return false;
        } else if (numberOfPlayers >= 2 && numberOfPlayers <= 4) {
            size = x;
            return true;
        } else if (numberOfPlayers >= 5 && numberOfPlayers <= 8) {
            if (x < MIN_MAP_SIZE_FOR_5_TO_8_PLAYERS) {
                return false;
            }
            size = x;
            return true;
        } else {
            return false;
        }
    }

    public void generate() throws SizeOfMapWasNotSet {
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
        totalAmountOfWaterTiles = (int) Math.ceil((double) (size * size) / 10);
        while (counter++ < totalAmountOfWaterTiles) {
            x = rand.nextInt(size);
            y = rand.nextInt(size);
            // If the location is already water or treasure, it does not count as a new water Tile.
            if (map[x][y] == TILE_TYPE.TREASURE || map[x][y] == TILE_TYPE.WATER) {
                counter--;
                continue;
            }
            map[x][y] = TILE_TYPE.WATER;
        }

    }

    public TILE_TYPE getTileType(final int x, final int y) throws PositionIsOutOfRange {
        // X and y was agreed that it will start from 0.
        if ((x < 0 || x >= size) || (y < 0 || y >= size)) {
            throw new PositionIsOutOfRange(x, y);
        }
        return map[x][y];
    }

    public void setInitialPlayerPosition(final Player player) throws PositionIsOutOfRange {
        int x, y;
        Random rand = new Random();

        while (true) {
            x = rand.nextInt(size);
            y = rand.nextInt(size);

            if (getTileType(x, y) == TILE_TYPE.GRASS) {
                player.setPosition(new Position(x, y));
                break;
            }
        }
    }

    public static int getSize() {
        return size;
    }

    public int getMapSize() {
        return size;
    }

    public Map() {
        size = 0;
    }
}
