package com.demo;

public class Dealer {
    private Hand hand;

    public void initializeHand() {
        hand = new Hand();
    }

    public Hand getHand() {
        return hand;
    }

    public void drawCard(Card card) {
        hand.addCard(card);
    }
}
