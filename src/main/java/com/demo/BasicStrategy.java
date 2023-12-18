package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicStrategy implements Strategy {
    private final Random random = new Random();

    @Override
    public PlayerAction decide(Hand dealerHand, Hand playerHand, boolean playerCanBetMore) {
        // TODO: Implement decision logic
        return random(playerHand);
    }

    private PlayerAction decide(Hand dealerHand, Hand playerHand) {
        int dealerHandValue = dealerHand.computeValue();
        int playerHandValue = playerHand.computeValue();

        if ((List.of(9, 10, 11).contains(dealerHandValue) && 16 == playerHandValue) ||
                (10 == dealerHandValue && 15 == playerHandValue)) {
            // Surrender
            return PlayerAction.SURRENDER;
        } else if (playerHand.isPair()) {
            // Pair Splitting
        } else if (playerHand.size() == 2 && playerHand.containsAce()) {
            // Soft Totals
        } else {
            // Hard Totals
        }

        return PlayerAction.STAND;
    }

    private PlayerAction random(Hand playerHand) {
        List<PlayerAction> playerActionList = new ArrayList<>();
        playerActionList.add(PlayerAction.STAND);
        playerActionList.add(PlayerAction.HIT);
        playerActionList.add(PlayerAction.SURRENDER);

        if (2 == playerHand.size()) {
            playerActionList.add(PlayerAction.DOUBLE);
        }

        if (playerHand.isPair()) {
            playerActionList.add(PlayerAction.SPLIT);
        }

        return playerActionList.get(random.nextInt(0, playerActionList.size()));
    }
}
