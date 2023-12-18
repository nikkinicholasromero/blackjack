package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {
    private final List<Player> players;
    private final int minimumBet;
    private final int deckCount;

    public Game(
            List<Player> players,
            int minimumBet,
            int deckCount) {
        this.players = new ArrayList<>(players);
        this.minimumBet = minimumBet;
        this.deckCount = deckCount;
    }

    private boolean hasPlayers() {
        players.removeIf(player -> player.budget() < minimumBet);
        return !players.isEmpty();
    }

    public void deal() {
        if (!hasPlayers()) {
            return;
        }

        // Initialize deal
        Shoe shoe = new Shoe(deckCount);
        Dealer dealer = new Dealer();
        List<Player> roundPlayers = new ArrayList<>(players);
        roundPlayers.forEach(Player::initializeHand);
        roundPlayers.forEach(player -> player.bet(minimumBet));

        // Initial draw
        roundPlayers.forEach(player -> player.hands().forEach(hand -> hand.addCard(shoe.draw())));
        Card dealerUpCard = shoe.draw();
        dealer.hand().addCard(dealerUpCard);
        roundPlayers.forEach(player -> player.hands().forEach(hand -> hand.addCard(shoe.draw())));

        // Initial blackjack
        boolean dealerCanBlackJack = dealerUpCard.rank().equals(Rank.ACE) ||
            Objects.equals(dealerUpCard.rank().value(), 10);
        if (!dealerCanBlackJack) {
            roundPlayers.forEach(player ->
                player.hands().forEach(hand -> {
                    if (Objects.equals(hand.computeValue(), 21)) {
                        player.payout((minimumBet * 2) + (minimumBet / 2));
                        hand.setWon();
                    }
                }));

            roundPlayers.forEach(player -> player.hands().removeIf(Hand::won));
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
        roundPlayers.forEach(player -> player.hands().removeIf(Hand::surrendered));
        roundPlayers.forEach(player -> player.hands().removeIf(Hand::bust));
        roundPlayers.removeIf(player -> player.hands().isEmpty());

        // Dealer deals self until bust or <= 17
        while (!dealer.hand().bust() && dealer.hand().computeValue() <= 17) {
            dealer.hand().addCard(shoe.draw());
        }

        // Payout
        if (dealer.hand().bust()) {
            roundPlayers.forEach(player -> player.hands().forEach(hand -> {
                int multiplier = hand.doubled() ? 2 : 1;
                player.payout((minimumBet * 2) * multiplier);
            }));
        } else {
            int dealerHandValue = dealer.hand().computeValue();

            roundPlayers.forEach(player -> player.hands().forEach(hand -> {
                int multiplier = hand.doubled() ? 2 : 1;
                int playerHandValue = hand.computeValue();
                if (playerHandValue > dealerHandValue) {
                    player.payout((minimumBet * 2) * multiplier);
                } else if (playerHandValue == dealerHandValue) {
                    player.payout((minimumBet) * multiplier);
                }
            }));
        }

        players.removeIf(player -> player.budget() < minimumBet);
    }

    public void out(Player player) {
        players.remove(player);
    }

    public static void main(String[] args) {
        int count = 0;
        for (long i = 0; i < 10000; i++) {
            count += counter();
        }
        System.out.println(count + "/" + 30000);
    }

    private static int counter() {
        boolean player1out = false;
        boolean player2out = false;
        boolean player3out = false;
        Player player1 = new Player("Erwin", 20_000, new BasicStrategy());
        Player player2 = new Player("RJ", 20_000, new BasicStrategy());
        Player player3 = new Player("Nikki", 20_000, new BasicStrategy());
        Game game = new Game(List.of(player1, player2, player3), 1000, 6);
        int dealCounter = 0;
        while (game.hasPlayers()) {
            dealCounter++;
            game.deal();
            if (!player1out && player1.budget() >= 25_000) {
                game.out(player1);
                player1out = true;
            }
            if (!player2out && player2.budget() >= 25_000) {
                game.out(player2);
                player2out = true;
            }
            if (!player3out && player3.budget() >= 25_000) {
                game.out(player3);
                player3out = true;
            }
        };
        int count = 0;
        if (player1out) {
            count++;
        }
        if (player2out) {
            count++;
        }
        if (player3out) {
            count++;
        }
        return count;
//        System.out.println("Ended in " + dealCounter + " deals. ");
//        System.out.println("Player1: " + player1.budget());
//        System.out.println("Player2: " + player2.budget());
//        System.out.println("Player3: " + player3.budget());
    }
}
