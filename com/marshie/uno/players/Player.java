package com.marshie.uno.players;

import com.marshie.uno.Card;

import java.util.ArrayList;

public interface Player {
    ArrayList<Card> hand = new ArrayList<>();

    String playCard(Card cardAtPlay, int stackStreak);

    default int size () {
        return hand.size();
    }

    default ArrayList<Card> getHand () {
        return hand;
    }

    default void addCard (Card card) {
        hand.add(card);
    }

    default void removeCard (Card card) {
        hand.remove(card);
    }

    default boolean isValidCard(Card card, Card cardAtPlay) {
        return card.getColor() == cardAtPlay.getColor() || card.getValue().equals(cardAtPlay.getValue()) || card.getColor() == 'w';
    }
}
