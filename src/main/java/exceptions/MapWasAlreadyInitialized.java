package exceptions;

/**
 * Map already has an instance. Error is used since Map is a singleton.
 *
 * @author Dylan Frendo.
 */
public class MapWasAlreadyInitialized extends Exception {

    public MapWasAlreadyInitialized() {
        super("Map Instance already exists!");
    }
}
