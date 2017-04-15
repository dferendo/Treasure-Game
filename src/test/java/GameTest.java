import exceptions.GameWasNotInitialized;
import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.fail;

/**
 * @author Miguel Dingli
 */
public class GameTest {

    private Game game;
    private final InputStream stdin = System.in;
    private final String UP = "U\n", DN = "D\n", LT = "L\n", RT = "R\n";

    private final int MAP_SIZE_FOR_MOVE_TESTS = 20;
    private final int FAIL_IF_TREASURE_FOUND = 1;
    private final int FAIL_IF_NO_TREASURE_FOUND = 2;

    @Test(expected = GameWasNotInitialized.class)
    public void startGame_startBeforeSetupCausesException() throws GameWasNotInitialized {

        game = new Game();
        try {
            game.startGame();
            fail("Game was successfully started before setup.");
        } catch (GameWasNotInitialized e1) {
            throw e1; // test successful
        } catch (PositionIsOutOfRange e2) {
            fail("A position was out of range.");
        }
    }

    @Test
    public void setup_validNumberOfPlayersAndMapSize_smallMap() throws SizeOfMapWasNotSet {
        // 2 players and map size of 5
        setInputStreamAndTryGameSetup("2\n5\n");
    }

    @Test
    public void setup_validNumberOfPlayersAndMapSize_mediumMap() throws SizeOfMapWasNotSet {
        // 5 players and map size of 8
        setInputStreamAndTryGameSetup("5\n8\n");
    }

    @Test
    public void setup_validNumberOfPlayersAndMapSize_largeMap() throws SizeOfMapWasNotSet {
        // 8 players and map size of 50
        setInputStreamAndTryGameSetup("8\n50\n");
    }

    @Test
    public void setup_invalidNumberOfPlayersFollowedByValidValues() throws SizeOfMapWasNotSet {
        // invalid (players: -1, 100, abc), followed by 2 players and map size of 5
        setInputStreamAndTryGameSetup("-1\n100\nabc\n2\n5\n");
    }

    @Test
    public void setup_validNumberOfPlayersAndInvalidMapSizeFollowedByValidValue() throws SizeOfMapWasNotSet {
        // 2 players, invalid (map sizes: -1, 100, abc), and a valid map size of 5
        setInputStreamAndTryGameSetup("2\n-1\n100\nabc\n5\n");
    }

    @Test
    public void setup_smallMapForMoreThanFourPlayers() throws SizeOfMapWasNotSet, IOException {
        // 5 players, invalid (map size: 5), and a valid map size of 8
        setInputStreamAndTryGameSetup("5\n5\n8\n");
    }

