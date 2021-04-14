package com.marshie.uno.players;

import com.marshie.uno.Card;
import com.marshie.uno.Deck;

import java.util.ArrayList;

public class RandomPlayer implements Player{
    private final ArrayList<Card> hand;
    private boolean reachedCriticalSize;

    public RandomPlayer() {
        hand = new ArrayList<>();
        reachedCriticalSize = false;
    }

    public int size() {
        return hand.size();
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void addCard(Card card) {
        hand.add(card);
        if (hand.size() >= Deck.SIZE / 2)
            reachedCriticalSize = true;
    }

    public void removeCard(Card card) {
        for (int i = 0; i < hand.size(); i++) {
            Card comparison = hand.get(i);
            if (comparison.equals(card)) {
                hand.remove(comparison);
                break;
            }
        }
    }

    /**
     * Will randomly choose if it will play given card. If there's too many cards in its hand then it will play the card
     * passed through.
     * @param card The card that may or may not be played.
     * @return <code>boolean</code>.
     */
    public boolean playGivenCard(Card card) {
        if (reachedCriticalSize)
            return true;
        return (int) (Math.random() * 2) == 0;
    }

    /**
     *
     * @param cardAtPlay The card that is in play in the game.
     * @param stackStreak The value of +2 & +4 cards.
     * @return <code>String</code>- The String version of the played card, "DRAW" if the computer chooses to, and null
     * if there aren't any playable cards.
     */
    public String playCard(Card cardAtPlay, byte stackStreak) {
        ArrayList<Card> playableCards = new ArrayList<>();
        for(Card card : hand)
            if (isValidCard(card, cardAtPlay))
                playableCards.add(card);

        if (playableCards.size() == 0)
            return null;

        Card chosenCard = playableCards.get((int) (Math.random() * playableCards.size()));
        if (!reachedCriticalSize) {
            if ((int) (Math.random() * 2) == 0) {
                return "DRAW";
            }
            if (chosenCard.getColor() == 'w') {
                char finalColor = switch ((int) (Math.random() * Card.NUM_COLORS)) {
                    case 0 -> 'r';
                    case 1 -> 'g';
                    case 2 -> 'b';
                    case 3 -> 'y';
                    default -> '\0';
                };
                return new Card(finalColor, chosenCard.getValue()).toString();
            }
        }else {
            while (chosenCard.getValue().matches("drw\\+\\d"))
                chosenCard = playableCards.get((int) (Math.random() * playableCards.size()));
        }
        return chosenCard.toString();
    }

    public String toString() {
        return hand.toString();
    }
}