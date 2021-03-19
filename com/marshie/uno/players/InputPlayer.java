package com.marshie.uno.players;

import com.marshie.uno.Card;

import java.util.*;

public class InputPlayer implements Player {
    private final ArrayList<Card> hand;

    public InputPlayer(boolean isBot) {
        hand = new ArrayList<>();
    }

    public String playCard(Card cardAtPlay, int stackStreak) {
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
        System.out.print("WHOLE HAND: ");
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
                    return playableCards.get(index).toString();
                }
            }
        }
    }

    public String toString() {
        return hand.toString();
    }
}
