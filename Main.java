import java.io.IOException;

/**
 * This class is for implementing the AI??
 */
public class Main {
    public static void main(String[] args) throws IOException {
        //creates a new Uno game for 2 players.
        GameManager game = new GameManager(2);
        game.startGame();
    }
}