package com.marshie.uno;

public class Card implements Comparable<Card> {
    private final char color;
    private final String value;
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

    public String toString() {
        return color + " " + value;
    }

    /**
     * Compares colors first and then value second. Compared alphabetically??
     * @param c the comparison card.
     * @return <code>int</code>>
     */
    public int compareTo(Card c) {
        if (color > c.getColor())
            return 1;
        if (color < c.getColor())
            return -1;
        else
            return value.compareTo(c.getValue());
    }
}