package com.demo;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        int deckCount = 6;
        int minimumBet = 1_000;
        int budget = 20_000;
        Strategy strategy = new BasicStrategy();
        Player player1 = new Player("Erwin", budget, strategy);
        Player player2 = new Player("RJ", budget, strategy);
        Player player3 = new Player("Nikki", budget, strategy);
        List<Player> players = List.of(player1, player2, player3);

        Game game = new Game(deckCount, minimumBet, players);

        int dealCounter = 0;
        while (game.hasPlayers()) {
            dealCounter++;
            game.deal();
        }

        System.out.println("Ended in " + dealCounter + " deals. ");
    }
}
