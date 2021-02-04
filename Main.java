import uno.*;
import uno.cards.*;

import java.util.*;

public class Main
{
    static Deck deck;
    static int whosTurn = 0;
    static Player player1, player2;
    static Stack<Card> playingCards;
    
    //this method will be used to run the entire app ?

    public static void main(String[] args) {

        //setting up the game
        prepare();

        while (player1.numCardsInHand() != 0 && player2.numCardsInHand() != 0) {
            turn(whosTurn);
            whosTurn = ++whosTurn % 2;
        }
        
        System.out.println(deck.toString());
        Arrays.stream(new String[] {"1", "2"}).mapToInt(Integer :: parseInt).toArray();
    }

    public static void prepare() {
        playingCards = new Stack<Card>();
        deck = new Deck();
        deck.shuffle();
        player1 = new Player();
        player2 = new Player();

        for(int i = 0; i < 7; i++) {
            player1.addCard(deck.draw());
            player2.addCard(deck.draw());
        }
    }

    public static void turn(int turn) {
        switch (turn) {
            case 0:
                //player1 does their thing
                break;
        
            case 1:
                //player2 dose their thing
                break;
        }
    }
}
