import exceptions.InitialPlayerPositionWasNotSet;
import exceptions.PositionIsOutOfRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game. It stores the player's ID, where the player current
 * is and where the player has been. Also provides functions to make the player move, to
 * set and get the current position, to check if a position was visited, and to return
 * the player to the starting position (for when the player hits a water tile).
 *
 * @author Miguel Dingli
 */
public class Player {

    /**
     * Player ID, current position, and list of visited positions. The list is kept so
     * the the game can check which tiles to keep uncovered for the player.
     */
    private int ID;
    private Position position = null;
    private List<Position> visited;
    private Team team = null;

    /**
     * Constructor that only requires the player ID as an argument. It sets the ID
     * and initializes the list of visited positions to an empty list.
     *
     * @param ID Player ID.
     */
    public Player(final int ID) {
        this.ID = ID;
        this.visited = new ArrayList<Position>();
    }

    /**
     * Constructor that also requires the team as an argument, besides the ID. It
     * calls the other constructor, sets the team, and adds the player to it.
     *
     * @param ID Player ID.
     * @param team Team that player will be placed in.
     */
    public Player(final int ID, final Team team) {
        this(ID);
        this.team = team;
        this.team.addPlayer(this);
    }

    /**
     * Returns the player ID.
     *
     * @return Player ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns the player's team (null if no team).
     *
     * @return Team.
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Sets the current position of the player to the exact specified position, irrelevant
     * to where the player currently is. Besides setting the position, the new position is
     * added to the list of visited positions.
     *
     * @param p The position that the current player position will be set to.
     * @return True if the position is successfully set or false if the position is null.
     */
    public boolean setPosition(final Position p) {

        if (p == null) {
            return false;
        } else {
            position = p;
            visited.add(position);
            if (team != null) {
                team.send(position, this);
            }
            return true;
        }
    }

    /**
     * Adds a position to the player's visited list without moving the position of the player.
     * This is used by the team when it distributes a new position explored by a team member.
     *
     * @param p The position to be added.
     * @return True if the position is successfully added or false if the position is null.
     */
    public boolean addPosition(final Position p) {

        if (p == null) {
            return false;
        } else {
            visited.add(p);
            return true;
        }
    }

    /**
     * Returns the current position of the player.
     *
     * @return Current position of the player.
     * @throws InitialPlayerPositionWasNotSet The position was never set.
     */
    public Position getPosition() throws InitialPlayerPositionWasNotSet {

        if (position == null) {
            throw new InitialPlayerPositionWasNotSet();
        } else {
            return position;
        }
    }

    /**
     * Checks if the specified coordinates were ever visited by the player using the
     * list of visited positions and trying to match the coordinates with a position.
     *
     * @param x x coordinate of the position to check.
     * @param y y coordinate of the position to check.
     * @return True if the position was visited, or false otherwise.
     * @throws PositionIsOutOfRange Position violates the bounds of the map.
     */
    public boolean wasVisited(final int x, final int y) throws PositionIsOutOfRange {

        // Check that the map bounds are not violated
        if (x < 0 || y < 0 || x >= Map.getSize() || y >= Map.getSize()) {
            throw new PositionIsOutOfRange(x, y);
        } else {
            // Set the coordinates as a position and compare to all visited positions
            final Position toCheck = new Position(x, y);
            for (final Position position : visited) {
                if (position.equals(toCheck)) {
                    return true;
                }
            }
            return false; // location not visited
        }
    }

    /**
     * Sets the player's position to the start position. Using the list of visited
     * tiles, the start position is the first position inserted in the list.
     */
    public void backToStartPosition() {
        setPosition(visited.get(0));
    }
}