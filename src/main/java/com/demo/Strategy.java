package com.demo;

public interface Strategy {
    PlayerAction decide(Hand dealerHand, Hand playerHand, boolean playerCanBetMore);
}
