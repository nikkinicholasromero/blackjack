package com.demo;

public class Dealer {
    private final Hand hand;

    public Dealer() {
        this.hand = new Hand();
    }

    public Hand hand() {
        return hand;
    }
}
