package com.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }

        Collections.shuffle(cards);
    }

    public Card draw() {
        Card card = cards.get(0);
        cards.remove(card);
        return card;
    }
}
