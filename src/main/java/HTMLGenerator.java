import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Dylan Frendo.
 */
public class HTMLGenerator {

    public HTMLGenerator(File fileLocation, Map map, Player player) throws IOException, Exception {
        writeOnFile(fileLocation, createTable(map, player));
    }

    private void writeOnFile(File fileLocation, String table) throws IOException {
        String htmlTemplate = FileUtils.readFileToString(fileLocation);
        htmlTemplate = htmlTemplate.replace("$table", table);
        FileUtils.writeStringToFile(fileLocation, htmlTemplate);
    }

    private String createTable(Map map, Player player) throws Exception {
        return "";
    }
}
