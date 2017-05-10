import exceptions.MapWasAlreadyInitialized;
import exceptions.PositionIsOutOfRange;
import exceptions.SizeOfMapWasNotSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.Assert.fail;

/**
 * Tests used to test the implementation of HTMLGenerator.
 *
 * @author Dylan Frendo.
 */
public class HTMLGeneratorTest {

    private HTMLGenerator htmlGeneratorInstance;
    private File file = new File("src/test/resources/html-test-template/SoftEngineer.html");
    private Player player;
    private Map map;
    private int size = 10, playerNumber = 3;

    @Before
    public void setUp() throws IOException, SizeOfMapWasNotSet, MapWasAlreadyInitialized {
        int numberOfPlayers = 3;
        // Can be anything or can use mocking
        map = new SafeMap();
        player = new Player(playerNumber);
        File HTMLTemplateLocation = new File("src/main/resources/html-template/SoftEngineer.html");

        map.setMapSize(size, size, numberOfPlayers);
        map.generate();
        FileUtils.copyFile(HTMLTemplateLocation, file);
    }

    @After
    public void tearDown() throws IOException, IllegalAccessException, NoSuchFieldException {
        if (!file.delete()) {
            throw new IOException("Failed to delete test file.");
        }
        // Clear Map instance
        Field instance = Map.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test(expected = IOException.class)
    public void HTMLGenerator_fileTemplateNotFound_correctPosition() throws IOException, PositionIsOutOfRange, SizeOfMapWasNotSet {
        map.setInitialPlayerPosition(player);
        htmlGeneratorInstance = new HTMLGenerator(new File("ThisFileDoesNotExist"), map, player);
    }

    @Test
    public void HTMLGenerator_fileTemplateFoundAndCreated() throws IOException, PositionIsOutOfRange, SizeOfMapWasNotSet {
        map.setInitialPlayerPosition(player);
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
        // Check if file exists.
        Assert.assertTrue(file.exists());
    }

    @Test
    public void HTMLGenerator_checkThatThereIsTheCaptionWithThePlayerName()
            throws IOException, PositionIsOutOfRange, SizeOfMapWasNotSet {
        map.setInitialPlayerPosition(player);
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
        // The file generated will be available after constructor
        String html = FileUtils.readFileToString(file);

        Assert.assertTrue(StringUtils.countMatches(html,
                "<caption class=\"playerNumber\">Player " + player.getID() + "</caption>\n")
                == 1);
    }

    @Test
    public void HTMLGenerator_checkIfPlayerIsInitiallyOnGrassTileAndOtherTilesAreUndiscovered()
            throws IOException, PositionIsOutOfRange, SizeOfMapWasNotSet {
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
    public void HTMLGenerator_checkPlayerMovementRemovesUnDiscoveredCell() throws IOException, PositionIsOutOfRange, SizeOfMapWasNotSet {
        int newX = 0, newY = 1;
        // Set 2 position of the player.
        map.setInitialPlayerPosition(player);
        // This is a condition so that the new position is not the same as the initial position
        if (player.getPosition().getX() == newX && player.getPosition().getY() == newY) {
            newX = 1;
        }
        player.setPosition(new Position(newX, newY));
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
        // The file generated will be available after constructor
        String html = FileUtils.readFileToString(file);

        // Two Unknown tiles are removed from being unknown regardless of tiles type.
        Assert.assertTrue(StringUtils.countMatches(html,
                htmlGeneratorInstance.IDLE_CELL) == (map.getMapSize() * map.getMapSize()) - 2);
    }

    @Test
    public void HTMLGenerator_checkPlayerMovementHas1GreenTile() throws IOException, PositionIsOutOfRange, SizeOfMapWasNotSet {
        int newX = 0, newY = 1;
        // Set 2 position of the player.
        map.setInitialPlayerPosition(player);
        // This is a condition so that the new position is not the same as the initial position
        if (player.getPosition().getX() == newX && player.getPosition().getY() == newY) {
            newX = 1;
        }
        player.setPosition(new Position(newX, newY));
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
        // The file generated will be available after constructor
        String html = FileUtils.readFileToString(file);

        // Regardless of the Tile Type, there is the initial tile which is Grass.
        Assert.assertTrue(StringUtils.countMatches(html,
                htmlGeneratorInstance.GRASS_CELL) == 1);
    }

    @Test
    public void HTMLGenerator_checkPlayerMovementMovesToNextTile_nextTileIsGreen()
            throws IOException, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Set 2 position of the player.
        map.setInitialPlayerPosition(player);
        // Find position of Type.
        findTilePosition(player, Map.TILE_TYPE.GRASS);
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
        // The file generated will be available after constructor
        String html = FileUtils.readFileToString(file);

        if (map.getTileType(player.getPosition().getX(), player.getPosition().getY()) == Map.TILE_TYPE.GRASS) {
            // Assert that there is 1 green tile with the player on it.
            Assert.assertTrue(StringUtils.countMatches(html,
                    htmlGeneratorInstance.GRASS_CELL_WITH_PLAYER) == 1);
        } else {
            fail("There are no Grass Tiles in the game, error.");
        }
    }

    @Test
    public void HTMLGenerator_checkPlayerMovementMovesToNextTile_nextTileIsTreasure()
            throws IOException, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Set 2 position of the player.
        map.setInitialPlayerPosition(player);
        // Find position of Type.
        findTilePosition(player, Map.TILE_TYPE.TREASURE);
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
        // The file generated will be available after constructor
        String html = FileUtils.readFileToString(file);

        if (map.getTileType(player.getPosition().getX(), player.getPosition().getY()) == Map.TILE_TYPE.TREASURE) {
            // Assert that there is 1 green tile with the player on it.
            Assert.assertTrue(StringUtils.countMatches(html,
                    htmlGeneratorInstance.TREASURE_CELL_WITH_PLAYER) == 1);
        } else {
            fail("There is no Treasure Tile in the game, there should be 1.");
        }
    }

