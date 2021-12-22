package com.nimble.model;

import com.nimble.exceptions.Player.InvalidPlayerColorException;
import com.nimble.exceptions.Player.NameMustNotBeEmptyException;
import com.nimble.exceptions.deck.EmptyDeckException;
import com.nimble.utils.ColorUtils;

import java.util.EmptyStackException;

public class Player {

	private String name;
	private String color;

	private Deck onHandsDeck;
	private Deck discardDeck;
	private Card handCard;

	public Player(String name, String color) {
		setName(name);
		setColor(color);
		discardDeck = new Deck();
		onHandsDeck = Deck.startingDeck(color);
		handCard = onHandsDeck.draw();
	}

	public void setName(String name) {
		if (name.isEmpty()) {
			throw new NameMustNotBeEmptyException();
		}
		this.name = name;
	}

	public void setColor(String color) {
		if (!ColorUtils.isValidPlayerColor(color)) {
			throw new InvalidPlayerColorException(color);
		}
		this.color = color;
	}

	public String getName() {
		return name;
	}
	public String getColor() {
		return color;
	}
	public Card getHandCard() {
		return handCard;
	}

	public void draw() {
		try {
			handCard = onHandsDeck.draw();
		}
		catch(EmptyDeckException e) {
			while(discardDeck.size() > 0){
				onHandsDeck.add(discardDeck.draw());
			}
		}
	}

	//Player plays his hand card
	public void play(Deck deckBoard){
		if(!deckBoard.canplay(this.handCard)){
			// throw an error
		}

		deckBoard.add(this.handCard);
		draw();
	}

	public void discard(){
		discardDeck.add(this.handCard);
		draw();
	}

}
