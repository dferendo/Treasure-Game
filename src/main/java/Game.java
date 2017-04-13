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
 * @author Miguel Dingli
 * @author Dylan Frendo
 */
public class Game {

    private final File HTMLTemplateLocation = new File("src/main/resources/html-template/SoftEngineer.html");
    private final String playersMapLocation = "src/main/resources/players-maps/";
    private final File GitIgnoreLocation = new File("src/main/resources/players-maps/.gitignore");
    private final Scanner scanner;

    private int turns = 1;
    private Player[] players = null;
    private Map map = null;
    private List<Player> winners = new ArrayList<Player>();

    public Game() {
        this(System.in);
    }

    public Game(final InputStream in) {
        scanner = new Scanner(in);
    }

    public void setup() throws GameWasNotInitialized {
        setNumPlayers();
        setMapSize();
    }

    private void preStart() throws GameWasNotInitialized, SizeOfMapWasNotSet, PositionIsOutOfRange {

        // Check if either map or players array were not initialized
        if (map == null) {
            throw new GameWasNotInitialized("Map");
        } else if (players == null) {
            throw new GameWasNotInitialized("Players array");
        }

        // Generate map
        map.generate();

        // Initialize players and set their initial position
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i+1);
            map.setInitialPlayerPosition(players[i]);
        }
    }

    public void startGame() throws GameWasNotInitialized, SizeOfMapWasNotSet, PositionIsOutOfRange {

        // Some checks, map generation, and setting of players
        preStart();

        // Main game loop
        do {
            System.out.println("-----------------");
            System.out.println("Turn " + (turns++));
            System.out.println("-----------------");

            // Generate files and ask users for direction to move
            generateHTMLFiles();
            for (final Player p : players) {

                System.out.println("It's Player " + p.getID() + "'s turn.");
                Player.MOVE_DIRECTION dir;
                do {
                    System.out.println("Insert a direction to move: (U)p, (D)own, (L)eft, or (R)ight");
                    dir = getValidDirection();
                } while (!verifyDirectionAndMove(p, dir));
            }

            System.out.println();

            // Generate files and check where the players landed
            generateHTMLFiles();
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

    private void setNumPlayers() {

        final int MIN_PLAYERS = 2, MAX_PLAYERS = 8;
        final String NUM_PLAYERS_RANGE = "(" + MIN_PLAYERS + "-" + MAX_PLAYERS + ")";

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

    private void setMapSize () throws GameWasNotInitialized {

        final int MIN_MAP_SIZE = (players.length <= 4 ? 5 : 8), MAX_MAP_SIZE = 50;
        final String MAP_SIZE_RANGE = "(" + MIN_MAP_SIZE + "-" + MAX_MAP_SIZE + ")";
        map = new Map();

        // Check if players array was initialized
        if (players == null) {
            throw new GameWasNotInitialized("Players array");
        }

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

    private void generateHTMLFiles() {

        try {
            // .gitignore is still needed in the directory, thus re-write it after
            // cleaning the directory
            String gitIgnore = FileUtils.readFileToString(GitIgnoreLocation);
            FileUtils.cleanDirectory(new File(playersMapLocation));
            FileUtils.writeStringToFile(GitIgnoreLocation, gitIgnore);
            for (final Player player : players) {
                File playerFile = new File(playersMapLocation + "map_player_" + player.getID() + ".html");
                FileUtils.copyFile(HTMLTemplateLocation, playerFile);
                new HTMLGenerator(playerFile, map, player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PositionIsOutOfRange positionIsOutOfRange) {
            positionIsOutOfRange.printStackTrace();
        }
    }

    private int getValidInt() {

        while(!scanner.hasNextInt()) {
            System.out.println("The input was not a valid integer!");
            scanner.nextLine();
        }
        // nextLine() instead of nextInt() so that the '\n' gets read
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private Player.MOVE_DIRECTION getValidDirection() {

        final String ERROR_MESSAGE = "The input was not a valid direction! Valid directions: U, D, L, R.";
        do {
            final String input = scanner.nextLine();
            if (input.length() != 1) {
                System.out.println(ERROR_MESSAGE);
            } else {
                switch (input.charAt(0)) {
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

    private boolean verifyDirectionAndMove(final Player player, final Player.MOVE_DIRECTION dir) {

        final Position pos = player.getPosition();
        switch (dir) {
            case UP:
                if (pos.getY() > 0) {
                    return player.setPosition(new Position(pos.getX(), pos.getY() - 1));
                } break;
            case DOWN:
                if (pos.getY() < map.getMapSize() - 1) {
                    return player.setPosition(new Position(pos.getX(), pos.getY() + 1));
                } break;
            case LEFT:
                if (pos.getX() > 0) {
                    return player.setPosition(new Position(pos.getX() - 1, pos.getY()));
                } break;
            case RIGHT:
                if (pos.getX() < map.getMapSize() - 1) {
                    return player.setPosition(new Position(pos.getX() + 1, pos.getY()));
                } break;
        }
        System.out.println("Cannot go outside the map!");
        return false;
    }
}