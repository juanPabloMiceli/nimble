package com.nimble.model;

import com.nimble.exceptions.deck.EmptyDeckException;
import com.nimble.model.enums.ValidCardColors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

public class Deck {

	private final Stack<Card> cards;

	public Deck() {
		cards = new Stack<>();
	}

	public Deck(Collection<Card> cards) {
		this.cards = new Stack<>();
		this.cards.addAll(cards);
	}

	public static Deck startingDeck(String backColor) {

		ArrayList<Card> cards = new ArrayList<>();

		for (ValidCardColors innerColor : ValidCardColors.values()) {
			for (ValidCardColors outerColor : ValidCardColors.values()) {
				if (innerColor.equals(outerColor))
					continue;
				cards.add(new Card(innerColor.name(), outerColor.name(), backColor));
			}
		}

		Collections.shuffle(cards);

		return new Deck(cards);

	}

	public Card peek() {
		assertDeckIsNotEmpty();
		return cards.peek();
	}


	public Card draw() {
		Card card = peek();
		cards.pop();
		return card;
	}

	public Stack<Card> getCards() {
		return cards;
	}

	public int size() {
		return getCards().size();
	}

	public void add(Card card) {
		cards.push(card);
	}

	private void assertDeckIsNotEmpty() {
		if (cards.size() <= 0) {
			throw new EmptyDeckException();
		}
	}

}
