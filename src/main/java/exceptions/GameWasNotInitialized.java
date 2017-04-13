package exceptions;

/**
 * @author Miguel Dingli
 */
public class GameWasNotInitialized extends Exception {

    public GameWasNotInitialized(final String aspect) {
        super(aspect + " was not initialized.");
    }
}
