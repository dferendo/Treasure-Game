import java.util.ArrayList;
import java.util.List;

/**
 * Represents a team in the game. It stores the team's ID and a list of all the players in
 * the team. Besides a getter for the ID and the players list, it also provides functions
 * to add a player to the team (used in the game setup), and since this class makes use of
 * a Mediator design pattern, it also has a send(...) method that is used by players to
 * send a position to their team so that it is distributed to the other players in the team.
 */
public class Team {

    /**
     * Team ID and list of players in the team. A new player is
     * added to this list by using the addPlayer(...) method.
     */
    private final int ID;
    private final List<Player> playerList = new ArrayList<Player>();

    /**
     * Constructor which simply sets the ID of the team.
     *
     * @param ID Team ID.
     */
    public Team(int ID) {
        this.ID = ID;
    }

    /**
     * Returns the team ID.
     *
     * @return Team ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Adds the specified player to the team (i.e. to the players list).
     *
     * @param player Player to be added.
     */
    public void addPlayer(final Player player) {
        playerList.add(player);
    }

    /**
     * Returns the list of players in the team.
     *
     * @return Players list.
     */
    public List<Player> getPlayerList() {
        return playerList;
    }

    /**
     * The Mediator method used to distribute a new position that a player in the
     * team visited. The player object supplies the position and a reference to
     * itself so that the position is sent to all players except for this player.
     *
     * @param position New position to be distributed.
     * @param player Player that visited the new position.
     */
    public void send(final Position position, final Player player) {

        for (final Player p : playerList) {
            if (p.getID() != player.getID()) {
                p.addPosition(position);
            }
        }
    }
}
