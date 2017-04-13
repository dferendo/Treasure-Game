import exceptions.PositionIsOutOfRange;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
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
    private int size = 10, playerNumber = 3;

    @Before
    public void setUp() throws Exception {
        int numberOfPlayers = 3;
        map = new Map();
        player = new Player(playerNumber);
        File HTMLTemplateLocation = new File("src/main/resources/html-template/SoftEngineer.html");

        map.setMapSize(size, size, numberOfPlayers);
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
        map.setInitialPlayerPosition(player);
        htmlGeneratorInstance = new HTMLGenerator(new File("ThisFileDoesNotExist"), map, player);
    }

    @Test
    public void HTMLGenerator_fileTemplateFound_badPosition() throws IOException, PositionIsOutOfRange {
        map.setInitialPlayerPosition(player);
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
    }

    @Test
    public void HTMLGenerator_checkIfPlayerIsInitiallyOnGrassTileAndOtherTilesAreUndiscovered()
            throws IOException, PositionIsOutOfRange {
        map.setInitialPlayerPosition(player);
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
        // The file generated will be available after constructor
        String html = FileUtils.readFileToString(file);

        // Since the player only has the initial starting Position, the player cannot be on the treasure tile.
        // (If the player can be on the treasure tile it will have different string).
        Assert.assertTrue(StringUtils.countMatches(html,
                htmlGeneratorInstance.GRASS_CELL_WITH_PLAYER) == 1);
        Assert.assertTrue(StringUtils.countMatches(html,
                htmlGeneratorInstance.IDLE_CELL) == (map.getMapSize() * map.getMapSize()) - 1);
    }

    @Test
    public void HTMLGenerator_checkThatThereIsTheCaptionWithThePlayerName()
            throws IOException, PositionIsOutOfRange {
        map.setInitialPlayerPosition(player);
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
        // The file generated will be available after constructor
        String html = FileUtils.readFileToString(file);

        Assert.assertTrue(StringUtils.countMatches(html,
                "<caption class=\"playerNumber\">Player " + player.getID() + "</caption>\n")
                == 1);
    }

}