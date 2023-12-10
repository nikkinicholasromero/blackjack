package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {
    public static void main(String[] args) {
        Player player1 = new Player("RJ", 20_000);
        Player player2 = new Player("Erwin", 20_000);
        Player player3 = new Player("Nikki", 20_000);
        Game game = new Game(List.of(player1, player2, player3), 1000, 6);
        int dealCounter = 0;
        while (game.hasPlayers()) {
            dealCounter++;
            game.startGame();
        };
        System.out.println("Ended in " + dealCounter + " deals. ");
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
        // Note: Initialize shoe and dealer/player hands
        shoe = new Shoe(deckCount);
        List<Player> roundPlayers = new ArrayList<>(players);
        roundPlayers.forEach(Player::initializeHand);
        dealer.initializeHand();

        // Note: Initial deal
        roundPlayers.forEach(player ->
            player.hands().forEach(hand -> hand.drawCard(shoe.draw())));
        dealer.drawCard(shoe.draw());
        roundPlayers.forEach(player ->
            player.hands().forEach(hand -> hand.drawCard(shoe.draw())));

        // Note: Determine initial black jack win
        int dealerHandValue = dealer.computeHand();
        if (dealerHandValue != 10 && dealerHandValue != 11) {
            roundPlayers.forEach(player ->
                player.hands().forEach(hand -> {
                    if (hand.computeHand() == 21) {
                        player.payout((minimumBet * 2) + (minimumBet / 2));
                        hand.setWon(true);
                    }
                }));

            // Note: Remove hands that won already
            roundPlayers.forEach(player ->
                    player.hands().removeIf(Hand::getWon));

            // Note: Remove players where all hands won already
            roundPlayers.removeIf(player -> player.hands().isEmpty());
        }

        // TODO: Decide action per hand (
        //  stand
        //  hit
        //  surrender
        //  double (allowed if two cards pa lang)
        //  split (allowed if pair)

        // Note: Dealer deals himself until > 16 or bust
        while (dealer.computeHand() > 16) {
            dealer.drawCard(shoe.draw());
        }
        // TODO: Decide who won/lost (payout)

        // Note: Remove players that can't afford minimumBet anymore
        players.removeIf(player -> player.getBudget() < minimumBet);
        players.forEach(player -> player.bet(minimumBet));
    }

    public boolean hasPlayers() {
        return !players.isEmpty();
    }
}
