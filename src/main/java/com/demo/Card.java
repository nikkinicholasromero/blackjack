package com.demo;

public record Card(
        Suit suit,
        Rank rank
) {
    @Override
    public String toString() {
        return suit + "" + rank;
    }
}
