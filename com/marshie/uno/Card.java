package com.marshie.uno;

public class Card implements Comparable<Card> {
    private final char color;
    private final String value;
    public static final int NUM_COLORS = 4;
    public static final String VALID_CARD_REGEX = "([RGBYrgby] [0-9])|([Ww] drw\\+[024])";

    public Card(char clr, String val) {
        color = clr;
        value = val;
    }

    public static Card parseCard(String str)throws IllegalArgumentException {
        if (!str.matches(VALID_CARD_REGEX))
            throw new IllegalArgumentException(String.format("Input string %s doesn't match %s", str, VALID_CARD_REGEX));

        return new Card (str.toLowerCase().charAt(0), str.toLowerCase().substring(2));
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

    public String toString() {
        return color + " " + value;
    }
}