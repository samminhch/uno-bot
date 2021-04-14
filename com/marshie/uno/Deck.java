package com.marshie.uno;

import java.util.Stack;
import java.util.Collections;

public class Deck {
    public final static int SIZE = 108;
    private final Stack<Card> deck;

    public Deck() {
        deck = new Stack<>();

        char[] colors = {'r', 'g', 'b', 'y'};
        // add the first 100 cards, r g b & y
        for (char color : colors) {
            for (int j = 0; j < 10; j++)
                deck.add(new Card(color, (j) + ""));

            for (int j = 1; j < 10; j++)
                deck.add(new Card(color, "" + (j)));

            for (int j = 0; j < 2; j++) {
                deck.add(new Card(color, "rev"));
                deck.add(new Card(color, "skp"));
                deck.add(new Card(color, "drw+2"));
            }
        }
        // add the 8 wild cards
        for(int i = 0; i < 4; i++)
        {
            deck.add(new Card('w', "drw+4"));
            deck.add(new Card('w', "drw+0"));
        }
    }

    public Card draw() {
        return deck.size() > 0 ? deck.pop() : null;
    }

    public void removeCard(Card card) {
        deck.remove(card);
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public int size() {
        return deck.size();
    }

    @Override
    public String toString() {
        StringBuilder stringified = new StringBuilder();
        for(int i = 0; i < deck.size(); i++)
            stringified.append(String.format("%-3d| %c : %5s\n", i + 1, deck.get(i).getColor(), deck.get(i).getValue()));
        return stringified.toString();
    }
}