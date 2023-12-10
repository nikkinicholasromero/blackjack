package com.demo;

import java.util.*;

public class Player {
    private int budget;
    private List<Hand> hands;
    private final Strategy strategy;

    public Player(int budget, Strategy strategy) {
        this.budget = budget;
        this.strategy = strategy;
    }

    public void initializeHand() {
        hands = new ArrayList<>();
        hands.add(new Hand());
    }

    public List<Hand> hands() {
        return hands;
    }

    public void bet(int bet) {
        budget -= bet;
    }

    public void payout(int win) {
        budget += win;
    }

    public int budget() {
        return budget;
    }

    public Action decide(Card playerUpCard, Hand playerHand, int minimumBet) {
        return strategy.decide(playerUpCard, playerHand, budget >= minimumBet);
    }
}
