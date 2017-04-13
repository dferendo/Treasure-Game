import exceptions.GameWasNotInitialized;
import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static junit.framework.TestCase.fail;

/**
 * @author Miguel Dingli
 */
public class GameTest {

    private Game game;
    private final InputStream stdin = System.in;

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
        game = new Game(toInputStream("2\n5\n")); // 2 players and map of size 5
        game.setup();
    }

    @Test
    public void setup_validNumberOfPlayersAndMapSize_mediumMap() throws SizeOfMapWasNotSet {
        game = new Game(toInputStream("5\n8\n")); // 5 players and map of size 8
        game.setup();
    }

    @Test
    public void setup_validNumberOfPlayersAndMapSize_largeMap() throws SizeOfMapWasNotSet {
        game = new Game(toInputStream("8\n50\n")); // 8 players and map of size 50
        game.setup();
    }

    private InputStream toInputStream(final String input) {
        return new ByteArrayInputStream(input.getBytes());
    }

    @After
    public void end() {
        System.setIn(stdin); // Reset input stream
    }
}
