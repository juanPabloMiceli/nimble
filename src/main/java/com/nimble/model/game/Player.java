package com.nimble.model.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Player {

	private Deck onHandsDeck;

	private Deck discardDeck;

	private Card handCard;

	public Player() {
		// TODO: Tendria sentido pasar estos parametros desde el constructor?
		discardDeck = new Deck();
		onHandsDeck = Deck.startingDeck();
		handCard = null;
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

	public Card getDiscardTop() {
		return discardDeck.peek();
	}

	public int getTotalCards() {
		return discardDeck.size() + onHandsDeck.size() + (hasCardOnHand() ? 1 : 0);
	}

	public void draw() {
		// TODO: Repensar para discard

		if (onHandsDeck.isEmpty()) {
			if (hasCardOnHand()) {
				// Hay que pasar la carta de la mano al descarte y nada mas
				discardDeck.add(handCard);
				handCard = null;

			}
			else {
				// Hay que pasar el pilon de descarte a la mano
				while (!discardDeck.isEmpty()) {
					onHandsDeck.add(discardDeck.draw());
				}
			}
		}
		else {
			if (hasCardOnHand()) {
				// Si tengo una carta en la mano la tengo que pasar al mazo de descarte
				discardDeck.add(handCard);
				handCard = null;
			}
			// Como el mazo de la mano tiene cartas agarro una y la paso a la mano
			handCard = onHandsDeck.draw();
		}
	}

	// Player plays his hand card
	public Boolean playHandCard(Deck deckBoard) {
		if (!deckBoard.canplay(this.handCard)) {
			return false;
		}

		deckBoard.add(this.handCard);
		this.handCard = null;
		draw();
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
		return !hasCardOnHand() && discardDeck.isEmpty() && onHandsDeck.isEmpty();
	}

	public int getDiscardDeckSize() {
		return discardDeck.size();
	}

	public int getOnHandsDeckSize() {
		return onHandsDeck.size();
	}

	public Boolean hasCardOnHand() {
		return handCard != null;
	}

}
