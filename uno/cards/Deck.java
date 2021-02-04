package uno.cards;

import java.util.Stack;
import java.util.Collections;

public class Deck {
    private Stack<Card> deck;

    public Deck() {
        deck = new Stack<Card>();
        /*
         * 4 = number of colors in uno deck
         */
        char[] colors = {'r', 'g', 'b', 'y'};
        // add the first 100 cards, r g b & y
        for(int i = 0; i < colors.length; i++) {
            for(int j = 0; j < 10; j++)
                deck.add(new Card(colors[i], (j)+""));

            for(int j = 1; j < 10; j++)
                deck.add(new Card(colors[i], "" + (j)));

            for(int j = 0; j < 2; j++)
            {
                deck.add(new Card(colors[i], "rev"));
                deck.add(new Card(colors[i], "skp"));
                deck.add(new Card(colors[i], "drw+2"));
            }
        }
        // add the 8 wild cards
        for(int i = 0; i < 4; i++)
        {
            deck.add(new Card('w', "drw+4"));
            deck.add(new Card('w', "drw+0"));
        }
    }

    public void set(Stack<Card> deckCards) {
        deck = deckCards;
    }
    public Card draw() {
        return deck.pop();
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Stack<Card> getDeck() {
        return deck;
    }

    @Override
    public String toString() {
        StringBuilder stringified = new StringBuilder();
        for(int i = 0; i < deck.size(); i++)
            stringified.append(String.format("%-3d| %c : %5s\n", i + 1, deck.get(i).getColor(), deck.get(i).getValue()));
        return stringified.toString();
    }
}