package com.marshie.uno.players;

import com.marshie.uno.Card;

import java.util.*;

public class InputPlayer implements Player {
    private final ArrayList<Card> hand;

    public InputPlayer() {
        hand = new ArrayList<>();
    }

    public int size() {
        return hand.size();
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public boolean playGivenCard(Card card) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.printf("Play card %s? [Y/N]\n", card.toString());
            String response = in.nextLine();
            if (response.toUpperCase().equals("Y"))
                return true;
            else if (response.toUpperCase().equals("N"))
                return false;
        }
    }

    public String playCard(Card cardAtPlay, byte stackStreak) {
        ArrayList<Card> playableCards = new ArrayList<>();
        for(Card card : hand)
            if (isValidCard(card, cardAtPlay))
                playableCards.add(card);

        System.out.print("PLAYABLE HAND: ");
        if (playableCards.size() > 0) {
            for (int i = 0; i < playableCards.size() - 1; i++)
                System.out.printf("(%d) %s, ", i + 1, playableCards.get(i));
            System.out.printf("(%d) %s\n", playableCards.size() - 1, playableCards.get(playableCards.size() - 1));
        }else {
            System.out.println("null");
            return null;
        }
        System.out.printf("WHOLE HAND: %d ", size());
        System.out.println(hand.toString());

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Pick a card to play, or enter DRAW to draw:");
            String response = in.nextLine().toUpperCase();
            if (response.equals("DRAW")) //draws a card from deck and ends turn
                return "DRAW";
            else if (response.matches("\\d+")) { //case that you actually choose a playable card
                int index = Integer.parseInt(response) - 1;
                if (index >= 0 && index < playableCards.size()) {
                    Card chosenCard = playableCards.get(index);
                    if (chosenCard.getColor() == 'w') {
                        char color = '\0'; //that character just stands for null
                        while(!Character.toString(color).matches("[rgby]")){
                            System.out.println("Choose a color for the wild card: 'r' for Red, 'g' for Green, 'b' for Blue, 'y' for Yellow");
                            response = in.nextLine();
                            //the ternary operator is used to counter the possibility that someone will enter ""
                            color = response.equals("") ? '\0' : response.charAt(0);
                        }
                        return new Card(color, chosenCard.getValue()).toString();
                    }
                    return chosenCard.toString();
                }
            }
        }
    }

    public String toString() {
        return hand.toString();
    }
}
