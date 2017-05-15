import exceptions.GameWasNotInitialized;
import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.fail;

/**
 * General game tests . Most tests in this class make use of an input stream to
 * simulate the input of the user. This requires each test that will make use of
 * the stream to include particular inputs that are expected by the game. These
 * are the following, where order is important:
 *
 * @author Miguel Dingli
 */
public class GameTest {

    // Set in setInputStreamAndTryGameSetup() method
    private Game game;
    private Map map;
    private Player players[];
    private Position defStartPos[] = null; // default start positions

    // Set in setStartPositions(...) method
    private Position cstStartPos[] = null; // custom start positions

    // Directions with newline (since read from input stream as line)
    private final String UP = "U\n", DN = "D\n", LT = "L\n", RT = "R\n";

    // Response to collaborative game mode with newline (since read from input stream as line)
    private final String NO = "0\n", YES = "1\n";

    // Response to map type with newline (since read from input steam as line)
    private final String SAFE = "0\n", HAZD = "1\n";

    // For clarity when passing the argument to the startGame() method
    private final boolean FAIL_IF_TREASURE = true, FAIL_IF_NO_TREASURE = false;

    @After
    public void tearDown() throws NoSuchFieldException, IllegalAccessException {
        // Clear Map instance
        Field instance = Map.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

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
        // 2 players, map type safe and map size of 5
        setInputStreamAndTryGameSetup(NO + "2\n" + SAFE + "5\n");
    }

    @Test
    public void setup_validNumberOfPlayersMapTypeSafeAndValidMapSize() throws SizeOfMapWasNotSet {
        // 5 players, map type safe and map size of 8
        setInputStreamAndTryGameSetup(NO + "5\n" + SAFE + "8\n");
        Assert.assertThat(map, CoreMatchers.instanceOf(SafeMap.class));
    }

    @Test
    public void setup_validNumberOfPlayersMapTypeHazardousAndValidMapSize() throws SizeOfMapWasNotSet {
        // 5 players, map type hazardous and map size of 8
        setInputStreamAndTryGameSetup(NO + "5\n" + HAZD + "8\n");
        Assert.assertThat(map, CoreMatchers.instanceOf(HazardousMap.class));
    }

    @Test
    public void setup_validNumberOfPlayersInvalidMapTypeValidMapTypeAndValidMapSize() throws SizeOfMapWasNotSet {
        // 5 players, map type safe and map size of 8
        setInputStreamAndTryGameSetup(NO + "5\nabd\n-1\n" + HAZD + "8\n");
        Assert.assertThat(map, CoreMatchers.instanceOf(HazardousMap.class));
    }

    @Test
    public void setup_validNumberOfPlayersAndMapSize_mediumMap() throws SizeOfMapWasNotSet {
        // 5 players, map type safe and map size of 8
        setInputStreamAndTryGameSetup(NO + "5\n" + SAFE + "8\n");
    }

    @Test
    public void setup_validNumberOfPlayersAndMapSize_largeMap() throws SizeOfMapWasNotSet {
        // 8 players, map type safe,  map size of 50
        setInputStreamAndTryGameSetup(NO + "8\n" + SAFE + "50\n");
    }

    @Test
    public void setup_invalidNumberOfPlayersFollowedByValidValues() throws SizeOfMapWasNotSet {
        // invalid (players: -1, 100, abc), followed by 2 players, map type safe and map size of 5
        setInputStreamAndTryGameSetup(NO + "-1\n100\nabc\n2\n" + SAFE + "5\n");
    }

    @Test
    public void setup_validNumberOfPlayersAndInvalidMapSizeFollowedByValidValue() throws SizeOfMapWasNotSet {
        // 2 players, invalid (map sizes: -1, 100, abc), map type safe and a valid map size of 5
        setInputStreamAndTryGameSetup(NO + "2\n-1\n100\nabc\n" + SAFE + "5\n");
    }

    @Test
    public void setup_smallMapForMoreThanFourPlayers() throws SizeOfMapWasNotSet, IOException {
        // 5 players, invalid (map size: 5), map type safe, and a valid map size of 8
        setInputStreamAndTryGameSetup(NO + "5\n5\n" + SAFE + "8\n");
    }

