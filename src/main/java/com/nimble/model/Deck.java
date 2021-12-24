package com.nimble.model;

import com.nimble.exceptions.deck.EmptyDeckException;
import com.nimble.model.enums.ValidCardColors;

import java.util.*;

public class Deck {

	private final Stack<Card> cards;

	public Deck() {
		cards = new Stack<>();
	}

	public Deck(Collection<Card> cards) {
		this.cards = new Stack<>();
		this.cards.addAll(cards);
	}

	public static Deck startingDeck() {

		ArrayList<Card> cards = new ArrayList<>();

		for (ValidCardColors innerColor : ValidCardColors.values()) {
			for (ValidCardColors outerColor : ValidCardColors.values()) {
				if (innerColor.equals(outerColor))
					continue;
				cards.add(new Card(innerColor.name(), outerColor.name()));
			}
		}

		Collections.shuffle(cards);
		return new Deck(cards);
	}

	public int size() {
		return cards.size();
	}

	public Boolean isEmpty() {
		return cards.size() == 0;
	}

	public void add(Card card) {
		cards.push(card);
	}

	public Card draw() {
		if (cards.isEmpty()) {
			throw new EmptyDeckException();
		}
		return cards.pop();
	}

	public Card peek() {
		if (cards.isEmpty()) {
			throw new EmptyDeckException();
		}
		return cards.peek();
	}

	// View if card can be played in this deck
	public boolean canplay(Card card) {
		if (cards.isEmpty()) {
			throw new RuntimeException("wut");
		}
		Card current_card = cards.peek();
		return current_card.compare(card);
	}

}
