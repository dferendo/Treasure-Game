import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;

import java.util.Random;

/**
 * Create a randomized squared map consisting of Grass, Water and Treasure tiles
 * and sets the initially player position to start from a Grass tile.
 *
 * @author Dylan Frendo.
 */
public class Map {

    /**
     * GRASS: Players are allowed to walk on Grass tiles.
     * WATER: If a player moves to a water tile, the player resets to the
     * initial starting position. (Discovered tiles are still shown).
     * TREASURE: Player wins game.
     */
    public enum TILE_TYPE {
        GRASS,
        WATER,
        TREASURE
    }

    private static int size;
    private TILE_TYPE[][] map;

    public Map() {
        size = 0;
    }

    /**
     * Set the size of the Map. The size of the map can only be set once. The minimum
     * number of players is 2 while maximum is 8.
     *
     * 2-4 players (inclusive) minimum map size is 5.
     * 5-8 players (inclusive) minimum map size is 8.
     * Maximum size for both cases is 50.
     *
     * @param x: The width in tiles of the map.
     * @param y: The height in tiles of the map, needs to be the same value as x.
     * @param numberOfPlayers: Number of players playing. Minimum is 2, maximum is 8.
     * @return True if the map was inputted was correct and saved, false otherwise.
     */
    public boolean setMapSize(final int x, final int y, final int numberOfPlayers) {
        final int MIN_MAP_SIZE_FOR_2_TO_4_PLAYERS = 5;
        final int MIN_MAP_SIZE_FOR_5_TO_8_PLAYERS = 8;
        final int MAX_MAP_SIZE = 50;

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

    /**
     * Fill the map with the specified size of the map with random tiles.
     * There is only one Treasure Tile and around 10% (rounded to the
     * next Integer) water Tiles in the map. The rest are Green Tiles.
     *
     * @throws SizeOfMapWasNotSet: Method generate was called before setting the size of the map.
     */
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

    /**
     * Get the type of the Tile according to the specified coordinates. If called
     * without the generation of map it throw {@link PositionIsOutOfRange}.
     *
     * @param x: The x-coordinate.
     * @param y: The y-coordinate.
     * @return Returns the type of the Tile if found.
     * @throws PositionIsOutOfRange: Inputted coordinates go below or above the size of the map.
     */
    public TILE_TYPE getTileType(final int x, final int y) throws PositionIsOutOfRange {
        // X and y was agreed that it will start from 0.
        if ((x < 0 || x >= size) || (y < 0 || y >= size)) {
            throw new PositionIsOutOfRange(x, y);
        }
        return map[x][y];
    }

    /**
     * Sets the initial starting position for player to a Green Tile.
     *
     * @param player: Player to set the initial Position.
     * @throws PositionIsOutOfRange: The x, y coordinates generated are incorrect.
     * @throws SizeOfMapWasNotSet: Size of map was not beforehand.
     */
    public void setInitialPlayerPosition(final Player player) throws PositionIsOutOfRange, SizeOfMapWasNotSet {
        int x, y;
        Random rand;

        if (size == 0) {
            throw new SizeOfMapWasNotSet();
        }
        rand = new Random();

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
}
