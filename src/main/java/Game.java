import exceptions.GameWasNotInitialized;
import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a single game consisting of a turns counter, a list of players, a map, and a
 * list of winners. The list of winners is only populated when the game ends, which is at
 * the immediate point when at least one player reaches the treasure.
 *
 * @author Miguel Dingli
 * @author Dylan Frendo
 */
public class Game {

    /**
     * Locations of files and a directory.
     */
    private final File HTMLTemplateLocation = new File("src/main/resources/html-template/SoftEngineer.html");
    private final String playersMapLocation = "src/main/resources/players-maps/";
    private final File GitIgnoreLocation = new File("src/main/resources/players-maps/.gitignore");

    /**
     * Scanner used during the game to read from the input stream specified in the constructor.
     */
    private final Scanner scanner;

    /**
     * Current turn, list of players, the map, and a list of winners.
     */
    private int turns = 1;
    private Player[] players = null;
    private Map map = null;
    private List<Player> winners = new ArrayList<Player>();

    /**
     * Constructor for the game which sets the input stream to the standard input.
     */
    public Game() {
        this(System.in);
    }

    /**
     * Constructor for the game which allows for a custom input stream to be used
     * by the game Scanner instead of standard input. This is mostly intended to
     * be used to be able to simulate user input in the tests.
     *
     * @param in The custom input stream to be read.
     */
    public Game(final InputStream in) {
        scanner = new Scanner(in);
    }

