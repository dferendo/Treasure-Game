package exceptions;

/**
 * @author Dylan Frendo.
 */
public class PositionIsOutOfRange extends Exception {

    public PositionIsOutOfRange(int x, int y) {
        super("Position(" + x + ", " + y + ") is not accepted.");
    }

}
