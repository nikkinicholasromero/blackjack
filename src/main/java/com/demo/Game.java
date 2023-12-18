package com.demo;

import java.util.ArrayList;
import java.util.List;

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

    public boolean hasPlayers() {
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
        dealer.hand().addCard(shoe.draw());
        roundPlayers.forEach(player -> player.hands().forEach(hand -> hand.addCard(shoe.draw())));
    }

    private void handleInitialBlackJacks(Dealer dealer, List<Player> roundPlayers) {
        int dealerHandValue = dealer.hand().computeValue();
        boolean dealerCanBlackJack = List.of(10, 11).contains(dealerHandValue);
        if (dealerCanBlackJack) {
            return;
        }

        roundPlayers.forEach(player ->
            player.hands().forEach(hand -> {
                if (21 == hand.computeValue()) {
                    player.payout((minimumBet * 2) + (minimumBet / 2));
                    hand.setState(HandState.WON);
                }
            })
        );

        roundPlayers.forEach(player -> player.hands().removeIf(hand -> HandState.WON.equals(hand.getState())));
        roundPlayers.removeIf(player -> player.hands().isEmpty());
    }

    private void handlePlayerActions(Shoe shoe, Dealer dealer, List<Player> roundPlayers) {
        roundPlayers.forEach(player -> {
            List<Hand> hands = new ArrayList<>(player.hands());
            for (Hand hand : hands) {
                handleHandAction(shoe, dealer, player, hand);
            }
        });
        roundPlayers.forEach(player -> player.hands().removeIf(hand -> HandState.BUST.equals(hand.getState())));
        roundPlayers.forEach(player -> player.hands().removeIf(hand -> HandState.SURRENDERED.equals(hand.getState())));
        roundPlayers.forEach(player -> player.hands().removeIf(hand -> HandState.SPLIT.equals(hand.getState())));
        roundPlayers.removeIf(player -> player.hands().isEmpty());
    }

    private void handleHandAction(Shoe shoe, Dealer dealer, Player player, Hand hand) {
        PlayerAction playerAction;

        do {
            playerAction = player.decide(dealer.hand(), hand, minimumBet);
            switch (playerAction) {
                case STAND:
                    hand.setState(HandState.STOOD);
                    break;
                case SURRENDER:
                    player.payout(minimumBet / 2);
                    hand.setState(HandState.SURRENDERED);
                    break;
                case DOUBLE:
                    player.bet(minimumBet);
                    hand.addCard(shoe.draw());
                    hand.setState(HandState.DOUBLED);
                    break;
                case HIT:
                    hand.addCard(shoe.draw());
                    break;
                case SPLIT:
                    handleSplit(shoe, dealer, player, hand);
                    break;
            }
        } while (PlayerAction.HIT.equals(playerAction) && !HandState.BUST.equals(hand.getState()));
    }

    private void handleSplit(Shoe shoe, Dealer dealer, Player player, Hand hand) {
        List<Hand> newHands = hand.split();
        newHands.get(0).addCard(shoe.draw());
        newHands.get(1).addCard(shoe.draw());
        player.bet(minimumBet);
        player.hands().addAll(newHands);
        handleSplitInitialBlackJacks(dealer, player, newHands);

        newHands.forEach(newHand -> handleHandAction(shoe, dealer, player, newHand));
    }

    private void handleSplitInitialBlackJacks(Dealer dealer, Player player, List<Hand> newHands) {
        int dealerHandValue = dealer.hand().computeValue();
        boolean dealerCanBlackJack = List.of(10, 11).contains(dealerHandValue);
        if (dealerCanBlackJack) {
            return;
        }

        newHands.forEach(hand -> {
            if (21 == hand.computeValue()) {
                player.payout((minimumBet * 2) + (minimumBet / 2));
                hand.setState(HandState.WON);
            }
        });

        player.hands().removeIf(hand -> HandState.WON.equals(hand.getState()));
    }

    private void handleDealerSelfDeal(Shoe shoe, Dealer dealer) {
        while (!HandState.BUST.equals(dealer.hand().getState()) && dealer.hand().computeValue() <= 17) {
            dealer.hand().addCard(shoe.draw());
        }
    }

    private void handlePayout(Dealer dealer, List<Player> roundPlayers) {
        if (HandState.BUST.equals(dealer.hand().getState())) {
            roundPlayers.forEach(player -> player.hands().forEach(hand -> {
                int multiplier = HandState.DOUBLED.equals(hand.getState()) ? 2 : 1;
                player.payout((minimumBet * 2) * multiplier);
            }));

            return;
        }

        int dealerHandValue = dealer.hand().computeValue();

        roundPlayers.forEach(player -> player.hands().forEach(hand -> {
            int multiplier = HandState.DOUBLED.equals(hand.getState()) ? 2 : 1;
            int playerHandValue = hand.computeValue();
            if (playerHandValue > dealerHandValue) {
                player.payout((minimumBet * 2) * multiplier);
            } else if (playerHandValue == dealerHandValue) {
                player.payout((minimumBet) * multiplier);
            }
        }));
    }
}
