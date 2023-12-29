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

    public PlayerAction decide(Hand dealerHand, Hand playerHand, int minimumBet) {
        return strategy.decide(dealerHand, playerHand, budget >= minimumBet);
    }

    @Override
    public String toString() {
        return name + ":" + budget;
    }
}
