/**
 * @author Miguel Dingli
 */
public class Player {

    public enum MOVE_DIRECTION {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    private Position position;

    public void move(final MOVE_DIRECTION direction) {

        // One of these will be changed below
        int newX = position.getX();
        int newY = position.getY();

        // Change a coordinate depending on direction
        switch (direction) {
            case UP:
                newY--;
                break;
            case DOWN:
                newY++;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
        }

        // Set position based on new coordinates
        setPosition(new Position(newX, newY));
    }

    public boolean setPosition(final Position p) {

        if (p == null) {
            return false;
        } else {
            position = p;
            return true;
        }
    }

    public Position getPosition() {
        return position;
    }
}