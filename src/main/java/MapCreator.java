import exceptions.MapWasAlreadyInitialized;

/**
 * @author Dylan Frendo.
 */
public class MapCreator {

    public enum MAP_TYPE {
        SAFE_MAP,
        HAZARDOUS_MAP
    }

    public Map createMap(MAP_TYPE type) {
        try {
            switch (type) {
                case SAFE_MAP:
                    return new SafeMap();
                case HAZARDOUS_MAP:
                    return new HazardousMap();
                default:
                    return Map.getInstance();
            }

        } catch (MapWasAlreadyInitialized mapWasAlreadyInitialized) {
            // If Map already exists, return the instance of that map.
            return Map.getInstance();
        }
    }

}
