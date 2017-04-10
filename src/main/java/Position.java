/**
 * @author Miguel Dingli
 */
public class Position {

    private int x, y;

    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(final Position pos) {
        return true;
    }
}