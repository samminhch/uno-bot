package com.marshie.uno;

import java.util.*;

public class Player {
    private final ArrayList<Card> hand;
    private final boolean isAI;

    public Player(boolean isBot) {
        hand = new ArrayList<>();
        isAI = isBot;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public int size() {
        return hand.size();
    }

    /**
     * @return <code>String</code> of all the cards the player's hand.
     */
    public String toString() {
        return hand.toString();
    }
}
