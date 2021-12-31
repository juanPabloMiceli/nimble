package com.nimble.model.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Player {

	private Deck onHandsDeck;

	private Deck discardDeck;

	private Card handCard;

	public Player() {
		discardDeck = new Deck();
		onHandsDeck = Deck.startingDeck();
		handCard = draw();
	}

	public Player(Player player) {
		if (player == null) {
			return;
		}
		discardDeck = player.discardDeck;
		onHandsDeck = player.onHandsDeck;
		handCard = player.handCard;
	}

	public Card getHandCard() {
		return handCard;
	}

	public Card peekDiscardDeck() {
		return discardDeck.peek();
	}

	public int totalCards() {
		return discardDeck.size() + onHandsDeck.size() + 1;
	}

	public void discard() {
		discardDeck.add(handCard);
		handCard = draw();
	}

	private Card draw() {
		if (onHandsDeck.isEmpty()) {
			if (discardDeck.isEmpty()) {
				return null;
			}
			// Como no tengo de donde agarrar paso el mazo del descarte a la mano
			// invirtiendo su orden:
			while (!discardDeck.isEmpty()) {
				onHandsDeck.add(discardDeck.draw());
			}

		}
		// Saco la primer carta del mazo y la devuelvo
		return onHandsDeck.draw();
	}

	// Player plays his hand card
	public Boolean playHandCard(Deck deckBoard) {
		if (!deckBoard.canplay(this.handCard)) {
			return false;
		}

		deckBoard.add(this.handCard);
		handCard = draw();
		return true;
	}

	public Boolean playDiscardCard(Deck deckBoard) {
		if (!deckBoard.canplay(discardDeck.peek())) {
			return false;
		}

		deckBoard.add(discardDeck.draw());
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

}
