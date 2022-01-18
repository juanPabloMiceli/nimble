package com.nimble.model.game;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Player {

	private Deck onHandsDeck;

	private Deck discardDeck;

	public Player() {
		onHandsDeck = Deck.startingDeck();
		discardDeck = new Deck();
		discard();
	}

	public Player(Player player) {
		if (player == null) {
			return;
		}
		discardDeck = player.discardDeck;
		onHandsDeck = player.onHandsDeck;
	}

	public Card peekDiscardDeck() {
		return discardDeck.peek();
	}

	public int totalCards() {
		return discardDeck.size() + onHandsDeck.size();
	}

	public void discard() {
		if (onHandsDeck.isEmpty()) {
			if (discardDeck.isEmpty()) {
				return;
			}
			// Como no tengo de donde agarrar paso el mazo del descarte a la mano
			// invirtiendo su orden:
			while (!discardDeck.isEmpty()) {
				onHandsDeck.add(discardDeck.draw());
			}
		}
		discardDeck.add(onHandsDeck.draw());
	}

	public Boolean recover() {
		if (discardDeck.size() <= 1) {
			return false;
		}
		onHandsDeck.add(discardDeck.draw());
		return true;
	}

	// Player plays his hand card
	public Boolean playHandCard(Deck deckBoard) {
		if (!deckBoard.canplay(discardDeck.peek())) {
			return false;
		}

		deckBoard.add(discardDeck.draw());
		discard();
		return true;
	}

	public Boolean hasEnded() {
		return discardDeck.isEmpty() && onHandsDeck.isEmpty();
	}

	public int getDiscardDeckSize() {
		return discardDeck.size();
	}

	public int getOnHandsDeckSize() {
		return onHandsDeck.size();
	}

	public Boolean isStuck(List<Card> centerCards) {
		return !canPlayAnyCard(onHandsDeck, centerCards) && !canPlayAnyCard(discardDeck, centerCards);
	}

	private Boolean canPlayCard(Card card, List<Card> centerCards) {
		return centerCards.stream().anyMatch(card::canBePlayedAfter);
	}

	private Boolean canPlayAnyCard(Deck deck, List<Card> centerCards) {
		boolean canPlay = false;

		Deck auxiliarDeck = new Deck();
		while (!deck.isEmpty()) {
			Card topCard = deck.draw();
			auxiliarDeck.add(topCard);
			canPlay |= canPlayCard(topCard, centerCards);
		}

		while (!auxiliarDeck.isEmpty()) {
			deck.add(auxiliarDeck.draw());
		}

		return canPlay;
	}
}
