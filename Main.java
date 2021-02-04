import uno.*;
import uno.cards.*;

import java.util.*;

public class Main
{
    static Player player1, player2;
    static Deck dick;
    
    //this method will be used to run the entire app ?

    public static void main(String[] args) {

        //setting up the game
        prepare();
        System.out.println(dick.toString());
        Arrays.stream(new String[] {"1", "2"}).mapToInt(Integer :: parseInt).toArray();
    }

    public static void prepare() {
        dick = new Deck();
        dick.shuffle();
        player1 = new Player();
        player2 = new Player();

        for(int i = 0; i < 7; i++) {
            player1.addCard(dick.draw());
            player2.addCard(dick.draw());
        }
    }
}
