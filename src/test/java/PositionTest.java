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
}
