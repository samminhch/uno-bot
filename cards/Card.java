package cards;

public interface Card {
    public String getColor();
    /**
     * 0-9 = regular number cards
     * rev = reverse card
     * wld = wild card
     * skp = skip card
     * drw+[val] = draw card
     * @return <code>String</code> the value of the card
     */
    public String getValue();
}