package exceptions;

/**
 * Exception when the inputted x and y exceeds map size.
 *
 * @author Dylan Frendo.
 */
public class PositionIsOutOfRange extends Exception {

    public PositionIsOutOfRange(final int x, final int y) {
        super("Position(" + x + ", " + y + ") is not accepted.");
    }

}
