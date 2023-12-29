package com.demo;

import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        int deckCount = 6;
        int minimumBet = 1_000;
        List<Player> initialPlayers = initialPlayers();
        List<Player> activePlayers = activePlayers(initialPlayers, minimumBet);

        int dealCounter = 0;
        do {
            dealCounter++;
            new Game(deckCount, minimumBet, activePlayers).deal();
            activePlayers = activePlayers(activePlayers, minimumBet);
        } while (!activePlayers.isEmpty());

        System.out.println("Ended in " + dealCounter + " deals. ");
        System.out.println(initialPlayers);
    }

    private static List<Player> initialPlayers() {
        int budget = 20_000;
        Strategy strategy = new BasicStrategy();
        List<Player> players = new ArrayList<>();
        players.add(new Player("Erwin", budget, strategy));
        players.add(new Player("RJ", budget, strategy));
        players.add(new Player("Nikki", budget, strategy));
        players.add(new Player("Jordan", budget, strategy));
        players.add(new Player("Caleb", budget, strategy));
        players.add(new Player("Pat", budget, strategy));
        return players;
    }

    private static List<Player> activePlayers(List<Player> players, int minimumBet) {
        return players.stream()
                .filter(e -> e.budget() >= minimumBet)
                .filter(e -> e.budget() <= (30_000))
                .toList();
    }
}
