import exceptions.GameWasNotInitialized;
import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.fail;

/**
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

    private final String UP = "U\n", DN = "D\n", LT = "L\n", RT = "R\n";
    private final int FAIL_IF_TREASURE = 1, FAIL_IF_NO_TREASURE = 2;

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
        // Sets p=2, map=20, and two invalid moves (invalid character and invalid length)
        setInputStreamAndTryGameSetup("2\n20\nX\nXYZ\n");
        startGame(FAIL_IF_TREASURE);
        assertP1andP2Pos(defStartPos[0], defStartPos[1]); // assert that positions did not change
    }

    @Test
    public void move_topLeftLimit() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + UP + LT); // Sets p=2, map=20, and UP,LT
        move_twoMovesInCorner(new Position(0, 0));
        assertP1andP2Pos(cstStartPos[0], cstStartPos[1]); // assert that positions did not change
    }

    @Test
    public void move_topRightLimit() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + UP + RT); // Sets p=2, map=20, and UP,RT
        move_twoMovesInCorner(new Position(19, 0));
        assertP1andP2Pos(cstStartPos[0], cstStartPos[1]); // assert that positions did not change
    }

    @Test
    public void move_bottomLeftLimit() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + DN + LT); // Sets p=2, map=20, and DN,LT
        move_twoMovesInCorner(new Position(0, 19));
        assertP1andP2Pos(cstStartPos[0], cstStartPos[1]); // assert that positions did not change
    }

    @Test
    public void move_bottomRightLimit() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + DN + RT); // Sets p=2, map=20, and DN,RT
        move_twoMovesInCorner(new Position(19, 19));
        assertP1andP2Pos(cstStartPos[0], cstStartPos[1]); // assert that positions did not change
    }

    @Test
    public void move_moveOntoTreasureFromLeft() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + RT + RT);                     // Sets pl=2, map=20, and moves=R,R
        final Position tile = findTileWithGrassOnLeft(Map.TILE_TYPE.TREASURE);  // Find grass tile to left of treasure
        setStartPositions(new Position(tile.getX() - 1, tile.getY()));          // Move players to left of treasure
        startGame(FAIL_IF_NO_TREASURE);                                         // Start game (fail if no treasure)
        assertP1andP2Pos(tile, tile);                                           // assert that players now on treasure
    }

    @Test
    public void move_moveOntoWaterFromLeft() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + RT + RT);                 // Sets pl=2, map=20, and moves=R,R
        final Position tile = findTileWithGrassOnLeft(Map.TILE_TYPE.WATER); // Find grass tile to left of water
        setStartPositions(new Position(tile.getX() - 1, tile.getY()));      // Move players to left of water
        startGame(FAIL_IF_TREASURE);                                        // Start game (fail if treasure reached)
        assertP1andP2Pos(defStartPos[0], defStartPos[1]);                   // assert that players returned to start
    }

    @Test
    public void move_moveOntoGrassFromLeft() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + RT + RT);                 // Sets pl=2, map=20, and moves=R,R
        final Position tile = findTileWithGrassOnLeft(Map.TILE_TYPE.GRASS); // Find grass tile to left of grass
        setStartPositions(new Position(tile.getX() - 1, tile.getY()));      // Move players to left of grass
        startGame(FAIL_IF_TREASURE);                                        // Start game (fail if treasure reached)
        assertP1andP2Pos(tile, tile);                                       // assert that players now on grass
    }

    @Test
    public void move_moveUpDownLeftRight() throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n20\n" + UP + LT + DN + RT);   // Sets pl=2, map=20, p1=up+dn, p2=lt+rt
        final Position tile = findGrassWithGrassOnLeftAndUp();          // Find grass tile with grass on left and up
        setStartPositions(new Position(tile.getX(), tile.getY()));      // Move players to grass tile
        startGame(FAIL_IF_TREASURE);                                    // Start game (fail if treasure reached)
        assertP1andP2Pos(cstStartPos[0], cstStartPos[1]);               // assert that players returned to start
    }

    @Test
    public void enum_Testing() {
        Assert.assertTrue(Game.MOVE_DIRECTION.valueOf("RIGHT") == Game.MOVE_DIRECTION.RIGHT);
        Assert.assertTrue(Game.MOVE_DIRECTION.valueOf("UP") == Game.MOVE_DIRECTION.UP);
        Assert.assertTrue(Game.MOVE_DIRECTION.valueOf("LEFT") == Game.MOVE_DIRECTION.LEFT);
        Assert.assertTrue(Game.MOVE_DIRECTION.valueOf("DOWN") == Game.MOVE_DIRECTION.DOWN);
    }

    // Perform two moves in a corner to try and break bound
    private void move_twoMovesInCorner(final Position corner)
            throws GameWasNotInitialized, PositionIsOutOfRange, SizeOfMapWasNotSet {

        // Corner has to be grass
        do {
            map.generate();
        } while (map.getTileType(corner.getX(), corner.getY()) != Map.TILE_TYPE.GRASS);

        setStartPositions(corner); // Move players into corner
        startGame(FAIL_IF_TREASURE);
    }

    // Loop until there is a grass tile to the left of tile
    private Position findTileWithGrassOnLeft(final Map.TILE_TYPE tileToFind)
            throws PositionIsOutOfRange, SizeOfMapWasNotSet {

        do {
            // Find tile (skip first column since grass on left is needed)
            for (int x = 1; x < map.getMapSize(); x++) {
                for (int y = 0; y < map.getMapSize(); y++) {
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

    // Loop until grass with grass on left and up is found
    private Position findGrassWithGrassOnLeftAndUp() throws PositionIsOutOfRange, SizeOfMapWasNotSet {

        do {
            // Find grass (skip first column and row since grass on left and up is needed)
            for (int x = 1; x < map.getMapSize(); x++) {
                for (int y = 1; y < map.getMapSize(); y++) {
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

    private void setStartPositions(final Position newPosition) {

        cstStartPos = new Position[players.length];
        for (int i = 0; i < players.length; i++) {
            players[i].setPosition(newPosition);
            cstStartPos[i] = newPosition;
        }
    }

    private void assertP1andP2Pos(final Position p1Pos, final Position p2Pos) {

        Assert.assertTrue(players != null && players.length >= 2);
        Assert.assertTrue(players[0].getPosition().equals(p1Pos));
        Assert.assertTrue(players[1].getPosition().equals(p2Pos));
    }

    private void startGame(final int failInstruction) throws GameWasNotInitialized, PositionIsOutOfRange {

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
            fail("A position was out of range");
        }
    }
}
