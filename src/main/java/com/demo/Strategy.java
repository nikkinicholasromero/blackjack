package com.demo;

public interface Strategy {
    Action decide(Hand dealerHand, Hand playerHand, boolean playerCanBetMore);
}
