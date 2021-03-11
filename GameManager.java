import com.marshie.uno.*;

import java.util.*;

/*
 * This class is used in order to run the Uno game.
 */
public class GameManager {
    private Player[] players;

    /*
     * formatted as [boolean, byte] where the boolean is whether a streak is occurring and the byte being how high the
     * streak is. will probably be reworked later since I would like this option to be configurable.
     */
    private Object[] streak;
    /*
     * formatted as [Card, boolean, byte, byte], where Card is the current card played, boolean is whether the card has
     * been played, the first byte is the player that played that card, and the second byte is whether the first card
     * played in a stack is a normal card, a draw+2 card, or a draw+4 card.
     */
    private Object[] playedCard;
    private Deck deck;
    private byte whoseTurn;
    private boolean dirPos;

    /**
     * Constructor for the GameManager class. Sets up the Uno game for specified amount of players.
     * @param numPlayers the amount of players for the Uno game.
     */
    public GameManager(int numPlayers) {
        if (numPlayers < 2 || numPlayers >= 10)
            throw new IllegalArgumentException(String.format(
                    "The number of players needed for Uno is 2-10. The number of players you entered was %d.",
                    numPlayers));
        prepare(numPlayers);
    }

    /**
     * The method used to prepare the game. Creates a new players, a deck, and sets some elements.
     * @param numPlayers the number of players that's playing.
     */
    private void prepare(int numPlayers) {
        players = new Player[numPlayers];
        for (int i = 0; i < players.length; i++)
            players[i] = new Player(false);

        deck = new Deck();
        deck.shuffle();

        playedCard = new Object[4];

        dirPos = true;
        whoseTurn = 0;
        streak = new Object[2];
        streak[0] = false;
        streak[1] = (byte) 0;
    }

    /**
     * This will set up a new Uno game and play until the game is finished.
     */
    public void startGame() {
        for (int i = 0; i < 7; i++)
            for (Player player : players)
                player.addCard(deck.draw());

        playedCard[0] = wildCardCase(draw());
        playedCard[1] = false;
        playedCard[2] = -1;

        while (!isFinished()) {
            newDeck();
            turn();
        }
    }

    /**
     * Checks if a player's hand is empty.
     * @return <code>Boolean</code>: whether the game is finished or not.
     */
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
        interpretCard();
        playCard(); //this method should be the method the AI uses to play a card.

