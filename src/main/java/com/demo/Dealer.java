package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dealer {
    private final List<Card> hand;

    public Dealer() {
        this.hand = new ArrayList<>();
    }

    public void drawCard(Card card) {
        hand.add(card);
    }

    public void displayHand() {
        System.out.println("Dealer: " + hand + ", sum: " + computeHand());
    }

    public int computeHand() {
        int total = 0;

        hand.sort((a, b) -> b.rank().value() - a.rank().value());

        for (Card card : hand) {
            total += card.rank().value();
            if (Objects.equals(card.rank(), Rank.ACE)) {
                if (total + 11 > 21){
                    total += 1;
                } else {
                    total += 11;
                }
            }
        }

        return total;
    }
}
