package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hand {
    private final List<Card> cards;
    private boolean won = false;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int computeValue() {
        int total = 0;

        cards.sort((a, b) -> b.rank().value() - a.rank().value());

        for (Card card : cards) {
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

    public void setWon(boolean won) {
        this.won = won;
    }

    public boolean getWon() {
        return won;
    }
}
