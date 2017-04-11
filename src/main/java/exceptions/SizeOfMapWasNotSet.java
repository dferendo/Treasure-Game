package exceptions;

/**
 * @author Dylan Frendo.
 */
public class SizeOfMapWasNotSet extends Exception {

    public SizeOfMapWasNotSet() {
        super("Size of Map was not set before generate function call.");
    }
}
