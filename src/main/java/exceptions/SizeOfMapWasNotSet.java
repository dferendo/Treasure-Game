package exceptions;

/**
 * Exception used when trying to generate a map with the size of the map
 * not inputted.
 *
 * @author Dylan Frendo.
 */
public class SizeOfMapWasNotSet extends Exception {

    public SizeOfMapWasNotSet() {
        super("Size of Map was not set before generate function call.");
    }
}
