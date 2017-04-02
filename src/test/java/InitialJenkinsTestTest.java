import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dylan on 25/03/2017.
 * Code will be pushed 3 times to check if Jenkins pulls, compiles and tests the code
 * automatically.
 * Test 1 - 1 unit Test, should trigger build #15 in Jenkins.
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

//    @Test
//    public void minimum_firstArgumentIsMin() {
//        final int x = 10, y = 20;
//        Assert.assertTrue(initialJenkinsTest.minimum(x, y) == x);
//    }
//
//    @Test
//    public void mean_twiceMeanIsGreaterOrEqualToArguments() {
//        final int x = 10, y = 20;
//        final float twiceMean = 2 * initialJenkinsTest.mean(x, y);
//        Assert.assertTrue(twiceMean >= x);
//        Assert.assertTrue(twiceMean >= y);
//    }
}