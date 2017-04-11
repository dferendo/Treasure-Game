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

    public void setup() {
        
        map = new Map();
        setNumPlayers();
        setMapSize();
        setPlayers();
    }

    public void startGame() throws Exception {

        if (players == null) {
            throw new Exception("Players array was not initialized.");
        } else if (map == null) {
            throw new Exception("Map was not initialized.");
        }

        // Attempt to generate map
        try {
            map.generate();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // End game
        players = null;
        map = null;
    }

    private void setNumPlayers() {

        final int MIN_PLAYERS = 2, MAX_PLAYERS = 8;
        final String NUM_PLAYERS_RANGE = "(" + MIN_PLAYERS + "-" + MAX_PLAYERS + ")";

        int numPlayers;
        while (true) {
            System.out.println("How many players will be playing? " + NUM_PLAYERS_RANGE);
            numPlayers = getValidInt();

            if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
                System.out.println("The input value was out of the range " + NUM_PLAYERS_RANGE + ".");
            } else {
                players = new Player[numPlayers];
                break;
            }
        }
    }

    private void setMapSize () {

        final int MIN_MAP_SIZE = (players.length <= 4 ? 5 : 8), MAX_MAP_SIZE = 50;
        final String MAP_SIZE_RANGE = "(" + MIN_MAP_SIZE + "-" + MAX_MAP_SIZE + ")";

        int mapSize;
        while (true) {
            System.out.println("What will be the size of the map? " + MAP_SIZE_RANGE);
            mapSize = getValidInt();

            if (!map.setMapSize(players.length, mapSize)) {
                System.out.println("The input value was out of the range " + MAP_SIZE_RANGE + ".");
            } else {
                break;
            }
        }
    }

    private void setPlayers() {
        
        final Random rand = new Random();
        for (int i = 0; i < players.length; i++) {
            final Position startPos = new Position(rand.nextInt(map.getMapSize()), rand.nextInt(map.getMapSize()));
            players[i] = new Player(i+1);
            players[i].setPosition(startPos);
            try {
                map.setInitialPlayerPosition(startPos);
            } catch(Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void generateHTMLFiles() { }

    private int getValidInt() {

        while(!scanner.hasNext()) {
            System.out.println("The input was not a valid integer!");
            scanner.next();
        }
        return scanner.nextInt();
    }
}