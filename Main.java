import com.marshie.uno.GameManager;

/**
 * This class is for implementing the AI??
 */
public class Main {
    public static void main(String[] args) {
        //creates a new Uno game for 2 players.
        GameManager game = new GameManager(2);
        game.playGame();
    }
}