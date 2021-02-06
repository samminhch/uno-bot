import uno.*;
import uno.cards.*;

import java.util.*;

/**
 * This class is used in order to run the Uno game.
 * #TODO: integrate machine learning into the main method.
 */
public class Main
{
    static Deck deck;
    static int whosTurn = 0;
    static Player player1, player2;
    static Card playedCard;
    
    //this method will be used to run the entire app ?

    public static void main(String[] args) {

        //setting up the game
        prepare();

        while (player1.numCardsInHand() != 0 && player2.numCardsInHand() != 0) {
            if (deck.size() == 0)
                newDeck();
            turn(whosTurn);
            whosTurn = ++whosTurn % 2;
        }
        
        System.out.println(deck.toString());
        Arrays.stream(new String[] {"1", "2"}).mapToInt(Integer :: parseInt).toArray();
    }

    /**
     * This method sets up the Uno game.
     */
    public static void prepare() {
        deck = new Deck();
        deck.shuffle();
        player1 = new Player();
        player2 = new Player();

        for(int i = 0; i < 7; i++) { //in the Uno rules, player start off with 7 cards in each hand (?)
            playerDrawCard(player1);
            playerDrawCard(player2);
        }
    }

    /**
     * This method creates a new Uno deck, excluding cards in both player's hands.
     */
    public static void newDeck() {
        deck = new Deck();

        //removes cards from the deck that's already in player's hand & in play
        for(Card card : player1.getHand())
            deck.removeCard(card);
        for(Card card : player2.getHand())
            deck.removeCard(card);
        deck.removeCard(playedCard);

        deck.shuffle(); //shuffles deck again.
    }

    /**
     * This method draws a card from the deck to the player's hand.
     * @param player the Player that's drawing from the deck.
     */
    public static void playerDrawCard(Player player) {
        player.addCard(deck.draw());
    }

    /**
     * This method plays out a turn in the Uno game.
     * @param turn the player that gets to play.
     */
    public static void turn(int turn) {
        //in the case that a wild card is played, the program should ask the player for the color they want chosen.
        switch (turn) {
            case 0:
                //player1 does their thing
                
                break;
        
            case 1:
                //player2 dose their thing

                break;
        }
    }

    /**
     * This method determines what to do with the <code>playedCard</code> card.
     */
    public static void interpretPlayedCard() {
        String cardValue = playedCard.getValue();
        if (cardValue.equals("rev")) {
            /*
                 * WARNING: this one line for the reverse card *WILL NOT*
                 * work if the number of players is greater than two.
                 * This will have to be reimplemented if we're to make the neural
                 * network play with more than two players.
                 */
                ++whosTurn;
        }
        else if (cardValue.equals("skp"))
            ++whosTurn;
        else if (cardValue.matches("draw+\\d")) {
            int numDraws = Integer.parseInt(cardValue.substring(cardValue.indexOf("+") + 1));

            for(int i = 0; i < numDraws; i++)
                playerDrawCard(whosTurn == 0 ? player1 : player2);
        }
    }
}