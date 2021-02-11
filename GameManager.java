import com.marshie.uno.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private void prepare(int numPlayers) {
        players = new Player[numPlayers];
        for (int i = 0; i < players.length; i++)
            players[i] = new Player();

        deck = new Deck();
        deck.shuffle();

        whosTurn = 0;
        dirPos = true;
    }

    /**
     * This will set up a new Uno game and play until the game is finished.
     */
    public void startGame() throws IOException {
        for (int i = 0; i < 7; i++)
            for (Player player : players)
                player.addCard(deck.draw());
        cardPlayed = wildCardCase(deck.draw()); //in the case that a wild card is played first, the first player must choose a color.
    
        while (!isFinished()) {
            if (deck.size() == 0)
                newDeck();
            turn();
        }
    }

    private boolean isFinished() {
        for(Player player : players)
            if (player.getHand().size() == 0)
                return true;
        return false;
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



    /**
     * This method plays a turn out in the Uno game.
     */
    public void turn() throws IOException {
        interpretCard();
        playCard(); //this method should be the method the AI uses to play a card.

        if (players[whosTurn].size() == 1)
            System.out.printf("Player%d: Uno!\n", whosTurn + 1);
        whosTurn = nextTurn();
    }

    private void interpretCard() {
        String cardValue = cardPlayed.getValue();
        //#TODO: fix bug where if you play a reverse card and draw, it'll still be your turn!
        if (cardValue.equals("rev")) {
            dirPos = !dirPos;
            whosTurn = nextTurn();
        }
        else if (cardValue.equals("skp"))
            whosTurn = nextTurn();
        else if (cardValue.matches("drw\\+\\d")) {
            int numDraws = Integer.parseInt(cardValue.substring(cardValue.indexOf("+") + 1));

            for(int i = 0; i < numDraws; i++)
                players[whosTurn].addCard(deck.draw());
        }
    }

    private void playCard() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Card> playableCards = new ArrayList<Card>();
        ArrayList<Card> curPlayerHand = players[whosTurn].getHand();


        for (Card card : curPlayerHand)
            if (card.getColor() == cardPlayed.getColor() || card.getColor() == 'w' || card.getValue().equals(cardPlayed.getValue()))
                playableCards.add(card);

        //will automatically draw cards if you don't have any playable cards, and if you do, it'll print out the cards
        //that are playable and will ask you if you want to play a card or draw a card.
        //#TODO: make this if statement not break the game.
        if (playableCards.size() == 0){
            String response = "N";
            while(!response.equals("Y")) {
                //if there's no playable cards in hand, player will draw from deck until there's a card for the player to play.
                Card nextCard = deck.draw();
                while (playableCards.size() == 0){
                    players[whosTurn].addCard(nextCard);
                    if (nextCard.getColor() == cardPlayed.getColor() || nextCard.getValue().equals(cardPlayed.getValue()))
                        playableCards.add(nextCard);
                    else
                        nextCard = deck.draw();
                }
                System.out.printf("Play card %s? Y/N?\n", nextCard.toString());
                response = in.readLine().toUpperCase();
                if (response.equals("Y")) {
                    players[whosTurn].removeCard(nextCard);
                    cardPlayed = wildCardCase(nextCard); //wildCardCase just converts the wild card into a colored card
                }
            }
        }
        else {
            //prints out player's hand and card at play
            System.out.println("PLAYER " + (whosTurn + 1) + ":");
            System.out.printf("Card at play: %s\n", cardPlayed);
            System.out.print("PLAYABLE HAND: ");
            Collections.sort(curPlayerHand);
            Collections.sort(playableCards);
            for (int i = 0; i < playableCards.size() - 1; i++)
                System.out.printf("(%d) %s, ", i + 1, playableCards.get(i).toString());
            System.out.printf("(%d) %s\n", playableCards.size(), playableCards.get(playableCards.size() - 1).toString());
            System.out.printf("FULL HAND: (%d cards) %s\n", curPlayerHand.size(), curPlayerHand.toString());

            boolean validResponse = false;
            while (!validResponse) {
                System.out.println("Pick a card to play, or enter DRAW to draw:");
                String response = in.readLine().toUpperCase();
                if (response.equals("DRAW")) { //draws a card from deck and ends turn
                    players[whosTurn].addCard(deck.draw());
                    validResponse = true;
                } else if (response.matches("\\d+")) {
                    int index = Integer.parseInt(response) - 1;
                    if (index >= 0 && index < playableCards.size()) {
                        players[whosTurn].removeCard(playableCards.get(index));
                        cardPlayed = wildCardCase(playableCards.get(index));
                        validResponse = true;
                    }
                }
            }
        }
    }

    private Card wildCardCase(Card wild){
        if (wild.getColor() != 'w')
            return wild;
        Scanner in = new Scanner(System.in);
        System.out.println("Choose a color for the wild card: 'r' for Red, 'g' for Green, 'b' for Blue, 'y' for Yellow");
        char color = in.nextLine().charAt(0);
        in.close();
        return new Card(color, wild.getValue());
    }

    private int nextTurn() {
        return (whosTurn + (dirPos ? 1 : -1) + players.length * 69) % players.length;
    }

    /*                                  MAIN METHOD FOR TESTING                                   */

    public static void main(String[] args) throws IOException {
        GameManager game = new GameManager(2);
        game.startGame();
    }
}