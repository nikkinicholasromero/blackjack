package com.demo;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final List<Integer> POTENTIAL_BLACK_JACK_VALUES = List.of(10, 11);

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
            player.hands().forEach(hand -> hand.addCard(shoe.draw())));
        Card dealerUpCard = shoe.draw();
        dealer.hand().addCard(dealerUpCard);
        roundPlayers.forEach(player ->
            player.hands().forEach(hand -> hand.addCard(shoe.draw())));

        // Note: Determine initial black jack win
        int initialDealerHandValue = dealer.hand().computeValue();
        if (!POTENTIAL_BLACK_JACK_VALUES.contains(initialDealerHandValue)) {
            roundPlayers.forEach(player ->
                player.hands().forEach(hand -> {
                    if (hand.computeValue() == 21) {
                        player.payout((minimumBet * 2) + (minimumBet / 2));
                        hand.setWon();
                    }
                }));

            // Note: Remove hands that won already
            roundPlayers.forEach(player ->
                    player.hands().removeIf(Hand::won));

            // Note: Remove players where all hands won already
            roundPlayers.removeIf(player -> player.hands().isEmpty());
        }

        // TODO: Decide action per player hand
        for (Player player : roundPlayers) {
            for (Hand hand : player.hands()) {
                Action action = player.decide(dealerUpCard, hand, minimumBet);
                if (Action.STAND.equals(action)) {
                    hand.setStood();
                    break;
                }
                if (Action.SURRENDER.equals(action)) {
                   player.payout(minimumBet / 2);
                   hand.setSurrendered();
                   break;
                }
                if (Action.DOUBLE.equals(action)) {
                    player.bet(minimumBet);
                    hand.addCard(shoe.draw());
                    hand.setDoubled();
                    break;
                }
                // TODO: HIT & SPLIT
            }
        }

        // Note: Remove hands that surrendered
        roundPlayers.forEach(player ->
                player.hands().removeIf(Hand::surrendered));

        // Note: Remove hands that bust
        roundPlayers.forEach(player ->
                player.hands().removeIf(Hand::bust));

        // Note: Remove players where all hands bust already
        roundPlayers.removeIf(player -> player.hands().isEmpty());

        // Note: Dealer deals himself until > 16 or bust
        while (dealer.hand().computeValue() >= 17) {
            dealer.hand().addCard(shoe.draw());
        }

        // Note: Payout
        if (dealer.hand().bust()) {
            // Note: If dealer is bust, payout each player hand
            roundPlayers.forEach(player -> player.hands().forEach(hand -> {
                int multiplier = hand.doubled() ? 2 : 1;
                player.payout((minimumBet * 2) * multiplier);
            }));
        } else {
            // Note: If dealer is not bust, payout each player hand that beats the dealer hand
            int finalDealerHandValue = dealer.hand().computeValue();

            roundPlayers.forEach(player -> player.hands().forEach(hand -> {
                int multiplier = hand.doubled() ? 2 : 1;
                int finalPlayerHandValue = hand.computeValue();
                if (finalPlayerHandValue > finalDealerHandValue) {
                    player.payout((minimumBet * 2) * multiplier);
                } else if (finalPlayerHandValue == finalDealerHandValue) {
                    player.payout((minimumBet) * multiplier);
                }
            }));
        }

        // Note: Remove players that can't afford minimumBet anymore
        players.removeIf(player -> player.budget() < minimumBet);
    }

    public boolean hasPlayers() {
        return !players.isEmpty();
    }

    public static void main(String[] args) {
        Player player1 = new Player(20_000, new BasicStrategy());
        Player player2 = new Player(20_000, new BasicStrategy());
        Player player3 = new Player(20_000, new BasicStrategy());
        Game game = new Game(List.of(player1, player2, player3), 1000, 6);
        int dealCounter = 0;
        while (game.hasPlayers()) {
            dealCounter++;
            game.deal();
        };
        System.out.println("Ended in " + dealCounter + " deals. ");
    }
}
