import com.marshie.uno.*;
import com.marshie.util.Chain;

import java.util.*;

public class GameManager {
    private Player[] players;
    private Chain<Card, Boolean> playedCard; //formatted as [Card, boolean], where Card is the played card and the boolean is wether the card has been played yet
    private Deck deck;
    private int whoseTurn;
    private boolean dirPos;

    /*
     * This class is used in order to run the Uno game.
     */
    public GameManager(int numPlayers) {
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

        whoseTurn = 0;
        dirPos = true;
    }

    /**
     * This will set up a new Uno game and play until the game is finished.
     */
    public void startGame() {
        for (int i = 0; i < 7; i++)
            for (Player player : players)
                player.addCard(deck.draw());
        playedCard = new Chain<Card, Boolean>(wildCardCase(deck.draw()), false);

        while (!isFinished()) {
            newDeck();
            turn();
        }
    }

    private boolean isFinished() {
        for(int i = 0; i < players.length; i++)
            if (players[i].getHand().size() == 0) {
                System.out.printf("Player %d won!\n", i + 1);
                return true;
            }
        return false;
    }

    /**
     * This method plays a turn out in the Uno game.
     */
    public void turn() {
        System.out.printf("Player %d:\n", whoseTurn + 1);
        interpretCard();
        playCard(); //this method should be the method the AI uses to play a card.

        if (players[whoseTurn].size() == 1)
            System.out.println("Uno!");
        whoseTurn = nextTurn();
    }

    private void interpretCard() {
        if (!playedCard.getVal()){
            String cardValue = playedCard.getKey().getValue();
            if (cardValue.equals("rev")) {
                dirPos = !dirPos;
                whoseTurn = nextTurn();
            }
            else if (cardValue.equals("skp"))
                whoseTurn = nextTurn();
            else if (cardValue.matches("drw\\+\\d")) {
                int numDraws = Integer.parseInt(cardValue.substring(cardValue.indexOf("+") + 1));

                for(int i = 0; i < numDraws; i++) {
                    newDeck();
                    Card cardDrawn = deck.draw();
                    System.out.println("Drew " + cardDrawn.toString());
                    players[whoseTurn].addCard(cardDrawn);
                }
            }
        }
    }

    private void playCard() {
        Scanner in = new Scanner(System.in);
        ArrayList<Card> playableCards = new ArrayList<>();
        ArrayList<Card> curPlayerHand = players[whoseTurn].getHand();


        for (Card card : curPlayerHand)
            if (isValidCard(card))
                playableCards.add(card);

        //prints out player's hand and card at play
        System.out.printf("Card at play: %s\n", playedCard.getKey());
        System.out.print("PLAYABLE HAND: ");
        Collections.sort(curPlayerHand);
        Collections.sort(playableCards);

        for (int i = 0; i < playableCards.size() - 1; i++)
            System.out.printf("(%d) %s, ", i + 1, playableCards.get(i).toString());
        if (playableCards.size() == 0)
            System.out.println("null");
        else
            System.out.printf("(%d) %s\n", playableCards.size(), playableCards.get(playableCards.size() - 1).toString());
        System.out.printf("FULL HAND: (%d cards) %s\n", curPlayerHand.size(), curPlayerHand.toString());

        //will automatically draw cards if you don't have any playable cards, and if you do, it'll print out the cards
        //that are playable and will ask you if you want to play a card or draw a card.
        if (playableCards.size() == 0){
            String response = "N";
            while(response.equals("N")) {
                //if there's no playable cards in hand, player will draw from deck until there's a card for the player to play.
                Card nextCard = draw();
                while (!isValidCard(nextCard)){
                    System.out.printf("Drew card %s\n", nextCard.toString());
                    players[whoseTurn].addCard(nextCard);
                    if (isValidCard(nextCard))
                        playableCards.add(nextCard);
                    else
                        nextCard = draw();
                }

                //This while-loop is introduced so that the only valid answers are "Y" and "N".
                String ans = "";
                while(!ans.matches("[YN]")){
                    System.out.printf("Play card %s? Y/N?\n", nextCard.toString());
                    ans = in.nextLine().toUpperCase();
                }
                response = ans;
                if (response.equals("Y")) {
                    players[whoseTurn].removeCard(nextCard);
                    playedCard.setKey(wildCardCase(nextCard)); //wildCardCase just converts the wild card into a colored card
                    playedCard.setVal(false);
                }
            }
        }
        else {
            boolean validResponse = false;
            while (!validResponse) {
                System.out.println("Pick a card to play, or enter DRAW to draw:");
                String response = in.nextLine().toUpperCase();
                if (response.equals("DRAW")) { //draws a card from deck and ends turn
                    players[whoseTurn].addCard(deck.draw());
                    validResponse = true;
                    playedCard.setVal(true);
                } else if (response.matches("\\d+")) {
                    int index = Integer.parseInt(response) - 1;
                    if (index >= 0 && index < playableCards.size()) {
                        players[whoseTurn].removeCard(playableCards.get(index));
                        playedCard.setKey(wildCardCase(playableCards.get(index)));
                        playedCard.setVal(false);
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
        char color = '\0'; //that character just stands for null
        while(!Character.toString(color).matches("[rgby]")){
            System.out.println("Choose a color for the wild card: 'r' for Red, 'g' for Green, 'b' for Blue, 'y' for Yellow");
            String response = in.nextLine();
            color = response.equals("") ? '\0' : response.charAt(0);
        }
        return new Card(color, wild.getValue());
    }

    private Card draw() {
        newDeck();
        return deck.draw();
    }
    
    private int nextTurn() {
        return (whoseTurn + (dirPos ? 1 : -1) + players.length * 69) % players.length;
    }

    private boolean isValidCard(Card card) {
        return card.getColor() == playedCard.getKey().getColor() || card.getValue().equals(playedCard.getKey().getValue()) || card.getColor() == 'w';
    }

    /**
     * This method creates a new Uno deck, excluding cards in both player's hands.
     */
    private void newDeck() {
        if (deck.size() == 0) {
            deck = new Deck();

            //removes cards from the deck that's already in player's hand & in play
            for (Player player : players)
                for (Card card : player.getHand())
                    deck.removeCard(card);
            deck.removeCard(playedCard.getKey());

            deck.shuffle(); //shuffles deck again.
        }
    }

    /*                                  MAIN METHOD FOR TESTING                                   */

    public static void main(String[] args) {
        GameManager game = new GameManager(2);
        game.startGame();
    }
}