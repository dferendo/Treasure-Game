import exceptions.GameWasNotInitialized;
import exceptions.MapWasAlreadyInitialized;
import exceptions.SizeOfMapWasNotSet;

/**
 * @author Dylan Frendo.
 */
public class MapCreator {

    /**
     * Map types available.
     * SAFE_MAP: contains a map with 10% water tiles.
     * HAZARDOUS_MAP: contains a map between 25%-35% water tiles.
     */
    public enum MAP_TYPE {
        SAFE_MAP,
        HAZARDOUS_MAP
    }

    /**
     * Creates the map depending on the passed map type. If Map already has an instance,
     * the instance is returned.
     *
     * @param type: The map type of the game.
     * @param game: Required game variables needed to set up the map.
     * @return An new instance of a map if there was not an instance before or the
     * already existing instance if there was an instance.
     * @throws GameWasNotInitialized Thrown by setMapSize() if the number of player was not set
     * since the minimum size of the map depends on the number of players.
     * @throws SizeOfMapWasNotSet Thrown by map.generate() if the size of the map was
     * not set since these operations depend on the map size.
     */
    public Map createMap(MAP_TYPE type, Game game) throws SizeOfMapWasNotSet, GameWasNotInitialized {
        Map map;

        try {
            switch (type) {
                case SAFE_MAP:
                    map = new SafeMap();
                    // Set map size and generate map
                    setMapSize(game);
                    map.generate();
                    return map;
                case HAZARDOUS_MAP:
                    map = new HazardousMap();
                    // Set map size and generate map
                    setMapSize(game);
                    map.generate();
                    return map;
                default:
                    return Map.getInstance();
            }

        } catch (MapWasAlreadyInitialized mapWasAlreadyInitialized) {
            // If Map already exists, return the instance of that map.
            return Map.getInstance();
        }
    }

    /**
     * Helper method to set the size of the map.
     *
     * @throws GameWasNotInitialized Thrown if the players array is null.
     */
    private void setMapSize(Game game) throws GameWasNotInitialized {
        Map map;

        // Check if players array was initialized
        if (game.getPlayers() == null) {
            throw new GameWasNotInitialized("Players array");
        }

        // Minimum (based on players) and maximum map size and a range in string form
        final int MIN_MAP_SIZE = (game.getPlayers().length <= 4 ? 5 : 8), MAX_MAP_SIZE = 50;
        final String MAP_SIZE_RANGE = "(" + MIN_MAP_SIZE + "-" + MAX_MAP_SIZE + ")";

        map = Map.getInstance();

        // Loop until a valid map size is obtained
        while (true) {
            System.out.println("What will be the size of the map? " + MAP_SIZE_RANGE);
            final int mapSize = game.getValidInt();

            if (!map.setMapSize(mapSize, mapSize, game.getPlayers().length)) {
                System.out.println("The input value was out of the range " + MAP_SIZE_RANGE + ".");
            } else {
                break;
            }
        }
    }
}
