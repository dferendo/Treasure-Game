import exceptions.GameWasNotInitialized;
import exceptions.PositionIsOutOfRange;
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
        game = Game.getGameInstance();
    }

    @Test
    public void getGameInstance_notNull() {

        Assert.assertTrue(game != null);
        Assert.assertTrue(Game.getGameInstance() != null);
    }

    @Test(expected = GameWasNotInitialized.class)
    public void startGame_startBeforeSetupCausesException() throws GameWasNotInitialized, PositionIsOutOfRange {

        try {
            game.startGame();
            fail("Game was successfully started before setup.");
        } catch (GameWasNotInitialized e) {
            throw e;
        }
    }
}
