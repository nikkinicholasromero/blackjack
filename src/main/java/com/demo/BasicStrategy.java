package com.demo;

import java.util.List;

public class BasicStrategy implements Strategy {
    @Override
    public PlayerAction decide(Hand dealerHand, Hand playerHand, boolean playerCanBetMore) {
        int dealerHandValue = dealerHand.computeValue();
        int playerHandValue = playerHand.computeValue();

        if ((List.of(9, 10, 11).contains(dealerHandValue) && 16 == playerHandValue) ||
            (10 == dealerHandValue && 15 == playerHandValue)) {
            return PlayerAction.SURRENDER;
        }

        if (playerHand.isPair() && playerCanBetMore) {
            if (playerHand.contains(Rank.ACE) || playerHand.contains(Rank.EIGHT)) {
                return PlayerAction.SPLIT;
            }

            if (playerHand.contains(Rank.NINE) &&
                dealerHand.containsAny(List.of(Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX, Rank.EIGHT, Rank.NINE))) {
                return PlayerAction.SPLIT;
            }

            if (playerHand.containsAny(List.of(Rank.TWO, Rank.THREE, Rank.SEVEN)) &&
                dealerHand.containsAny(List.of(Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX, Rank.SEVEN))) {
                return PlayerAction.SPLIT;
            }

            if (playerHand.contains(Rank.SIX) &&
                dealerHand.containsAny(List.of(Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX))) {
                return PlayerAction.SPLIT;
            }

            if (playerHand.contains(Rank.FOUR) &&
                dealerHand.containsAny(List.of(Rank.FIVE, Rank.SIX))) {
                return PlayerAction.SPLIT;
            }
        }

        if (playerHand.size() == 2 && playerHand.contains(Rank.ACE)) {
            if (playerHand.contains(Rank.NINE)) {
                return PlayerAction.STAND;
            }

            if (playerHand.contains(Rank.EIGHT) &&
                dealerHand.containsAny(List.of(
                        Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE,
                        Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN,
                        Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE))) {
                return PlayerAction.STAND;
            }

            if (playerHand.contains(Rank.EIGHT) && dealerHand.contains(Rank.SIX) && playerCanBetMore) {
                return PlayerAction.DOUBLE;
            }

            if (playerHand.contains(Rank.SEVEN) &&
                dealerHand.containsAny(List.of(Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX)) &&
                playerCanBetMore) {
                return PlayerAction.DOUBLE;
            }

            if (playerHand.contains(Rank.SEVEN) &&
                    dealerHand.containsAny(List.of(Rank.SEVEN, Rank.EIGHT))) {
                return PlayerAction.STAND;
            }

            if (playerHand.contains(Rank.SIX) &&
                dealerHand.containsAny(List.of(Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX)) &&
                playerCanBetMore) {
                return PlayerAction.DOUBLE;
            }

            if (playerHand.containsAny(List.of(Rank.FIVE, Rank.FOUR)) &&
                dealerHand.containsAny(List.of(Rank.FOUR, Rank.FIVE, Rank.SIX)) &&
                playerCanBetMore) {
                return PlayerAction.DOUBLE;
            }

            if (playerHand.containsAny(List.of(Rank.THREE, Rank.TWO)) &&
                dealerHand.containsAny(List.of(Rank.FIVE, Rank.SIX)) &&
                playerCanBetMore) {
                return PlayerAction.DOUBLE;
            }

            return PlayerAction.HIT;
        } else {
            // Hard Totals
        }

        return PlayerAction.STAND;
    }
}
