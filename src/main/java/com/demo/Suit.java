package com.demo;

public enum Suit {
    DIAMOND("♦"),
    HEART("♥"),
    SPADE("♠"),
    CLOVER("♣");

    private final String name;

    Suit(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
