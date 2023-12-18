package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shoe {
    private final Random random;
    private final List<Deck> decks;

    public Shoe(int deckCount) {
        random = new Random();
        decks = new ArrayList<>();

        for (int i = 0; i < deckCount; i++) {
            decks.add(new Deck());
        }
    }

    public Card draw() {
        decks.removeIf(Deck::isEmpty);

        int targetDeck = random.nextInt(0, decks.size());
        Deck deck = decks.get(targetDeck);
        Card card = deck.draw();

        decks.removeIf(Deck::isEmpty);

        return card;
    }
}