    @Test
    public void HTMLGenerator_checkPlayerMovementMovesToNextTile_nextTileIsWaterTile()
            throws IOException, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Set 2 position of the player.
        map.setInitialPlayerPosition(player);
        // Find position of Type.
        findTilePosition(player, Map.TILE_TYPE.WATER);
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
        // The file generated will be available after constructor
        String html = FileUtils.readFileToString(file);

        if (map.getTileType(player.getPosition().getX(), player.getPosition().getY()) == Map.TILE_TYPE.WATER) {
            // Assert that there is 1 green tile with the player on it.
            Assert.assertTrue(StringUtils.countMatches(html,
                    htmlGeneratorInstance.WATER_CELL_WITH_PLAYER) == 1);
        } else {
            fail("There are no Water Tiles in the game, error there should be at least 10%.");
        }
    }

    @Test
    public void HTMLGenerator_checkPlayerMovementMovesToNextTile_thereIsAWaterTileAfterMovingAwayFromTheWaterTile()
            throws IOException, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Set 2 position of the player.
        map.setInitialPlayerPosition(player);
        // The player will go to the starting point to guarantee
        // that the player does not land on the same water tile again.
        int x = player.getPosition().getX(), y = player.getPosition().getY();
        // Find position of Type.
        findTilePosition(player, Map.TILE_TYPE.WATER);
        // Move to the starting location again
        player.setPosition(new Position(x, y));
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
        // The file generated will be available after constructor
        String html = FileUtils.readFileToString(file);

        Assert.assertTrue(StringUtils.countMatches(html, htmlGeneratorInstance.WATER_CELL) == 1);
    }

    @Test
    public void HTMLGenerator_checkPlayerMovementMovesToNextTile_thereIsAWaterTreasureTileAfterMovingAwayFromTheTreasureTile()
            throws IOException, PositionIsOutOfRange, SizeOfMapWasNotSet {
        // Set 2 position of the player.
        map.setInitialPlayerPosition(player);
        // The player will go to the starting point to guarantee
        // that the player does not land on the same water tile again.
        int x = player.getPosition().getX(), y = player.getPosition().getY();
        // Find position of Type.
        findTilePosition(player, Map.TILE_TYPE.TREASURE);
        // Move to the starting location again
        player.setPosition(new Position(x, y));
        htmlGeneratorInstance = new HTMLGenerator(file, map, player);
        // The file generated will be available after constructor
        String html = FileUtils.readFileToString(file);

        Assert.assertTrue(StringUtils.countMatches(html, htmlGeneratorInstance.TREASURE_CELL) == 1);
    }

    private void findTilePosition(Player player, Map.TILE_TYPE tileType) throws PositionIsOutOfRange {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (map.getTileType(x, y) == tileType) {
                    // This is to guarantee that the initial Grass tile will not be visited twice.
                    if (!player.wasVisited(x, y)) {
                        player.setPosition(new Position(x, y));
                        return;
                    }
                }
            }
        }
    }

}