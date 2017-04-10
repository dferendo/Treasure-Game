import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Miguel Dingli
 */
public class PlayerTest {

    private Player player;
    private final int x = 5, y = 10, id = 1;

    @Before
    public void init() {
        player = new Player(id);
        Assume.assumeTrue(player.setPosition(new Position(x, y)));
    }

    @Test
    public void setPosition_nullArgument() {
        Assert.assertFalse(player.setPosition(null));
    }

    @Test
    public void getPosition_setterValueMatchesGetterValue() {
        Assert.assertTrue(player.getPosition().getX() == x);
        Assert.assertTrue(player.getPosition().getY() == y);
    }

    @Test
    public void move_moveUpCausesChangeInYButNoChangeInX() {
        player.move(Player.MOVE_DIRECTION.UP);
        Assert.assertTrue(player.getPosition().getX() == x);
        Assert.assertTrue(player.getPosition().getY() == y - 1);
    }

    @Test
    public void move_moveDownCausesChangeInYButNoChangeInX() {
        player.move(Player.MOVE_DIRECTION.DOWN);
        Assert.assertTrue(player.getPosition().getX() == x);
        Assert.assertTrue(player.getPosition().getY() == y + 1);
    }

    @Test
    public void move_moveLeftCausesChangeInXButNoChangeInY() {
        player.move(Player.MOVE_DIRECTION.LEFT);
        Assert.assertTrue(player.getPosition().getX() == x - 1);
        Assert.assertTrue(player.getPosition().getY() == y);
    }

    @Test
    public void move_moveRightCausesChangeInXButNoChangeInY() {
        player.move(Player.MOVE_DIRECTION.RIGHT);
        Assert.assertTrue(player.getPosition().getX() == x + 1);
        Assert.assertTrue(player.getPosition().getY() == y);
    }

    @Test
    public void getId_constructorValueMatchesGetterValue() {
        Assert.assertTrue(player.getID() == id);
    }
}