    @Test
    public void move_invalidDirection() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Sets players=2, mapSize=20, and set invalid move (invalid character, and invalid length)
        setInputStreamAndTryGameSetup("2\n" + MAP_SIZE_FOR_MOVE_TESTS + "\nx\nxyz\n");
        startGame(FAIL_IF_TREASURE_FOUND);
    }

    @Test
    public void move_topLeftLimit() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        move_twoMovesInCorner(new Position(0, 0), UP, LT);
    }

    @Test
    public void move_topRightLimit() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        move_twoMovesInCorner(new Position(MAP_SIZE_FOR_MOVE_TESTS - 1, 0), UP, RT);
    }

    @Test
    public void move_bottomLeftLimit() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        move_twoMovesInCorner(new Position(0, MAP_SIZE_FOR_MOVE_TESTS - 1), DN, LT);
    }

    @Test
    public void move_bottomRightLimit() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        move_twoMovesInCorner(new Position(MAP_SIZE_FOR_MOVE_TESTS - 1, MAP_SIZE_FOR_MOVE_TESTS - 1), DN, RT);
    }

    @Test
    public void move_moveOntoTreasureFromLeft() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + RT + RT);                     // Sets pl=2, map=20, and moves=R,R
        final Position tile = findTileWithGrassOnLeft(Map.TILE_TYPE.TREASURE);  // Find grass tile to left of treasure
        moveAllPlayers(new Position(tile.getX() - 1, tile.getY()));             // Move players to left of treasure
        startGame(FAIL_IF_NO_TREASURE_FOUND);                                   // Start game (fail if no treasure)
    }

    @Test
    public void move_moveOntoWaterFromLeft() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + RT + RT);                 // Sets pl=2, map=20, and moves=R,R
        final Position tile = findTileWithGrassOnLeft(Map.TILE_TYPE.WATER); // Find grass tile to left of water
        moveAllPlayers(new Position(tile.getX() - 1, tile.getY()));         // Move players to left of water
        startGame(FAIL_IF_TREASURE_FOUND);                                  // Start game (fail if treasure reached)
    }

    @Test
    public void move_moveOntoGrassFromLeft() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + RT + RT);                 // Sets pl=2, map=20, and moves=R,R
        final Position tile = findTileWithGrassOnLeft(Map.TILE_TYPE.GRASS); // Find grass tile to left of grass
        moveAllPlayers(new Position(tile.getX() - 1, tile.getY()));         // Move players to left of grass
        startGame(FAIL_IF_TREASURE_FOUND);                                  // Start game (fail if treasure reached)
    }

    @Test
    public void move_moveUpDownLeftRight() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + UP + LT + DN + RT);   // Sets pl=2, map=20, p1=up+dn, p2=lt+rt
        final Position tile = findGrassWithGrassOnLeftAndUp();          // Find grass tile with grass on left and up
        moveAllPlayers(new Position(tile.getX(), tile.getY()));         // Move players to grass tile
        startGame(FAIL_IF_TREASURE_FOUND);                              // Start game (fail if treasure reached)
    }

    // Perform two moves in a corner to try and break bound
    private void move_twoMovesInCorner(final Position corner, final String move1, final String move2)
            throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {

        // Sets players=2, mapSize=20, and set two moves
        setInputStreamAndTryGameSetup("2\n" + MAP_SIZE_FOR_MOVE_TESTS + "\n" + move1 + move2);

        // Corner has to be grass
        final Map map = game.getMap();
        do {
            map.generate();
        } while (map.getTileType(corner.getX(), corner.getY()) != Map.TILE_TYPE.GRASS);

        moveAllPlayers(corner); // Move players to corner
        startGame(FAIL_IF_TREASURE_FOUND);
    }

    // Loop until there is a grass tile to the left of tile
    private Position findTileWithGrassOnLeft(final Map.TILE_TYPE tileToFind)
            throws PositionIsOutOfRange, SizeOfMapWasNotSet {

        final Map map = game.getMap();
        Position pos;

        do {
            // Find tile
            pos = null;
            for (int x = 0; x < map.getMapSize(); x++) {
                for (int y = 0; y < map.getMapSize(); y++) {
                    if (map.getTileType(x, y) == tileToFind) {
                        pos = new Position(x, y);
                    }
                }
            }
            Assert.assertTrue(pos != null);

            // Check that not out of bounds and that there is grass to the left
            if (pos.getX() - 1 >= 0 && map.getTileType(pos.getX() - 1, pos.getY()) == Map.TILE_TYPE.GRASS) {
                return pos; // grass to the left of tile found
            } else {
                map.generate(); // re-generate the map
            }
        } while (true);
    }

    // Loop until grass with grass on left and up is found
    private Position findGrassWithGrassOnLeftAndUp() throws PositionIsOutOfRange, SizeOfMapWasNotSet {

        final Map map = game.getMap();
        do {
            for (int x = 0; x < map.getMapSize(); x++) {
                for (int y = 0; y < map.getMapSize(); y++) {
                    if (map.getTileType(x, y) == Map.TILE_TYPE.GRASS) {

                        // Check that not out of bounds and that there is grass to the left and up
                        if (x-1 >= 0 && y-1 >= 0
                                && map.getTileType(x-1, y) == Map.TILE_TYPE.GRASS
                                && map.getTileType(x, y-1) == Map.TILE_TYPE.GRASS) {
                            return new Position(x, y); // required grass tile found
                        }
                    }
                }
            }
            map.generate(); // re-generate the map
        } while (true);
    }

    private void setInputStreamAndTryGameSetup(final String input) {

        game = new Game(new ByteArrayInputStream(input.getBytes()));
        try {
            game.setup();
        } catch (GameWasNotInitialized e1) {
            fail("Game was not initialized.");
        } catch (SizeOfMapWasNotSet e2) {
            fail("Size of map was not set.");
        } catch (PositionIsOutOfRange e3) {
            fail("A position was out of range");
        }
    }

    private void moveAllPlayers(final Position newPosition) {

        for (final Player p : game.getPlayers()) {
            p.setPosition(newPosition);
        }
    }

    private void assertPlayersLocations(final Position positionCheck) {

        for (final Player p : game.getPlayers()) {
            Assert.assertTrue(p.getPosition() == positionCheck);
        }
    }

    private void startGame(final int failInstruction) throws GameWasNotInitialized, PositionIsOutOfRange {

        try {
            game.startGame();
            // This is the case when at least one of the players reached the treasure
            if (failInstruction == FAIL_IF_TREASURE_FOUND) {
                fail("At least one of the players reached the treasure.");
            }
        } catch (NoSuchElementException e) {
            // This is the case when game tries to get a further input (i.e. treasure not reached)
            if (failInstruction == FAIL_IF_NO_TREASURE_FOUND) {
                fail("None of the players reached the treasure.");
            }
        }
    }

    @After
    public void end() {
        System.setIn(stdin); // Reset input stream
    }
}
