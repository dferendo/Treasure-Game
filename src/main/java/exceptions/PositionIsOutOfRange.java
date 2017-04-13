package exceptions;

/**
 * @author Dylan Frendo.
 */
public class PositionIsOutOfRange extends Exception {

    public PositionIsOutOfRange(final int x, final int y) {
        super("Position(" + x + ", " + y + ") is not accepted.");
    }

}
