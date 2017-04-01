import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dylan on 25/03/2017.
 */
public class InitialJenkinsTestTest {

    private InitialJenkinsTest initialJenkinsTest;

    @Before
    public void setUp() {
        initialJenkinsTest = new InitialJenkinsTest();
    }

    @Test
    public void maximum_firstArgumentIsMax() {
        final int x = 20, y = 10;
        Assert.assertTrue(initialJenkinsTest.maximum(x, y) == x);
    }

    @Test
    public void minimum_firstArgumentIsMin() {
        final int x = 10, y = 20;
        Assert.assertTrue(initialJenkinsTest.minimum(x, y) == x);
    }
}