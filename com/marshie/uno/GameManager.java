package com.marshie.uno;

import com.marshie.uno.players.*;

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
     * Constructor for the com.marshie.uno.GameManager class. Sets up the Uno game for specified amount of players.
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
     *
     * @param spot <code>byte</code> must be in between 0 -> numPlayers - 1.
     * @param playerType <code>String</code> a type of player that's specified. "rand" for randBot (plays cards randomly),
     *                   "ai" for an AI to play against, and "input" if you want to play.
     * @throws IllegalArgumentException if playerType given isn't available
     */
    public void setPlayer (int spot, String playerType)throws IllegalArgumentException {
        if (!(spot >= 0 && spot < players.length))
            throw new IllegalArgumentException("Set spot must be between not 0 -> numPlayers - 1.");
        if (playerType.equals("rand"))
            players[spot] = new RandomPlayer();
        //for when i make an AI class
//        else if (playerType.equals("ai"))
//            players[spot - 1] = new AIPlayer();
        else if (playerType.equals("input"))
            players[spot] = new InputPlayer();
        else
            throw new IllegalArgumentException("playerType given isn't valid. must be \"ai\", \"rand\", or \"input\".");
    }
    /**
     * The method used to prepare the game. Creates a new RandomPlayers (by default), a deck, and sets some elements.
     * @param numPlayers the number of players that's playing.
     */
    private void prepare(int numPlayers) {
        players = new Player[numPlayers];
        for (int i = 0; i < players.length; i++)
            players[i] = new RandomPlayer();

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
    public int playGame() {
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < players.length; j++)
                players[j].addCard(draw());

        playedCard[0] = wildCardCase(draw());
        playedCard[1] = false;
        playedCard[2] = -1;

        int winner = -1;
        while (winner == -1) {
            newDeck();
            turn();
            winner = isFinished();
        }
        return winner;
    }

    /**
     * Checks if a player's hand is empty.
     * @return <code>Boolean</code>: whether the game is finished or not.
     */
    private int isFinished() {
        for(int i = 0; i < players.length; i++)
            if (players[i].getHand().size() == 0) {
                //System.out.printf("Player %d won!\n", i + 1);
                return i;
            }
        return -1;
    }

    /**
     * This method plays a turn out in the Uno game.
     */
    public void turn() {
        interpretCard();
        playCard(); //this method should be the method the AI uses to play a card.

//        if (players[whoseTurn].size() == 1)
//            System.out.println("Uno!");
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
                //System.out.printf("Player %d:\n", whoseTurn + 1);
            }
            else if (cardValue.equals("skp")) {
                whoseTurn = nextTurn(false);
                //System.out.printf("Player %d:\n", whoseTurn + 1);
            }
//            else {
//                System.out.printf("Player %d:\n", whoseTurn + 1);
//            }
        }
    }

    /**
     * This method prints out information regarding the player's hand and gives them a choice to play a card from it
     * or to draw a card from the deck.
     */
    private void playCard() {

        //prints out and card at play
       // System.out.printf("Card at play: %s\n", playedCard[0]);
        /*
         * The total number of cards that will be added to your deck if you don't play a draw+2 or draw+4 card. Those
         * cards will also be added towards all the other cards you draw if you choose the DRAW option.
         */
//        if ((boolean) streak[0])
//            System.out.printf("total value from draw cards: %d\n", (byte) streak[1]);

        String res = players[whoseTurn].playCard((Card) playedCard[0], (byte) streak[1]);
        if (res == null) {
            Card drawnCard = draw();
            //System.out.printf("Drew %s\n", drawnCard.toString());
            while (!isValidCard(drawnCard))
                drawnCard = draw();
            if (players[whoseTurn].playGivenCard(drawnCard))
                play(drawnCard);
        }
        else if (res.toUpperCase().equals("DRAW"))
            players[whoseTurn].addCard(draw());
        else if (res.matches(Card.VALID_CARD_REGEX))
            play(Card.parseCard(res));
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

    /**
     * In the case that the computer draws a wild card, a color will be picked at random.
     * @param wild <code>Card</code>- The potential wild card.
     * @return <code>Card</code>- The original card if the card isn't a wild card, and a new card with a random color if
     * the card was a wild card.
     */
    private Card wildCardCase(Card wild){
        if (wild.getColor() != 'w')
            return wild;
        char finalColor = switch ((int) (Math.random() * Card.NUM_COLORS)) {
            case 0 -> 'r';
            case 1 -> 'g';
            case 2 -> 'b';
            case 3 -> 'y';
            default -> '\0';
        };
        return new Card (finalColor, wild.getValue());
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
}