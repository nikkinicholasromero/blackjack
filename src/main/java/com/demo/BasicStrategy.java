package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicStrategy implements Strategy {
    private final Random random = new Random();

    @Override
    public PlayerAction decide(Hand dealerHand, Hand playerHand, boolean playerCanBetMore) {
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
