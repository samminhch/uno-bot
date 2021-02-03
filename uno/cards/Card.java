package uno.cards;

public class Card {
    private char color;
    private String value;
    public Card(char clr, String val) {
        color = clr;
        value = val;
    }

    /**
     * @return <code>String</code> 
     * color key:
     * r = red
     * g = green
     * b = blue
     * y = yellow
     * w = wild color
     */
    public char getColor() {
        return color;
    }

    /**
     * value key:
     * 0-9 = regular number cards
     * rev = reverse card
     * wld = wild card
     * skp = skip card
     * drw+[val] = draw card
     * @return <code>String</code> the value of the card
     */
    public String getValue() {
        return value;
    }
}