package exceptions;

/**
 * Player did not have at least one position yet. This error is used when
 * resetInitialPosition() is called before player has at least one position.
 *
 * @author Miguel Dingli
 */
public class PlayerDidNotHaveAnyPositionsYet extends RuntimeException {

    public PlayerDidNotHaveAnyPositionsYet() {
        super("Player did not have a position to set as initial position!");
    }
}
