import com.marshie.uno.GameManager;

import java.io.IOException;

/**
 * This class is for implementing the AI??
 */
public class Main {
    public static void main(String[] args) {
        //creates a new Uno game for 2 players.
        GameManager game = new GameManager(2);
        game.setPlayer(0, "rand");
        game.setPlayer(1, "rand");
        System.out.println(game.playGame());
    }
}