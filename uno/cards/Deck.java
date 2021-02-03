package uno.cards;

public class Deck {
    ArrayList<Card> deck = new ArrayList<Card>();

    public Deck() {
        /*
         * 4 = number of colors in uno deck
         */
        int index = 0;
        char[] colors = {'r', 'g', 'b', 'y'};
        // add the first 100 cards, r g b & y
        for(int i = 0; i < colors.length; i++) {
            for(int j = 0; j < 10; j++)
            {
                deck.add(new Card(colors[i], (j+1)+""));
            }
            for(int j = 1; j < 10; j++) {
                deck.add(new Card(colors[i], "" + (j + 1)));
            }
            for(int j = 0; j < 2; j++)
            {
                deck.add(new Card(colors[i], "rev"));
                deck.add(new Card(colors[i], "skp"));
                deck.add(new Card(colors[i], "drw+2"))
            }
        }
        // add the 8 wild cards
        for(int i = 0; i < 4; i++)
        {
            deck.add(new Card('w', "drw+4"));
            deck.add(new Card('w', "wld"));
        }

        
    }
}