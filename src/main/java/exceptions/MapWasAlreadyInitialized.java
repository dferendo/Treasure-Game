package exceptions;

/**
 * @author Dylan Frendo.
 */
public class MapWasAlreadyInitialized extends Exception {

    public MapWasAlreadyInitialized() {
        super("Map Instance already exists!");
    }
}
