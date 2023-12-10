package com.demo;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final List<Integer> POTENTIAL_BLACK_JACK_VALUES = List.of(10, 11);
    private static final Integer BLACK_JACK_VALUE = 21;
    private static final Integer DEALER_MINIMUM_HAND_VALUE = 17;

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

    public void deal() {
        if (!hasPlayers()) {
            return;
        }

        // Note: Initialize shoe
        // Note: Initialize dealer and players hands
        // Note: Take initial bet
        shoe = new Shoe(deckCount);
        List<Player> roundPlayers = new ArrayList<>(players);
        roundPlayers.forEach(Player::initializeHand);
        dealer.initializeHand();
        players.forEach(player -> player.bet(minimumBet));

        // Note: Initial deal
        roundPlayers.forEach(player ->
            player.getHands().forEach(hand -> hand.addCard(shoe.draw())));
        dealer.getHand().addCard(shoe.draw());
        roundPlayers.forEach(player ->
            player.getHands().forEach(hand -> hand.addCard(shoe.draw())));

        // Note: Determine initial black jack win
        int initialDealerHandValue = dealer.getHand().computeHand();
        if (!POTENTIAL_BLACK_JACK_VALUES.contains(initialDealerHandValue)) {
            roundPlayers.forEach(player ->
                player.getHands().forEach(hand -> {
                    if (hand.computeHand() == BLACK_JACK_VALUE) {
                        player.payout((minimumBet * 2) + (minimumBet / 2));
                        hand.setWon(true);
                    }
                }));

            // Note: Remove hands that won already
            roundPlayers.forEach(player ->
                    player.getHands().removeIf(Hand::getWon));

            // Note: Remove players where all hands won already
            roundPlayers.removeIf(player -> player.getHands().isEmpty());
        }

        // TODO: Decide action per hand (
        //  stand
        //  hit
        //  surrender
        //  double (allowed if two cards pa lang)
        //  split (allowed if pair)

        // Note: Dealer deals himself until > 16 or bust
        while (dealer.getHand().computeHand() >= DEALER_MINIMUM_HAND_VALUE) {
            dealer.drawCard(shoe.draw());
        }

        // Note: Payout
        int finalDealerHandValue = dealer.getHand().computeHand();
        boolean dealerHandBust = finalDealerHandValue > BLACK_JACK_VALUE;
        if (dealerHandBust) {
            // Note: If dealer is bust, payout each player hand that is not bust
            roundPlayers.forEach(player -> player.getHands().forEach(hand -> {
                boolean playerHandBust = hand.computeHand() <= BLACK_JACK_VALUE;
                if (playerHandBust) {
                    player.payout(minimumBet * 2);
                }
            }));
        } else {
            // Note: If dealer is not bust, payout each player hand that beats the dealer hand
            roundPlayers.forEach(player -> player.getHands().forEach(hand -> {
                int finalPlayerHandValue = hand.computeHand();
                if (finalPlayerHandValue > finalDealerHandValue) {
                    player.payout(minimumBet * 2);
                } else if (finalPlayerHandValue == finalDealerHandValue) {
                    player.payout(minimumBet);
                }
            }));
        }

        // Note: Remove players that can't afford minimumBet anymore
        players.removeIf(player -> player.getBudget() < minimumBet);

    }

    public boolean hasPlayers() {
        return !players.isEmpty();
    }

    public static void main(String[] args) {
        Player player1 = new Player(20_000);
        Player player2 = new Player(20_000);
        Player player3 = new Player(20_000);
        Game game = new Game(List.of(player1, player2, player3), 1000, 6);
        int dealCounter = 0;
        while (game.hasPlayers()) {
            dealCounter++;
            game.deal();
        };
        System.out.println("Ended in " + dealCounter + " deals. ");
    }
}
