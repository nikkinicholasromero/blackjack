package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {
    private final int deckCount;
    private final int minimumBet;
    private final List<Player> players;

    public Game(
            int deckCount,
            int minimumBet,
            List<Player> players) {
        this.deckCount = deckCount;
        this.minimumBet = minimumBet;
        this.players = new ArrayList<>(players);
    }

    private boolean hasPlayers() {
        return !players.isEmpty();
    }

    public void deal() {
        Shoe shoe = new Shoe(deckCount);
        Dealer dealer = new Dealer();
        List<Player> roundPlayers = new ArrayList<>(players);
        roundPlayers.forEach(Player::initializeHand);
        roundPlayers.forEach(player -> player.bet(minimumBet));

        handleInitialDraws(shoe, dealer, roundPlayers);
        handleInitialBlackJacks(dealer, roundPlayers);
        handlePlayerActions(shoe, dealer, roundPlayers);
        handleDealerSelfDeal(shoe, dealer);
        handlePayout(dealer, roundPlayers);

        players.removeIf(player -> player.budget() < minimumBet);
    }

    private void handleInitialDraws(Shoe shoe, Dealer dealer, List<Player> roundPlayers) {
        roundPlayers.forEach(player -> player.hands().forEach(hand -> hand.addCard(shoe.draw())));
        Card dealerUpCard = shoe.draw();
        dealer.hand().addCard(dealerUpCard);
        roundPlayers.forEach(player -> player.hands().forEach(hand -> hand.addCard(shoe.draw())));
    }

    private void handleInitialBlackJacks(Dealer dealer, List<Player> roundPlayers) {
        boolean dealerCanBlackJack =
                Objects.equals(dealer.hand().computeValue(), 10) ||
                Objects.equals(dealer.hand().computeValue(), 11);
        if (dealerCanBlackJack) {
            return;
        }

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

    private void handlePlayerActions(Shoe shoe, Dealer dealer, List<Player> roundPlayers) {
        // TODO: Decide action per player hand
        for (Player player : roundPlayers) {
            for (Hand hand : player.hands()) {
                PlayerAction playerAction = player.decide(dealer.hand(), hand, minimumBet);
                if (PlayerAction.STAND.equals(playerAction)) {
                    hand.setStood();
                    break;
                }
                if (PlayerAction.SURRENDER.equals(playerAction)) {
                    player.payout(minimumBet / 2);
                    hand.setSurrendered();
                    break;
                }
                if (PlayerAction.DOUBLE.equals(playerAction)) {
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
    }

    private void handleDealerSelfDeal(Shoe shoe, Dealer dealer) {
        while (!dealer.hand().bust() && dealer.hand().computeValue() <= 17) {
            dealer.hand().addCard(shoe.draw());
        }
    }

    private void handlePayout(Dealer dealer, List<Player> roundPlayers) {
        if (dealer.hand().bust()) {
            roundPlayers.forEach(player -> player.hands().forEach(hand -> {
                int multiplier = hand.doubled() ? 2 : 1;
                player.payout((minimumBet * 2) * multiplier);
            }));

            return;
        }

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
