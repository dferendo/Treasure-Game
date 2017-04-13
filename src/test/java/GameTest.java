import exceptions.GameWasNotInitialized;
import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 * @author Miguel Dingli
 */
public class GameTest {

    private Game game;

    @Before
    public void init() {
        game = new Game();
    }

    @Test(expected = GameWasNotInitialized.class)
    public void startGame_startBeforeSetupCausesException() throws GameWasNotInitialized, PositionIsOutOfRange {

        try {
            game.startGame();
            fail("Game was successfully started before setup.");
        } catch (GameWasNotInitialized e1) {
            throw e1;
        } catch (SizeOfMapWasNotSet e2) {
            fail("Expected GameWasNotInitialized, not SizeOfMapWasNotSet.");
        }
    }
}
