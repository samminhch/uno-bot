package uno.cards;

public class Deck {
    Card[] deck = new Card[108];

    public Deck() {
        /*
         * 4 = number of colors in uno deck
         */
        int index = 0;
        char[] colors = {'r', 'g', 'b', 'y'};
        for(int i = 0; i < colors.length; i++) {
            for(int j = 0; j < 25; j++) {
                deck[index] = new Card(colors[i], "" + (j + 1));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringified = new StringBuilder();
        for(int i = 0; i < deck.size(); i++)
            String.format("%-3d| %c : %10s\n", i, deck.get(i).getColor(), deck.get(i).getValue());
        return stringified.toString();
    }
}