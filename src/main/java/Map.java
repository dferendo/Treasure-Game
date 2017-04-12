import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;

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

    private static int size;
    private TILE_TYPE[][] map;

    public boolean setMapSize(int x, int y, int numberOfPlayers) {
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

    public int getMapSize() {
        return size;
    }

    public Map() {
        size = 0;
    }

    public void generate() throws SizeOfMapWasNotSet {
        if (size == 0) {
            throw new SizeOfMapWasNotSet();
        }
        map = new TILE_TYPE[size][size];
        // TODO: check with the lecturer if there needs to be a path
        // TODO: check with the lecturer if there can be multiple treasures.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = TILE_TYPE.getRandomTile();
            }
        }
    }

    public TILE_TYPE getTileType(int x, int y) throws PositionIsOutOfRange {
        // X and y was agreed that it will start from 0.
        if ((x < 0 || x >= size) || (y < 0 || y >= size)) {
            throw new PositionIsOutOfRange(x, y);
        }
        return map[x][y];
    }

    public void setInitialPlayerPosition(Position position) throws PositionIsOutOfRange {
        int x = position.getX(), y = position.getY();

        if (getTileType(x, y) != TILE_TYPE.GRASS) {
            map[x][y] = TILE_TYPE.GRASS;
        }
    }

    public static int getSize() {
        return size;
    }
}
