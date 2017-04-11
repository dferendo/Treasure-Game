import java.util.Scanner;

/**
 * @author Miguel Dingli
 */
public class Main {

    public static void main(String args[]) throws Exception {

        Game.getGameInstance().setup();
        Game.getGameInstance().startGame();
    }
}
