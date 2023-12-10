package com.demo;

public class BasicStrategy implements Strategy {
    @Override
    public Action decide(Card dealerUpCard, Hand playerHand, boolean playerCanBetMore) {
        // TODO: Implement strategy
        return Action.STAND;
    }
}
