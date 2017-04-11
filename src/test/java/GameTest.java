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

    @Test
    public void startGame_startBeforeSetupCausesException() {

        try {
            game.startGame();
        } catch (Exception e) {
            fail("Game was started before setup.");
        }
    }
}
