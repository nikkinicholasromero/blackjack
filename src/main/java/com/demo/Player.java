package com.demo;

import java.util.*;

public class Player {
    private String name;
    private int budget;
    private final List<Hand> hands;

    public Player(String name, int budget) {
        this.name = name;
        this.budget = budget;
        this.hands = new ArrayList<>();
    }

    public int getBudget() {
        return budget;
    }

    public void bet(int bet) {
        budget -= bet;
    }

    public List<Hand> hands() {
        return hands;
    }

    public void displayHands() {
        hands.forEach(hand -> hand.displayHand(name));
    }

    public void payout(int win) {
        budget += win;
    }

    public void initializeHand() {
        hands.clear();
        hands.add(new Hand());
    }

    public Action decide(Hand hand) {
        // TODO: Add decision chart call from here
        return Action.STAND;
    }
}
