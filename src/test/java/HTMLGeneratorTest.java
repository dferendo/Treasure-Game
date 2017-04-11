import exceptions.PositionIsOutOfRange;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Dylan Frendo.
 */
public class HTMLGeneratorTest {

    private HTMLGenerator htmlGeneratorInstance;
    private File file = new File("src/test/resources/html-test-template/SoftEngineer.html");
    private Player player;
    private Map map;
    private int size = 10;

    @Before
    public void setUp() throws Exception {
        int numberOfPlayers = 3;
        map = new Map();
        player = new Player(1);
        File HTMLTemplateLocation = new File("src/main/resources/html-template/SoftEngineer.html");

        map.setMapSize(size, numberOfPlayers);
        map.generate();
        FileUtils.copyFile(HTMLTemplateLocation, file);
    }

    @After
    public void tearDown() throws Exception {
        if (!file.delete()) {
            throw new Exception("Failed to delete test file");
        }
    }

    @Test(expected = IOException.class)
    public void HTMLGenerator_fileTemplateNotFound_correctPosition() throws Exception {
        player.setPosition(new Position(0, 0));
        htmlGeneratorInstance = new HTMLGenerator(new File("ThisFileDoesNotExist"), map, player);
    }

    @Test
    public void HTMLGenerator_fileTemplateFound_badPosition() throws IOException, PositionIsOutOfRange {
        player.setPosition(new Position(2, 2));
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
    }

    // TODO ask how to test randomness.
}