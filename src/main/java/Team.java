import java.util.ArrayList;
import java.util.List;

public class Team {

    private List<Player> playerList = new ArrayList<Player>();

    public void addPlayer(final Player player) {
        playerList.add(player);
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}
