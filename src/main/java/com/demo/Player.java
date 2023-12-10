package com.demo;

import java.util.*;

public class Player {
    private int budget;
    private List<Hand> hands;

    public Player(int budget) {
        this.budget = budget;
    }

    public void initializeHand() {
        hands = new ArrayList<>();
        hands.add(new Hand());
    }

    public List<Hand> getHands() {
        return hands;
    }

    public void bet(int bet) {
        budget -= bet;
    }

    public void payout(int win) {
        budget += win;
    }

    public int getBudget() {
        return budget;
    }

    // TODO: Add decision chart call from here
    public Action decide(Hand hand) {
        return Action.STAND;
    }
}
