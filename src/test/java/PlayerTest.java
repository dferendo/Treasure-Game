import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Miguel Dingli
 */
public class PlayerTest {

    private Player player;
    private final int startX = 5, startY = 10, id = 1;

    @Before
    public void init() {
        player = new Player(id);
        Assume.assumeTrue(player.setPosition(new Position(startX, startY)));
    }

    @Test
    public void setPosition_nullArgument() {
        Assert.assertFalse(player.setPosition(null));
    }

    @Test
    public void getPosition_setterValueMatchesGetterValue() {
        Assert.assertTrue(player.getPosition().getX() == startX);
        Assert.assertTrue(player.getPosition().getY() == startY);
    }

    @Test
    public void move_moveUpCausesChangeInYButNoChangeInX() {
        player.move(Player.MOVE_DIRECTION.UP);
        Assert.assertTrue(player.getPosition().getX() == startX);
        Assert.assertTrue(player.getPosition().getY() == startY - 1);
    }

    @Test
    public void move_moveDownCausesChangeInYButNoChangeInX() {
        player.move(Player.MOVE_DIRECTION.DOWN);
        Assert.assertTrue(player.getPosition().getX() == startX);
        Assert.assertTrue(player.getPosition().getY() == startY + 1);
    }

    @Test
    public void move_moveLeftCausesChangeInXButNoChangeInY() {
        player.move(Player.MOVE_DIRECTION.LEFT);
        Assert.assertTrue(player.getPosition().getX() == startX - 1);
        Assert.assertTrue(player.getPosition().getY() == startY);
    }

    @Test
    public void move_moveRightCausesChangeInXButNoChangeInY() {
        player.move(Player.MOVE_DIRECTION.RIGHT);
        Assert.assertTrue(player.getPosition().getX() == startX + 1);
        Assert.assertTrue(player.getPosition().getY() == startY);
    }

    @Test
    public void getId_constructorValueMatchesGetterValue() {
        Assert.assertTrue(player.getID() == id);
    }

    @Test
    public void wasVisited_startPositionShouldBeVisited() {
        Assert.assertTrue(player.wasVisited(startX, startY));
    }

    @Test
    public void wasVisited_nonStartPositionShouldNotBeVisited() {
        Assert.assertFalse(player.wasVisited(startX + 1, startY + 1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void wasVisited_negativeCoordinates() {
        player.wasVisited(-1, -1);
    }

    @Test
    public void backToStartPosition_positionAfterCallEqualToStartPosition() {

        player.backToStartPosition();
        Assert.assertTrue(player.getPosition().equals(new Position(startX, startY)));
    }

    @Test
    public void backToStartPosition_positionAfterCallUnequalToNonStartPosition() {

        Assume.assumeTrue(player.setPosition(new Position(startX + 1, startY + 1)));
        player.backToStartPosition();
        Assert.assertTrue(player.getPosition().equals(new Position(startX, startY)));
    }
}
