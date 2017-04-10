import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Dylan Frendo.
 */
public class Map {

    public enum TILE_TYPE {
        GRASS,
        WATER,
        TREASURE;

        // Put the result of values() inside a list so it is cached instead of
        // copying an array with each values call.
        private static final List<TILE_TYPE> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private static final Random rand = new Random();

        public static TILE_TYPE getRandomTile(){
            return VALUES.get(rand.nextInt(VALUES.size()));
        }
    }

    private int size;
    private TILE_TYPE[][] map;

    public Map() {
        size = 0;
    }

    public boolean setMapSize(int mapSize, int numberOfPlayers) {
        int MIN_MAP_SIZE_FOR_2_TO_4_PLAYERS = 5;
        int MIN_MAP_SIZE_FOR_5_TO_8_PLAYERS = 8;
        int MAX_MAP_SIZE = 50;
        // TODO: check if size can be changed.

        if (size != 0) {
            return false;
        } else if (mapSize < MIN_MAP_SIZE_FOR_2_TO_4_PLAYERS || mapSize > MAX_MAP_SIZE) {
            return false;
        } else if (numberOfPlayers >= 2 && numberOfPlayers <= 4) {
            size = mapSize;
            return true;
        } else if (numberOfPlayers >= 5 && numberOfPlayers <= 8) {
            if (mapSize < MIN_MAP_SIZE_FOR_5_TO_8_PLAYERS) {
                return false;
            }
            size = mapSize;
            return true;
        } else {
            return false;
        }
    }

    public void generate() throws Exception {
        if (size == 0) {
            throw new Exception("Size was not set.");
        }
        map = new TILE_TYPE[size][size];
        // TODO: check if there needs to be a path
        // TODO: check if there can be multiple treasures.
        // TODO: Add an Custom Exception?
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = TILE_TYPE.getRandomTile();
            }
        }
    }
}
