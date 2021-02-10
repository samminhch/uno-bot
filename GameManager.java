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
            for (int j = 0; j < players.length; j++)
                players[j].addCard(deck.draw());
        cardPlayed = deck.draw();    
    
        while (!isFinished()) {
            if (deck.size() == 0)
                newDeck();
            turn();
        }
    }

    /**
     * This method plays a turn out in the Uno game.
     */
    public void turn() {
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
                players[whosTurn].addCard(deck.draw());
        }
    }

    private void playCard() {
        Scanner in = new Scanner(System.in);
        ArrayList<Card> playableCards = new ArrayList<Card>();
        ArrayList<Card> curPlayerHand = players[whosTurn].getHand();


        for (Card card : curPlayerHand)
            if (card.getColor() == cardPlayed.getColor() || card.getValue().equals(cardPlayed.getValue()))
                playableCards.add(card);
        
        //will automatically draw cards if you don't have any playable cards, and if you do, it'll print out the cards
        //that are playable and will ask you if you want to play a card or draw a card.
        if (playableCards.size() == 0){
            String response = "";
            while(response.equals("N")) {
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
                response = in.nextLine();
                if (response.equals("Y")) {
                    players[whosTurn].removeCard(nextCard);
                    cardPlayed = wildCardCase(nextCard); //wildCardCase just converts the wild card into a colored card
                }
            }
        }
        else{
            System.out.println(cardPlayed);
            System.out.print("HAND: ");
            for (int i = 0; i < curPlayerHand.size(); i++)
                System.out.printf("(%d) %s, ", i + 1, curPlayerHand.get(i).toString());
            System.out.println();

            boolean validResponse = false;
            while (!validResponse) {
                System.out.println("Pick a card to play, or enter DRAW to draw:");
                String response = in.nextLine();
                if (response.equals("DRAW")){
                    players[whosTurn].addCard(deck.draw());
                    validResponse = true;
                }
                else if (response.matches("\\d+")) {
                    int index = Integer.parseInt(response);
                    if (index >= 0 && index < playableCards.size()) {                        
                        players[whosTurn].removeCard(playableCards.get(index));
                        cardPlayed = wildCardCase(playableCards.get(index));
                    }
                }
            }           
        }
        in.close();
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
        return (index + players.length * 69) % players.length; // funny number ahaha 
    }

    private int nextTurn() {
        return whosTurn += dirPos ? 1 : -1;
    }
}