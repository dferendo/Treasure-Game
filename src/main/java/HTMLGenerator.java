import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Dylan Frendo.
 */
public class HTMLGenerator {

    final String IDLE_CELL = "<td class=\"notDiscoveredCell\"></td>\n";
    final String GRASS_CELL_WITH_PLAYER =
            "<td class=\"grassCell\">\n" +
                    "\t<img class=\"playerIcon\" src=\"../images/ic_person_pin_black_24px.svg\">\n" +
                    "</td>\n";
    final String GRASS_CELL = "<td class=\"grassCell\"></td>\n";
    final String WATER_CELL_WITH_PLAYER =
            "<td class=\"waterCell\">\n" +
                    "\t<img class=\"playerIcon\" src=\"../images/ic_person_pin_black_24px.svg\">\n" +
                    "</td>\n";
    final String WATER_CELL = "<td class=\"waterCell\"></td>\n";
    final String TREASURE_CELL_WITH_PLAYER =
            "<td class=\"treasureCell\">\n" +
                    "\t<img class=\"playerIcon\" src=\"../images/ic_person_pin_black_24px.svg\">\n" +
                    "</td>\n";
    final String TREASURE_CELL = "<td class=\"treasureCell\"></td>\n";

    public HTMLGenerator(File fileLocation, Map map, Player player) throws IOException, Exception {
        writeOnFile(fileLocation, createTable(map, player));
    }

    private void writeOnFile(File fileLocation, String table) throws IOException {
        String htmlTemplate = FileUtils.readFileToString(fileLocation);
        htmlTemplate = htmlTemplate.replace("$Table", table);
        FileUtils.writeStringToFile(fileLocation, htmlTemplate);
    }

    private String createTable(Map map, Player player) throws Exception {
        int mapSize = map.size, x, y;
        StringBuilder table = new StringBuilder();
        table.append(createCaption(player));

        for (int i = 0; i < mapSize; i++) {
            table.append("<tr>\n");
            for (int j = 0; j < mapSize; j++) {
                x = player.getPosition().getX();
                y = player.getPosition().getY();
                if (x == i && y == j) {
                    table.append(determineCellType(map.getTileType(x, y), true));
                } else if (player.wasVisited(x, y)) {
                    table.append(determineCellType(map.getTileType(x, y), false));
                } else {
                    table.append(createIdleCell());
                }
            }
            table.append("</tr>\n");
        }
        return table.toString();
    }

    private String createCaption(Player player) {
        return "<caption class=\"playerNumber\">Player " + player.getID() + "</caption>\n";
    }

    private String determineCellType(Map.TILE_TYPE cellType, boolean playerIsOnTile) {
        switch (cellType) {
            case GRASS:
                return createGrassCell(playerIsOnTile);
            case WATER:
                return createWaterCell(playerIsOnTile);
            case TREASURE:
                return createTreasureCell(playerIsOnTile);
            default:
                return "";
        }
    }

    private String createIdleCell() {
        return IDLE_CELL;
    }

    private String createGrassCell(boolean playerIsOnTile) {
        return playerIsOnTile ? GRASS_CELL_WITH_PLAYER : GRASS_CELL;
    }

    private String createWaterCell(boolean playerIsOnTile) {
        return playerIsOnTile ? WATER_CELL_WITH_PLAYER : WATER_CELL;
    }

    private String createTreasureCell(boolean playerIsOnTile) {
        return playerIsOnTile ? TREASURE_CELL_WITH_PLAYER : TREASURE_CELL;
    }
}
