package com.demo;

import java.util.*;

public class Player {
    private String name;
    private int budget;
    private List<Hand> hands;
    private final Strategy strategy;

    public Player(String name, int budget, Strategy strategy) {
        this.name = name;
        this.budget = budget;
        this.strategy = strategy;
    }

    public String name() {
        return name;
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

    public Action decide(Card dealerUpCard, Hand playerHand, int minimumBet) {
        return strategy.decide(dealerUpCard, playerHand, budget >= minimumBet);
    }
}
