import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Miguel Dingli
 */
public class PositionTest {

    private Position position;
    private final int x = 5, y = 10;

    @Before
    public void init() {
        position = new Position(x, y);
    }

    @Test
    public void getX_constructorValueMatchesGetterValue() {
        Assert.assertTrue(x == position.getX());
    }

    @Test
    public void getY_constructorValueMatchesGetterValue() {
        Assert.assertTrue(y == position.getY());
    }

    @Test
    public void equals_equalPositions() {
        final Position newPos = new Position(x, y);
        Assert.assertTrue(position.equals(newPos));
    }

    @Test
    public void equals_unequalPositions() {
        final Position newPos = new Position(x + 1, y + 1);
        Assert.assertFalse(position.equals(newPos));
    }

    @Test
    public void equals_nullPosition() {
        Assert.assertFalse(position.equals(null));
    }

    @Test
    public void equals_nonPositionObject() {
        Assert.assertFalse(position.equals(new Object()));
    }
}
