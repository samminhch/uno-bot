package uno;

import uno.cards.*;

import java.util.*;

public class Player {
    private final ArrayList<Card> hand;

    public Player() {
        hand = new ArrayList<Card>();
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public int numCardsInHand() {
        return hand.size();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    /**
     * @return <code>String</code> of all the cards the player's hand.
     */
    public String toString() {
        return hand.toString();
    }
}
