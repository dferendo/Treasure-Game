package exceptions;

/**
 * @author Miguel Dingli
 */
public class InitialPlayerPositionWasNotSet extends RuntimeException {
    public InitialPlayerPositionWasNotSet() {
        super("Initial player position was not set before calling getPosition.");
    }
}
