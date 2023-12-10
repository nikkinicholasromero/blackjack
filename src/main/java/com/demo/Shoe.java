package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shoe {
    private final int deckCount;
    private final List<Deck> decks;

    public Shoe(int deckCount) {
        this.deckCount = deckCount;
        this.decks = new ArrayList<>();

        for (int i = 0; i < deckCount; i++) {
            decks.add(new Deck());
        }
    }

    public Card draw() {
        Random random = new Random();
        int targetDeck = random.nextInt(0, deckCount);
        Deck deck = decks.get(targetDeck);
        Card card = deck.draw();
        if (deck.isEmpty()) {
            decks.remove(deck);
        }
        return card;
    }
}
