/**
 * Stores the x and y coordinates of a location and provides a function to check if two
 * positions are equal. It is intended to be used for player positions but is also used
 * for tile positions, especially in tests.
 *
 * @author Miguel Dingli
 */
public class Position {

    /**
     * The constant x and y coordinates
     */
    private final int x, y;

    /**
     * Constructor sets the two coordinates.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the position.
     *
     * @return x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the position.
     *
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Checks whether the object argument is a Position and if so checks if the coordinates of
     * the argument match with the coordinates of the Position from which the method was called.
     *
     * @param obj Object that will be cast to a Position and checked.
     * @return Returns true if the positions are equal or false if the positions
     * are unequal or the argument is not a position.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Position)) {
            return false;
        } else {
            final Position pos = (Position) obj;
            return this.x == pos.getX() && this.y == pos.getY();
        }
    }
}