package com.demo;

public enum Suit {
    DIAMOND("♦"),
    HEART("♥"),
    SPADE("♠"),
    CLOVER("♣");

    private final String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
