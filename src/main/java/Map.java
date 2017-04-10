/**
 * @author Dylan Frendo.
 */
public class Map {

    private int size;

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
    }
}
