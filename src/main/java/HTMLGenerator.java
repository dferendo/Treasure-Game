import exceptions.PositionIsOutOfRange;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Dylan Frendo.
 */
public class HTMLGenerator {

    private final String TAB_FOR_ROW = "\t\t\t";
    private final String TAB_FOR_CELL = TAB_FOR_ROW + "\t";
    private final String TAB_FOR_IMAGE = TAB_FOR_CELL + "\t";

    private final String GRASS_CELL_WITH_PLAYER =
            TAB_FOR_CELL +
                    "<td class=\"grassCell\">\n" +
                    TAB_FOR_IMAGE + "<img class=\"playerIcon\" src=\"../images/ic_person_pin_black_24px.svg\">\n" +
                    TAB_FOR_CELL + "</td>\n";
    private final String IDLE_CELL = TAB_FOR_CELL + "<td class=\"notDiscoveredCell\"></td>\n";
    private final String GRASS_CELL = TAB_FOR_CELL + "<td class=\"grassCell\"></td>\n";
    private final String WATER_CELL_WITH_PLAYER =
            TAB_FOR_CELL +
            "<td class=\"waterCell\">\n" +
                    TAB_FOR_IMAGE + "<img class=\"playerIcon\" src=\"../images/ic_person_pin_black_24px.svg\">\n" +
                    TAB_FOR_CELL + "</td>\n";
    private final String WATER_CELL = TAB_FOR_CELL + "<td class=\"waterCell\"></td>\n";
    private final String TREASURE_CELL_WITH_PLAYER =
            TAB_FOR_CELL +
            "<td class=\"treasureCell\">\n" +
                    TAB_FOR_IMAGE + "<img class=\"playerIcon\" src=\"../images/ic_person_pin_black_24px.svg\">\n" +
                    TAB_FOR_CELL + "</td>\n";
    private final String TREASURE_CELL = TAB_FOR_CELL + "<td class=\"treasureCell\"></td>\n";

    HTMLGenerator(final File fileLocation, final Map map, final Player player) throws IOException, PositionIsOutOfRange {
        writeOnFile(fileLocation, createTable(map, player));
    }

    private void writeOnFile(final File fileLocation, final String table) throws IOException {
        String htmlTemplate = FileUtils.readFileToString(fileLocation);
        htmlTemplate = htmlTemplate.replace("$Table", table);
        FileUtils.writeStringToFile(fileLocation, htmlTemplate);
    }

    private String createTable(final Map map, final Player player) throws PositionIsOutOfRange {
        int mapSize = map.getMapSize(), x, y;
        StringBuilder table = new StringBuilder();
        table.append(createCaption(player));

        for (int i = 0; i < mapSize; i++) {
            table.append(TAB_FOR_ROW);
            table.append("<tr>\n");
            for (int j = 0; j < mapSize; j++) {
                x = player.getPosition().getX();
                y = player.getPosition().getY();
                if (x == j && y == i) {
                    table.append(determineCellType(map.getTileType(j, i), true));
                } else if (player.wasVisited(j, i)) {
                    table.append(determineCellType(map.getTileType(j, i), false));
                } else {
                    table.append(IDLE_CELL);
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
