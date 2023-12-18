package com.demo;

public class BasicStrategy implements Strategy {
    @Override
    public Action decide(Hand dealerHand, Hand playerHand, boolean playerCanBetMore) {
        // TODO: Implement strategy
        return Action.STAND;
    }
}
