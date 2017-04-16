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
     *
     * @param fileLocation
     * @param map
     * @param player
     * @throws IOException: Inputted file Location was not found.
     * @throws PositionIsOutOfRange: Position checked by the
     */
    HTMLGenerator(final File fileLocation, final Map map, final Player player) throws IOException {
        writeOnFile(fileLocation, createTable(map, player));
    }

    private void writeOnFile(final File fileLocation, final String table) throws IOException {
        String htmlTemplate = FileUtils.readFileToString(fileLocation);
        htmlTemplate = htmlTemplate.replace("$Table", table);
        FileUtils.writeStringToFile(fileLocation, htmlTemplate);
    }

    private String createTable(final Map map, final Player player) {
        final int mapSize = map.getMapSize();
        final StringBuilder table = new StringBuilder();
        table.append(createCaption(player));

        for (int i = 0; i < mapSize; i++) {
            table.append(TAB_FOR_ROW);
            table.append("<tr>\n");
            for (int j = 0; j < mapSize; j++) {
                final int x = player.getPosition().getX();
                final int y = player.getPosition().getY();

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

    private String createCaption(final Player player) {
        return "<caption class=\"playerNumber\">Player " + player.getID() + "</caption>\n";
    }

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
