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

    public void addCard(Card card) {
        if (HandState.BUST.equals(state)) {
            return;
        }

        cards.add(card);

        int handValue = computeValue();
        if (handValue > 21) {
            state = HandState.BUST;
        }
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

    public void setWon() {
        this.state = HandState.WON;
    }

    public void setStood() {
        this.state = HandState.STOOD;
    }

    public void setSurrendered() {
        this.state = HandState.SURRENDERED;
    }

    public void setDoubled() {
        this.state = HandState.DOUBLED;
    }

    public boolean won() {
        return HandState.WON.equals(state);
    }

    public boolean bust() {
        return HandState.BUST.equals(state);
    }

    public boolean surrendered() {
        return HandState.SURRENDERED.equals(state);
    }

    public boolean doubled() {
        return HandState.DOUBLED.equals(state);
    }
}
