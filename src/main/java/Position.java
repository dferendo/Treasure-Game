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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Position)) {
            return false;
        } else {
            final Position pos = (Position) obj;
            return x == pos.getX() && y == pos.getY();
        }
    }
}