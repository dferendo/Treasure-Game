import java.util.ArrayList;
import java.util.List;

public class Team {

    private final int ID;
    private final List<Player> playerList = new ArrayList<Player>();

    public Team(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void addPlayer(final Player player) {
        playerList.add(player);
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void send(final Position position, final Player player) {}
}