        if (players[whoseTurn].size() == 1)
            System.out.println("Uno!");
        whoseTurn = nextTurn(false);
    }

    /**
     * This method looks at the current played card and plays out a card's effect if the card has any to play.
     */
    private void interpretCard() {
        if (!(boolean) playedCard[1]){
            String cardValue = ((Card)playedCard[0]).getValue();
            // #TODO: Reverse card does not function correctly when more than two players are in a game.
            if (cardValue.equals("rev")) {
                dirPos = !dirPos;
                whoseTurn = nextTurn(false);
                System.out.printf("Player %d:\n", whoseTurn + 1);
            }
            else if (cardValue.equals("skp")) {
                whoseTurn = nextTurn(false);
                System.out.printf("Player %d:\n", whoseTurn + 1);
            }
            /*
             * This doesn't need to be here since it gets interpreted whenever a player plays a card.
             */
//            else if (cardValue.matches("drw\\+\\d")) {
//                int numDraws = Integer.parseInt(cardValue.substring(cardValue.indexOf("+") + 1));
//                System.out.printf("Player %d:\n", whoseTurn + 1);
//
//                for(int i = 0; i < numDraws; i++) {
//                    Card cardDrawn = draw();
//                    System.out.println("Drew " + cardDrawn.toString());
//                    players[whoseTurn].addCard(cardDrawn);
//                }
//            }
            else {
                System.out.printf("Player %d:\n", whoseTurn + 1);
            }
        }
    }

    /**
     * This method prints out information regarding the player's hand and gives them a choice to play a card from it
     * or to draw a card from the deck.
     */
    private void playCard() {
        Scanner in = new Scanner(System.in);
        ArrayList<Card> playableCards = new ArrayList<>();
        ArrayList<Card> curPlayerHand = players[whoseTurn].getHand();

        for (Card card : curPlayerHand)
            if (isValidCard(card))
                playableCards.add(card);

        //prints out player's hand and card at play
        System.out.printf("Card at play: %s\n", playedCard[0]);
        /*
         * The total number of cards that will be added to your deck if you don't play a draw+2 or draw+4 card. Those
         * cards will also be added towards all the other cards you draw if you choose the DRAW option.
         */
        if ((boolean) streak[0]) {
            System.out.printf("total value from draw cards: %d\n", (byte) streak[1]);
        }
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
                System.out.printf("Drew card %s\n", nextCard.toString());
                //This while-loop is introduced so that the only valid answers are "Y" and "N".
                String ans = "";
                while(!ans.matches("[YN]")){
                    System.out.printf("Play card %s? Y/N?\n", nextCard.toString());
                    ans = in.nextLine().toUpperCase();
                }
                response = ans;
                if (response.equals("Y")) {
                    play(nextCard);
                }
            }
        }
        else {
            boolean validResponse = false;
            while (!validResponse) {
                System.out.println("Pick a card to play, or enter DRAW to draw:");
                String response = in.nextLine().toUpperCase();
                if (response.equals("DRAW")) { //draws a card from deck and ends turn
                    Card nextCard = draw();
                    players[whoseTurn].addCard(nextCard);
                    setPlayedCard(nextCard);
                    validResponse = true;
                    System.out.printf("Drew %s\n", nextCard.toString());
                } else if (response.matches("\\d+")) { //case that you actually choose a playable card
                    int index = Integer.parseInt(response) - 1;
                    if (index >= 0 && index < playableCards.size()) {
                        play(playableCards.get(index));
                        validResponse = true;
                    }
                }
            }
        }
    }

    /**
     * This method handles the player actually playing a card. This method takes in a Card, and checks for a draw card.
     * In the case that the Card passed is a draw card, it will count towards the 'card streak'. This Uno game may
     * be a little different from the Uno games other people have played, because you can stack a draw+4 card over a
     * draw+2 card. This method will be changed later once I polish up the game so that the option of stacking can be
     * configurable.
     * @param c The card that's going to be played.
     */
    private void play (Card c) {
        //#TODO: finish case where person draws a +2/+4 card
        String value = c.getValue();
        if (value.matches("drw\\+2") || value.matches("drw\\+4")) {
            byte val = Byte.parseByte(c.getValue().substring(value.length() - 1));
            streak[0] = true;
            streak[1] = (byte) ((byte) streak[1] + val);
        }
        else {
            for (int i = 0; i < (byte) streak[1]; i++) {
                Card drawnCard = draw();
                System.out.printf("Drew %s\n", drawnCard.toString());
                players[whoseTurn].addCard(drawnCard);
            }
            streak[0] = false;
            streak[1] = (byte) 0;
        }
        players[whoseTurn].removeCard(c);
        setPlayedCard(wildCardCase(c));
    }

    private Card wildCardCase(Card wild){
        if (wild.getColor() != 'w')
            return wild;
        Scanner in = new Scanner(System.in);
        char color = '\0'; //that character just stands for null
        while(!Character.toString(color).matches("[rgby]")){
            System.out.println("Choose a color for the wild card: 'r' for Red, 'g' for Green, 'b' for Blue, 'y' for Yellow");
            String response = in.nextLine();
            //the ternary operator is used to counter the possibility that someone will enter ""
            color = response.equals("") ? '\0' : response.charAt(0);
        }
        return new Card(color, wild.getValue());
    }

    /**
     * This method draws a card from the deck. If the deck is empty and this method is called then a new deck will be
     * created without the card that are in each player's hand and at play.
     * @return <code>Card</code>: a card from the deck.
     */
    private Card draw() {
        newDeck();
        return deck.draw();
    }

    private void setPlayedCard(Card card) {
        playedCard[0] = card;
        playedCard[1] = false;
        playedCard[2] = whoseTurn;
    }

    /**
     * @return <code>int</code>: a number indicating which player will be playing next.
     */
    private byte nextTurn(boolean getNegDir) {
        return (byte) ((whoseTurn + ((getNegDir != dirPos) ? 1 : -1) + players.length * 69) % players.length);
    }

    /**
     * @param card the card that's being checked.
     * @return <code>boolean</code>: Whether the card given is a wild card or its color/value matches the playedCard's
     * value/color
     */
    private boolean isValidCard(Card card) {
        return card.getColor() == ((Card)playedCard[0]).getColor() || card.getValue().equals(((Card)playedCard[0]).getValue()) || card.getColor() == 'w';
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
            deck.removeCard((Card)playedCard[0]);

            deck.shuffle(); //shuffles deck again.
        }
    }

    /*                                  MAIN METHOD FOR TESTING                                   */

    public static void main(String[] args) {
        GameManager game = new GameManager(2);
        game.startGame();
    }
}