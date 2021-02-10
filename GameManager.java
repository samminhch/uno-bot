import com.marshie.uno.*;

import java.util.*;

public class GameManager {
    private Player[] players;
    private Card cardPlayed;
    private Deck deck;
    private int whosTurn;
    private boolean dirPos;

    /*
     * This class is used in order to run the Uno game.
     */
    public GameManager(int numPlayers) throws IllegalArgumentException {
        if (numPlayers < 2 || numPlayers >= 10)
            throw new IllegalArgumentException(String.format(
                    "The number of players needed for Uno is 2-10. The number of players you entered was %d.",
                    numPlayers));
        prepare(numPlayers);
    }

    /**
     * This will set up a new Uno game and play until the game is finished.
     */
    public void startGame() {
        for (int i = 0; i < 7; i++)
            for (Player player : players)
                playerDrawCard(player);
        
        cardPlayed = deck.draw();    
    
        while (!isFinished()) {
            if (deck.size() == 0)
                newDeck();
            turn();
        }
    }

    private void turn() {
        interpretCard();
        playCard(); //this method should be the method the AI uses to play a card.
        
        if (players[whosTurn].size() == 1)
            System.out.printf("Player%d: Uno!", whosTurn + 1);
        nextTurn();
    }

    private void interpretCard() {
        String cardValue = cardPlayed.getValue();
        if (cardValue.equals("rev")) {
            dirPos = !dirPos;
            nextTurn();
        }
        else if (cardValue.equals("skp"))
            whosTurn = wrap(nextTurn());
        else if (cardValue.matches("draw+\\d")) {
            int numDraws = Integer.parseInt(cardValue.substring(cardValue.indexOf("+") + 1));

            for(int i = 0; i < numDraws; i++)
                playerDrawCard(players[wrap(whosTurn)]);
        }
    }

    //I'm just going to add rules of what cards you aren't allowed to play.
    private void playCard() {
        ArrayList<Card> playableCards  = new ArrayList<Card>();

        for (Card card : players[whosTurn].getHand())
            if (card.getColor() == cardPlayed.getColor() || card.getValue().equals(cardPlayed.getValue()))
                playableCards.add(card);

        //if there's no playable cards in hand, player will draw from deck until there's a card for the player to play.
        while (playableCards.size() == 0){
            Card nextCard = deck.draw();
            players[whosTurn].addCard(nextCard);
            if (nextCard.getColor() == cardPlayed.getColor() || nextCard.getValue().equals(cardPlayed.getValue()))
                playableCards.add(nextCard);
        }
        //since the AI isn't implemented yet, I'm just gonna make the program choose a random card.
        //please reimplement this later i just don't know what to do
        cardPlayed = playableCards.get((int)(Math.random() * playableCards.size()));

    }

    /**
     * This method draws a card from the deck to the player's hand.
     * @param player the Player that's drawing from the deck.
     */
    private void playerDrawCard(Player player) {
        player.addCard(deck.draw());
    }

    private void prepare(int numPlayers) {
        players = new Player[numPlayers];
        Arrays.fill(players, new Player());

        deck = new Deck();
        deck.shuffle();

        whosTurn = 0;
        dirPos = true;
    }

    /**
     * This method creates a new Uno deck, excluding cards in both player's hands.
     */
    private void newDeck() {
        deck = new Deck();

        //removes cards from the deck that's already in player's hand & in play
        for (Player player : players)
            for (Card card : player.getHand())
                deck.removeCard(card);
        deck.removeCard(cardPlayed);

        deck.shuffle(); //shuffles deck again.
    }

    private boolean isFinished() {
        for(Player player : players)
            if (player.getHand().size() == 0)
                return true;
        return false;
    }

    private int wrap(int index) {
        if (index >= players.length)
            index %= players.length;
        else if (index < 0)
            index += players.length + index;
        return index;
    }

    private int nextTurn() {
        return whosTurn += dirPos ? 1 : -1;
    }
}