    @Test
    public void setup_yesTeams_twoInvalidInputFollowedByValidValues() throws SizeOfMapWasNotSet, IOException {
        // Invalid -1 and 100 followed by YES for collaborative and 2 teams, 2 players, map type safe and map size of 5
        setInputStreamAndTryGameSetup("-1\n100\n" + YES + "2\n2\n" + SAFE + "5\n");
    }

    @Test
    public void setup_yesTeams_invalidNumberOfTeamsFollowedByValidValues() throws SizeOfMapWasNotSet, IOException {
        // YES for teams, followed by invalid -1 and 100 teams, 2 teams, 2 players, map type safe and map size of 5
        setInputStreamAndTryGameSetup(YES + "-1\n100\n2\n2\n" + SAFE + "5\n");
    }

    @Test
    public void move_invalidDirection() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Sets p=2, map type = 0, map=20, and two invalid moves (invalid character and invalid length)
        setInputStreamAndTryGameSetup(NO + "2\n" + SAFE + "20\nX\nXYZ\n");
        startGame(FAIL_IF_TREASURE);
        assertP1andP2Pos(defStartPos[0], defStartPos[1]); // assert that positions did not change
    }

    @Test
    public void move_intoTopLeftCorner() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Players = 2, Type = 0, Map size = 20, and Moves = UP and LT
        setInputStreamAndTryGameSetup(NO + "2\n" + SAFE + "20\n" + UP + LT);
        twoMovesInCorner(new Position(0, 0));
        assertP1andP2Pos(cstStartPos[0], cstStartPos[1]); // assert that positions did not change
    }

    @Test
    public void move_intoTopRightCorner() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Players = 2, Type = 0, Map size = 20, and Moves = UP and RT
        setInputStreamAndTryGameSetup(NO + "2\n" + SAFE + "20\n" + UP + RT);
        twoMovesInCorner(new Position(19, 0));
        assertP1andP2Pos(cstStartPos[0], cstStartPos[1]); // assert that positions did not change
    }

    @Test
    public void move_intoBottomLeftCorner() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Players = 2, Type = 0, Map size = 20, and Moves = DN and LT
        setInputStreamAndTryGameSetup(NO + "2\n" + SAFE + "20\n" + DN + LT);
        twoMovesInCorner(new Position(0, 19));
        assertP1andP2Pos(cstStartPos[0], cstStartPos[1]); // assert that positions did not change
    }

    @Test
    public void move_intoBottomRightCorner() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Players = 2, Type = 0, Map size = 20, and Moves = DN and RT
        setInputStreamAndTryGameSetup(NO + "2\n" + SAFE + "20\n" + DN + RT);
        twoMovesInCorner(new Position(19, 19));
        assertP1andP2Pos(cstStartPos[0], cstStartPos[1]); // assert that positions did not change
    }

    @Test
    public void move_ontoTreasureFromLeft() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Players = 2, Type = 0, Map size = 20, and Moves = RT and RT
        setInputStreamAndTryGameSetup(NO + "2\n" + SAFE + "20\n" + RT + RT);
        final Position tile = findTileWithGrassOnLeft(Map.TILE_TYPE.TREASURE); // Find grass tile to left of treasure
        setStartPositions(new Position(tile.getX() - 1, tile.getY()));         // Move players to left of treasure
        startGame(FAIL_IF_NO_TREASURE);                                        // Start game (fail if no treasure)
        assertP1andP2Pos(tile, tile);                                          // assert that players now on treasure
    }

    @Test
    public void move_ontoWaterFromLeft() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Players = 2, Type = 0, Map size = 20, and Moves = RT and RT
        setInputStreamAndTryGameSetup(NO + "2\n" + SAFE + "20\n" + RT + RT);
        final Position tile = findTileWithGrassOnLeft(Map.TILE_TYPE.WATER);  // Find grass tile to left of water
        setStartPositions(new Position(tile.getX() - 1, tile.getY()));       // Move players to left of water
        startGame(FAIL_IF_TREASURE);                                         // Start game (fail if treasure reached)
        assertP1andP2Pos(defStartPos[0], defStartPos[1]);                    // assert that players returned to start
    }

    @Test
    public void move_ontoGrassFromLeft() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Players = 2, Type = 0, Map size = 20, and Moves = RT and RT
        setInputStreamAndTryGameSetup(NO + "2\n" + SAFE + "20\n" + RT + RT);
        final Position tile = findTileWithGrassOnLeft(Map.TILE_TYPE.GRASS);  // Find grass tile to left of grass
        setStartPositions(new Position(tile.getX() - 1, tile.getY()));       // Move players to left of grass
        startGame(FAIL_IF_TREASURE);                                         // Start game (fail if treasure reached)
        assertP1andP2Pos(tile, tile);                                        // assert that players now on grass
    }

    @Test
    public void move_noTeams_upDownLeftRight() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Players = 2, Type = 0, Map size = 20, and Player 1 = UP and DN, and Player 2 = LT and RT
        setInputStreamAndTryGameSetup(NO + "2\n" + SAFE + "20\n" + UP + LT + DN + RT);
        final Position tile = findGrassWithGrassOnLeftAndUp();      // Find grass tile with grass on left and up
        setStartPositions(new Position(tile.getX(), tile.getY()));  // Move players to grass tile
        startGame(FAIL_IF_TREASURE);                                // Start game (fail if treasure reached)
        assertP1andP2Pos(cstStartPos[0], cstStartPos[1]);           // assert that players returned to start
    }

    @Test
    public void move_yesTeams_upDownLeftRight() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Teams=7, Players = 2, Type = 0, Map size = 20, and Player 1 = UP and DN, Player 2 = LT and RT
        setInputStreamAndTryGameSetup(YES + "7\n2\n" + SAFE + "20\n" + UP + LT + DN + RT);
        final Position tile = findGrassWithGrassOnLeftAndUp();      // Find grass tile w/ grass left and up
        setStartPositions(new Position(tile.getX(), tile.getY()));  // Move players to grass tile
        startGame(FAIL_IF_TREASURE);                                // Start game (fail if treasure reached)
        assertP1andP2Pos(cstStartPos[0], cstStartPos[1]);           // assert that players returned to start
    }

    @Test
    public void enum_Testing() {
        Assert.assertTrue(Game.MOVE_DIRECTION.valueOf("RIGHT") == Game.MOVE_DIRECTION.RIGHT);
        Assert.assertTrue(Game.MOVE_DIRECTION.valueOf("UP") == Game.MOVE_DIRECTION.UP);
        Assert.assertTrue(Game.MOVE_DIRECTION.valueOf("LEFT") == Game.MOVE_DIRECTION.LEFT);
        Assert.assertTrue(Game.MOVE_DIRECTION.valueOf("DOWN") == Game.MOVE_DIRECTION.DOWN);
    }

    /**
     * Helper Method #1.
     * <p>
     * Performs two moves in a position expected to be a corner in the map. Also sets the start
     * start positions of the players to the corner and starts the game. An appropriate input
     * stream is expected to have been set before calling this method.
     */
    private void twoMovesInCorner(final Position corner)
            throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {

        Assume.assumeTrue(corner.getX() == 0 || corner.getX() == Map.getSize() - 1);
        Assume.assumeTrue(corner.getY() == 0 || corner.getY() == Map.getSize() - 1);

        // Loop until corner is grass
        do {
            map.generate();
        } while (map.getTileType(corner.getX(), corner.getY()) != Map.TILE_TYPE.GRASS);

        setStartPositions(corner);
        startGame(FAIL_IF_TREASURE);
    }

    /**
     * Helper Method #2.
     * <p>
     * Loop until there is a grass tile to the left of the tile to be found and returns the
     * position of the tile, not the grass tile.
     */
    private Position findTileWithGrassOnLeft(final Map.TILE_TYPE tileToFind)
            throws PositionIsOutOfRange, SizeOfMapWasNotSet {

        // Loop and regenerate map if a tile of the specified type was not found
        do {
            // Find tile (skip first column since grass on left is needed)
            for (int x = 1; x < Map.getSize(); x++) {
                for (int y = 0; y < Map.getSize(); y++) {
                    if (map.getTileType(x, y) == tileToFind) {

                        // Check that there is grass to the left
                        if (map.getTileType(x - 1, y) == Map.TILE_TYPE.GRASS) {
                            return new Position(x, y); // required tile found
                        }
                    }
                }
            }
            map.generate(); // re-generate the map
        } while (true);
    }

    /**
     * Helper Method #3.
     * <p>
     * Loop until a grass tile with grass tiles to the left and up from it is found. The position
     * of the reference grass tile is returned.
     */
    private Position findGrassWithGrassOnLeftAndUp() throws PositionIsOutOfRange, SizeOfMapWasNotSet {

        // Loop and regenerate map if a grass tile satisfying the conditions was not found
        do {
            // Find grass (skip first column and row since grass on left and up is needed)
            for (int x = 1; x < Map.getSize(); x++) {
                for (int y = 1; y < Map.getSize(); y++) {
                    if (map.getTileType(x, y) == Map.TILE_TYPE.GRASS) {

                        // Check that there is grass to the left and up
                        if (map.getTileType(x - 1, y) == Map.TILE_TYPE.GRASS
                                && map.getTileType(x, y - 1) == Map.TILE_TYPE.GRASS) {
                            return new Position(x, y); // required grass tile found
                        }
                    }
                }
            }
            map.generate(); // re-generate the map
        } while (true);
    }

    /**
     * Helper Method #4.
     * <p>
     * Sets the start positions of all the players to the specified position.
     */
    private void setStartPositions(final Position newPosition) {

        cstStartPos = new Position[players.length];
        for (int i = 0; i < players.length; i++) {
            players[i].setPosition(newPosition);
            cstStartPos[i] = newPosition;
        }
    }

    /**
     * Helper Method #5.
     * <p>
     * Asserts that the positions of players 1 and 2 are equal to the specified positions.
     */
    private void assertP1andP2Pos(final Position p1Pos, final Position p2Pos) {

        Assert.assertTrue(players != null && players.length >= 2);
        Assert.assertTrue(players[0].getPosition().equals(p1Pos));
        Assert.assertTrue(players[1].getPosition().equals(p2Pos));
    }

    /**
     * Helper Method #6.
     * <p>
     * Start game and fail according to the failInstruction (i.e. if treasure found or if
     * not found). Game throws a NoSuchElementException when it has exhausted the input,
     * which indicates that none of the players had reached the treasure by that point.
     */
    private void startGame(final boolean failInstruction) throws GameWasNotInitialized, PositionIsOutOfRange {

        try {
            game.startGame();
            // This is the case when at least one of the players reached the treasure
            if (failInstruction == FAIL_IF_TREASURE) {
                fail("At least one of the players reached the treasure.");
            }
        } catch (NoSuchElementException e) {
            // This is the case when game tries to get a further input (i.e. treasure not reached)
            if (failInstruction == FAIL_IF_NO_TREASURE) {
                fail("None of the players reached the treasure.");
            }
        }
    }

    /**
     * Helper Method #7.
     * <p>
     * Set the input stream to be used to simulate the user input and attempt to set up the game.
     */
    private void setInputStreamAndTryGameSetup(final String input) {

        game = new Game(new ByteArrayInputStream(input.getBytes()));
        try {
            // Set-up game
            game.setup();

            // Get map, players, and set positions before
            map = game.getMap();
            players = game.getPlayers();
            defStartPos = new Position[players.length];
            for (int i = 0; i < players.length; i++) {
                defStartPos[i] = players[i].getPosition();
            }
        } catch (GameWasNotInitialized e1) {
            fail("Game was not initialized.");
        } catch (SizeOfMapWasNotSet e2) {
            fail("Size of map was not set.");
        } catch (PositionIsOutOfRange e3) {
            fail("A position was out of range.");
        } catch (NoSuchElementException e4) {
            fail("Not enough valid input was present.");
        }
    }
}
