package com.demo;

public interface Strategy {
    Action decide(Card dealerUpCard, Hand playerHand, boolean playerCanBetMore);
}
