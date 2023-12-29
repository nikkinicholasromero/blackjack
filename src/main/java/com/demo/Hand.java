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

    public List<Hand> split() {
        Hand hand1 = new Hand();
        Hand hand2 = new Hand();
        hand1.addCard(cards.get(0));
        hand2.addCard(cards.get(1));
        this.setState(HandState.SPLIT);
        return List.of(hand1, hand2);
    }

    public boolean contains(Rank rank) {
        return !cards.stream()
                .map(Card::rank)
                .filter(rank::equals)
                .toList()
                .isEmpty();
    }

    public boolean isBlackJack() {
        if (size() != 2) {
            return false;
        }

        return 21 == computeValue();
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