    /**
     * Sets up various aspects of the game. This method must be called before the startGame()
     * method so that the game is set up before starting it. Set up includes setting of the
     * number of players (and hence the players array), the map size (and hence the map object),
     * generation of the map, and initializing and moving the players to their start positions.
     * Exceptions are simply thrown since they should not occur because of this method.
     *
     * @throws GameWasNotInitialized Thrown by setMapSize() if the number of player was not set
     * since the minimum size of the map depends on the number of players.
     * @throws SizeOfMapWasNotSet Thrown by map.generate() and map.setInitialPlayerPosition(...)
     * if the size of the map was not set since these operations depend on the map size.
     * @throws PositionIsOutOfRange Thrown by map.setInitialPlayerPosition(...) if the position
     * specified violates the map bounds.
     */
    public void setup() throws GameWasNotInitialized, SizeOfMapWasNotSet, PositionIsOutOfRange {

        // Set number of players and map size
        setNumPlayers();
        setMapSize();

        // Generate map
        map.generate();

        // Initialize players and set their initial position
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i + 1);
            map.setInitialPlayerPosition(players[i]);
        }
    }

    /**
     * Starts the main game loop consisting of a loop to get the movement directions from the
     * input stream, a loop to indicate where the players landed. The main game loop terminates
     * once at least one player reaches the treasure. The list of winners is then output.
     *
     * @throws GameWasNotInitialized If either the map or players array are null.
     * @throws PositionIsOutOfRange Thrown by some functions that take a position as argument.
     */
    public void startGame() throws GameWasNotInitialized, PositionIsOutOfRange {

        // Check if either map or players array were not initialized
        if (map == null) {
            throw new GameWasNotInitialized("Map");
        } else if (players == null) {
            throw new GameWasNotInitialized("Players array");
        }

        // Main game loop
        do {
            System.out.println("-----------------");
            System.out.println("Turn " + (turns++));
            System.out.println("-----------------");

            // Generate output files
            generateHTMLFiles();

            // Ask users for direction to move
            for (final Player p : players) {

                System.out.println("It's Player " + p.getID() + "'s turn.");
                Player.MOVE_DIRECTION dir;
                do {
                    System.out.println("Insert a direction to move: (U)p, (D)own, (L)eft, or (R)ight");
                    dir = getValidDirection();
                } while (!verifyDirectionAndMove(p, dir));
            }

            // Separator
            System.out.println();

            // Check where the players landed
            for (final Player p : players) {
                final Position pos = p.getPosition();

                switch (map.getTileType(pos.getX(), pos.getY())) {
                    case TREASURE:
                        System.out.println("Player " + p.getID() + " landed on the treasure!");
                        winners.add(p);
                        break;
                    case GRASS:
                        System.out.println("Player " + p.getID() + " landed on grass!");
                        break;
                    case WATER:
                        System.out.println("Player " + p.getID() + " landed on water!");
                        p.backToStartPosition();
                        break;
                }
            }
        } while (winners.size() == 0);

        // Generate files so that final moves shown
        generateHTMLFiles();

        // At least one player landed on the treasure
        System.out.println("\nWINNERS");
        System.out.println("-------");
        for (Player p : winners) {
            System.out.println("Player " + p.getID());
        }

        // End game
        winners.clear();
        players = null;
        map = null;
    }

    /**
     * Helper method to set the number of players in the game. This also initializes the players
     * array as an empty array which will be populated in the setup() method.
     */
    private void setNumPlayers() {

        // Minimum and maximum players, and a range in string form
        final int MIN_PLAYERS = 2, MAX_PLAYERS = 8;
        final String NUM_PLAYERS_RANGE = "(" + MIN_PLAYERS + "-" + MAX_PLAYERS + ")";

        // Loop until a valid number of players is obtained
        while (true) {
            System.out.println("How many players will be playing? " + NUM_PLAYERS_RANGE);
            final int numPlayers = getValidInt();

            if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
                System.out.println("The input value was out of the range " + NUM_PLAYERS_RANGE + ".");
            } else {
                players = new Player[numPlayers];
                break;
            }
        }
    }

    /**
     * Helper method to set the size of the map.
     *
     * @throws GameWasNotInitialized Thrown if the players array is null.
     */
    private void setMapSize() throws GameWasNotInitialized {

        // Check if players array was initialized
        if (players == null) {
            throw new GameWasNotInitialized("Players array");
        }

        // Minimum (based on players) and maximum map size and a range in string form
        final int MIN_MAP_SIZE = (players.length <= 4 ? 5 : 8), MAX_MAP_SIZE = 50;
        final String MAP_SIZE_RANGE = "(" + MIN_MAP_SIZE + "-" + MAX_MAP_SIZE + ")";

        map = new Map();

        // Loop until a valid map size is obtained
        while (true) {
            System.out.println("What will be the size of the map? " + MAP_SIZE_RANGE);
            final int mapSize = getValidInt();

            if (!map.setMapSize(mapSize, mapSize, players.length)) {
                System.out.println("The input value was out of the range " + MAP_SIZE_RANGE + ".");
            } else {
                break;
            }
        }
    }

    /**
     * Generates the HTML Files to all players with respect to their current map.
     */
    private void generateHTMLFiles() {

        String gitIgnore;
        File playerFile;

        try {
            // .gitignore is still needed in the directory, thus re-write it after
            // cleaning the directory
            gitIgnore = FileUtils.readFileToString(GitIgnoreLocation);
            FileUtils.cleanDirectory(new File(playersMapLocation));
            FileUtils.writeStringToFile(GitIgnoreLocation, gitIgnore);
            for (final Player player : players) {
                playerFile = new File(playersMapLocation + "map_player_" + player.getID() + ".html");
                FileUtils.copyFile(HTMLTemplateLocation, playerFile);
                new HTMLGenerator(playerFile, map, player);
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    /**
     * Helper method that loops until the input is a valid integer.
     *
     * @return The valid integer obtained from the input stream.
     */
    private int getValidInt() {

        while (!scanner.hasNextInt()) {
            System.out.println("The input was not a valid integer!");
            scanner.nextLine();
        }
        // nextLine() instead of nextInt() so that the '\n' gets read
        return Integer.parseInt(scanner.nextLine().trim());
    }

    /**
     * Helper method that loops until a valid direction is obtained.
     *
     * @return The valid direction using the MOVE_DIRECTION enum.
     */
    private Player.MOVE_DIRECTION getValidDirection() {

        final String ERROR_MESSAGE = "The input was not a valid direction! Valid directions: U, D, L, R.";

        // Loop until a valid direction is obtained
        do {
            final String input = scanner.nextLine();
            if (input.length() != 1) {
                System.out.println(ERROR_MESSAGE);
            } else {
                switch (Character.toUpperCase(input.charAt(0))) {
                    case 'U':
                        return Player.MOVE_DIRECTION.UP;
                    case 'D':
                        return Player.MOVE_DIRECTION.DOWN;
                    case 'L':
                        return Player.MOVE_DIRECTION.LEFT;
                    case 'R':
                        return Player.MOVE_DIRECTION.RIGHT;
                    default:
                        System.out.println(ERROR_MESSAGE);
                }
            }
        } while (true);
    }

    /**
     * Verifies that the player can move in the specified direction and if so calls the
     * appropriate method to move the player. Otherwise indicates that the player cannot
     * go outside of the map.
     *
     * @param player
     * @param dir
     * @return False if a movement in the specified direction makes the player go out of
     * the map bounds, or otherwise the return value of the setPosition(...) method.
     */
    private boolean verifyDirectionAndMove(final Player player, final Player.MOVE_DIRECTION dir) {

        final Position pos = player.getPosition();
        switch (dir) {
            case UP:
                if (pos.getY() > 0) {
                    return player.setPosition(new Position(pos.getX(), pos.getY() - 1));
                }
                break;
            case DOWN:
                if (pos.getY() < map.getMapSize() - 1) {
                    return player.setPosition(new Position(pos.getX(), pos.getY() + 1));
                }
                break;
            case LEFT:
                if (pos.getX() > 0) {
                    return player.setPosition(new Position(pos.getX() - 1, pos.getY()));
                }
                break;
            case RIGHT:
                if (pos.getX() < map.getMapSize() - 1) {
                    return player.setPosition(new Position(pos.getX() + 1, pos.getY()));
                }
                break;
        }
        System.out.println("Cannot go outside the map!");
        return false;
    }

    /**
     * Returns the players array.
     *
     * @return The players array.
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Returns the map.
     *
     * @return The map.
     */
    public Map getMap() {
        return map;
    }
}