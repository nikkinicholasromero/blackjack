package com.demo;

public enum Rank {
    KING("K", 10),
    QUEEN("Q", 10),
    JACK("J", 10),
    TEN("10", 10),
    NINE("9", 9),
    EIGHT("8", 8),
    SEVEN("7", 7),
    SIX("6", 6),
    FIVE("5", 5),
    FOUR("4", 4),
    THREE("3", 3),
    TWO("2", 2),
    ACE("A", 0);

    private final String name;
    private final int value;

    Rank(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}
