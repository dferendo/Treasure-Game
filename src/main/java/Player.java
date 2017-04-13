import exceptions.InitialPlayerPositionWasNotSet;
import exceptions.PositionIsOutOfRange;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel Dingli
 */
public class Player {

    public enum MOVE_DIRECTION {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private int ID;
    private Position position = null;
    private List<Position> visited;

    public Player(int ID) {
        this.ID = ID;
        this.visited = new ArrayList<Position>();
    }

    public int getID() {
        return ID;
    }

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
            visited.add(position);
            return true;
        }
    }

    public Position getPosition() throws InitialPlayerPositionWasNotSet {

        if (position == null) {
            throw new InitialPlayerPositionWasNotSet();
        } else {
            return position;
        }
    }

    public boolean wasVisited(final int x, final int y) throws PositionIsOutOfRange {

        if (x < 0 || y < 0 || x >= Map.getSize() || y >= Map.getSize()) {
            throw new PositionIsOutOfRange(x, y);
        } else {
            final Position toCheck = new Position(x, y);
            for (final Position position : visited) {
                if (position.equals(toCheck)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void backToStartPosition() {
        setPosition(visited.get(0));
    }
}