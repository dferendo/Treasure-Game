import java.util.Random;
import java.util.Scanner;

/**
 * @author Miguel Dingli
 */
public class Game {

    private static Game game = new Game();
    private final Scanner scanner = new Scanner(System.in);

    private int turns;
    private Player[] players = null;
    private Map map = null;

    private Game() {}

    public static Game getGameInstance() {
        return game;
    }

    public void setup() { }

    public void startGame() throws Exception { }

    private int setNumPlayers() {
        return 0;
    }

    private int setMapSize () {
        return 0;
    }

    private void setPlayers() { }

    private void generateHTMLFiles() { }

    private int getValidInt() {
        return 0;
    }
}