package com.demo;

public class Dealer {
    private Hand hand;

    public void initializeHand() {
        hand = new Hand();
    }

    public Hand hand() {
        return hand;
    }
}
