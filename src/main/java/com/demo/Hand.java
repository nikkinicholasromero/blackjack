package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hand {
    private final List<Card> cards;
    private HandState state;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void setState(HandState state) {
        this.state = state;
    }

    public HandState getState() {
        return state;
    }

    public void addCard(Card card) {
        cards.add(card);

        int handValue = computeValue();
        if (handValue > 21) {
            state = HandState.BUST;
        }
    }

    public int size() {
        return cards.size();
    }

    public boolean isPair() {
        if (size() != 2) {
            return false;
        }

        return cards.get(0).rank().equals(cards.get(1).rank());
    }

    public boolean containsAce() {
        return !cards.stream()
                .map(Card::rank)
                .filter(Rank.ACE::equals)
                .toList()
                .isEmpty();
    }

    public int computeValue() {
        if (HandState.BUST.equals(state)) {
            return 0;
        }

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
}
