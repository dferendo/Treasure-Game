import exceptions.PositionIsOutOfRange;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Generate HTML code for the specific map and outputs it in resources/players-maps.
 *
 * @author Dylan Frendo.
 */
public class HTMLGenerator {

    /**
     * These strings are used for indentation.
     */
    private final String TAB_FOR_ROW = "\t\t\t";
    private final String TAB_FOR_CELL = TAB_FOR_ROW + "\t";
    private final String TAB_FOR_IMAGE = TAB_FOR_CELL + "\t";

    /**
     * These Strings are used to generated the type of the cell and the image
     * for the player.
     */
    final String GRASS_CELL_WITH_PLAYER =
            TAB_FOR_CELL +
                    "<td class=\"grassCell\">\n" +
                    TAB_FOR_IMAGE + "<img class=\"playerIcon\" src=\"../images/ic_person_pin_black_24px.svg\">\n" +
                    TAB_FOR_CELL + "</td>\n";
    final String IDLE_CELL = TAB_FOR_CELL + "<td class=\"notDiscoveredCell\"></td>\n";
    final String GRASS_CELL = TAB_FOR_CELL + "<td class=\"grassCell\"></td>\n";
    final String WATER_CELL_WITH_PLAYER =
            TAB_FOR_CELL +
                    "<td class=\"waterCell\">\n" +
                    TAB_FOR_IMAGE + "<img class=\"playerIcon\" src=\"../images/ic_person_pin_black_24px.svg\">\n" +
                    TAB_FOR_CELL + "</td>\n";
    final String WATER_CELL = TAB_FOR_CELL + "<td class=\"waterCell\"></td>\n";
    final String TREASURE_CELL_WITH_PLAYER =
            TAB_FOR_CELL +
                    "<td class=\"treasureCell\">\n" +
                    TAB_FOR_IMAGE + "<img class=\"playerIcon\" src=\"../images/ic_person_pin_black_24px.svg\">\n" +
                    TAB_FOR_CELL + "</td>\n";
    final String TREASURE_CELL = TAB_FOR_CELL + "<td class=\"treasureCell\"></td>\n";

    /**
     * Constructor calls the required methods to write on files. Inputted file location requires
     * $Table tag to have correct output.
     *
     * @param outputLocation: File location where the contents of the file will be read and updated.
     * @param map: The map that will be printed.
     * @param player: The player that needs it's map to be printed.
     * @throws IOException: File location specific is not correct.
     */
    HTMLGenerator(final File outputLocation, final Map map, final Player player) throws IOException {
        writeOnFile(outputLocation, createTable(map, player));
    }

    /**
     * Reads to contents of the specified file and replace $Table found in the file with
     * the html contents of the table. $Table is required for proper output.
     *
     * @param fileLocation: The file location where the contents will be read and updated.
     * @param table: A String containing the contents of the table.
     * @throws IOException: File specified was not found.
     */
    private void writeOnFile(final File fileLocation, final String table) throws IOException {
        String htmlTemplate = FileUtils.readFileToString(fileLocation);
        htmlTemplate = htmlTemplate.replace("$Table", table);
        FileUtils.writeStringToFile(fileLocation, htmlTemplate);
    }

    /**
     * Create a String containing the rows and their cells in HTML format for the entire map. The css class
     * for each cell depends on the Tile, got found x, y coordinates of the map. An Image tag is inserted
     * in the ceil tag if the player is currently on the Tile.
     *
     * @param map: The map given converted to HTML format.
     * @param player: The player that the file is generated for.
     * @return String containing the rows and their cells in HTML format.
     */
    private String createTable(final Map map, final Player player) {
        final int mapSize = map.getMapSize();
        int x, y;
        final StringBuilder table = new StringBuilder();
        table.append(createCaption(player));

        for (int i = 0; i < mapSize; i++) {
            table.append(TAB_FOR_ROW);
            table.append("<tr>\n");
            for (int j = 0; j < mapSize; j++) {
                x = player.getPosition().getX();
                y = player.getPosition().getY();

                try {
                    if (x == j && y == i) {
                        table.append(determineCellType(map.getTileType(j, i), true));
                    } else if (player.wasVisited(j, i)) {
                        table.append(determineCellType(map.getTileType(j, i), false));
                    } else {
                        table.append(IDLE_CELL);
                    }
                } catch (PositionIsOutOfRange positionIsOutOfRange) {
                    // Create Table loop is incorrect.
                    positionIsOutOfRange.getMessage();
                }
            }
            table.append(TAB_FOR_ROW);
            table.append("</tr>\n");
        }
        return table.toString();
    }

    /**
     * Prints the caption (Header) of the table. It consists of "Player n" where n is the player ID.
     *
     * @param player: The player that the file is generated for.
     * @return String containing the caption.
     */
    private String createCaption(final Player player) {
        final String team = player.getTeam() == null ? "" : " (Team " + player.getTeam().getID() + ")";
        return "<caption class=\"playerNumber\">Player " + player.getID() + team + "</caption>\n";
    }

    /**
     * Determine the String required for the selected Tile. It also checks whether the player is on the
     * cell, which returns a different String containing an Image tag.
     *
     * @param cellType: Type of the cell.
     * @param playerIsOnTile: Player's current location is on the cell.
     * @return Returns a string in HTML format of the cell.
     */
    private String determineCellType(final Map.TILE_TYPE cellType, final boolean playerIsOnTile) {
        switch (cellType) {
            case GRASS:
                return playerIsOnTile ? GRASS_CELL_WITH_PLAYER : GRASS_CELL;
            case WATER:
                return playerIsOnTile ? WATER_CELL_WITH_PLAYER : WATER_CELL;
            case TREASURE:
                return playerIsOnTile ? TREASURE_CELL_WITH_PLAYER : TREASURE_CELL;
            default:
                return "";
        }
    }
}
