import exceptions.GameWasNotInitialized;
import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static junit.framework.TestCase.fail;

/**
 * @author Miguel Dingli
 */
public class GameTest {

    private Game game;

    private final InputStream stdin = System.in;
    private InputStream customIn;

    @Test(expected = GameWasNotInitialized.class)
    public void startGame_startBeforeSetupCausesException() throws GameWasNotInitialized, PositionIsOutOfRange {

        game = new Game();
        try {
            game.startGame();
            fail("Game was successfully started before setup.");
        } catch (GameWasNotInitialized e1) {
            throw e1;
        } catch (SizeOfMapWasNotSet e2) {
            fail("Expected GameWasNotInitialized, not SizeOfMapWasNotSet.");
        }
    }

    @Test
    public void setup_validNumberOfPlayersAndMapSize_smallMap() throws SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("2\n5\n"); // 2 players and map size of 5
    }

    @Test
    public void setup_validNumberOfPlayersAndMapSize_mediumMap() throws SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("5\n8\n"); // 5 players and map size of 8
    }

    @Test
    public void setup_validNumberOfPlayersAndMapSize_largeMap() throws SizeOfMapWasNotSet {
        setInputStreamAndTryGameSetup("8\n50\n"); // 8 players and map size of 50
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

    private void setInputStreamAndTryGameSetup(final String input) {
        game = new Game(new ByteArrayInputStream(input.getBytes()));
        try {
            game.setup();
        } catch (GameWasNotInitialized e) {
            fail("Game was not initialized.");
        }
    }

    @After
    public void end() {
        System.setIn(stdin); // Reset input stream
    }
}
