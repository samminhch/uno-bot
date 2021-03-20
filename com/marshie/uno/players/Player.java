package com.marshie.uno.players;

import com.marshie.uno.Card;

import java.util.ArrayList;

public interface Player {

    int size ();

    ArrayList<Card> getHand ();

    void addCard (Card card);

    void removeCard (Card card);

    boolean playGivenCard(Card card);

    String playCard(Card cardAtPlay, byte stackStreak);

    default boolean isValidCard(Card card, Card cardAtPlay) {
        return card.getColor() == cardAtPlay.getColor() || card.getValue().equals(cardAtPlay.getValue()) || card.getColor() == 'w';
    }
}
