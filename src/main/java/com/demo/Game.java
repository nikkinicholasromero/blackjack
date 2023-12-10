package com.demo;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static void main(String[] args) {
        Player player1 = new Player("RJ", 20_000);
        Player player2 = new Player("Erwin", 20_000);
        Player player3 = new Player("Nikki", 20_000);
        Game game = new Game(List.of(player1, player2, player3), 1000, 6);
        game.startGame();
    }

    private final Dealer dealer;
    private final List<Player> players;
    private final int minimumBet;
    private final int deckCount;
    private Shoe shoe;

    public Game(
            List<Player> players,
            int minimumBet,
            int deckCount) {
        dealer = new Dealer();
        this.players = new ArrayList<>(players);
        this.minimumBet = minimumBet;
        this.deckCount = deckCount;
    }

    public void startGame() {
        shoe = new Shoe(deckCount);

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getBudget() < minimumBet) {
                // eliminate players
                players.remove(players.get(i));
            } else {
                // take bets
                players.get(i).bet(minimumBet);
            }
        }

        List<Player> roundPlayers = new ArrayList<>(players);

        roundPlayers.forEach(Player::initializeHand);

        // initial deal
        roundPlayers.forEach(player -> {
            player.hands().forEach(hand -> hand.drawCard(shoe.draw()));
        });
        dealer.drawCard(shoe.draw());
        roundPlayers.forEach(player -> {
            player.hands().forEach(hand -> hand.drawCard(shoe.draw()));
        });

        dealer.displayHand();
        roundPlayers.forEach(Player::displayHands);

        boolean somebodyWon = false;

        // determine initial black jack
        int dealerHandValue = dealer.computeHand();
        if (dealerHandValue != 10 && dealerHandValue != 11) {
            for (int i = 0; i < roundPlayers.size(); i++) {
                if (roundPlayers.get(i).hands().get(0).computeHand() == 21) {
                    roundPlayers.get(i).payout((minimumBet * 2) + (minimumBet / 2));
                    roundPlayers.remove(roundPlayers.get(i));
                    somebodyWon = true;
                }
            }
        }

        if (somebodyWon) {
            players.forEach(player -> System.out.println(player.getBudget()));
        }

        // hit, stand, surrender, double (if two cards pa lang), split (if twin)
        roundPlayers.forEach(player -> {
            player.hands().forEach(hand -> {
                Action action = hand.decide();

                if (Action.STAND.equals(action)) {
                    // move to next hand
                } else if (Action.HIT.equals(action)) {
                    hand.drawCard(shoe.draw());
                } else if (Action.SURRENDER.equals(action)) {
                    // remove hand
                } else if (Action.DOUBLE.equals(action)) {
                    // idk
                } else if (Action.SPLIT.equals(action)) {
                    // create
                } else {
                    // invalid
                }
            });
        });

        // dealer deals himself
        // decide who won/lost

        dealer.displayHand();
        roundPlayers.forEach(Player::displayHands);
    }
}
