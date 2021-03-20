package com.marshie.uno.players;

import com.marshie.uno.Card;

import java.util.ArrayList;

public class RandomPlayer implements Player{
    private final ArrayList<Card> hand;

    public RandomPlayer() {
        hand = new ArrayList<>();
    }

    public int size() {
        return hand.size();
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

    public boolean playGivenCard(Card card) {
        return (int) (Math.random() * 2) == 0;
    }

    public String playCard(Card cardAtPlay, byte stackStreak) {
        ArrayList<Card> playableCards = new ArrayList<>();
        for(Card card : hand)
            if (isValidCard(card, cardAtPlay))
                playableCards.add(card);

        if (playableCards.size() == 0)
            return null;
        if ((int) (Math.random() * 2) == 0)
            return "DRAW";
        Card chosenCard = playableCards.get((int) (Math.random() * playableCards.size()));
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
        return chosenCard.toString();
    }

    public String toString() {
        return hand.toString();
    }
}