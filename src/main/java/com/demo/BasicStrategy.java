package com.demo;

public class BasicStrategy implements Strategy {
    @Override
    public PlayerAction decide(Hand dealerHand, Hand playerHand, boolean playerCanBetMore) {
        // TODO: Implement strategy
        return PlayerAction.STAND;
    }
}
