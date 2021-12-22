package com.nimble.model;

import com.nimble.exceptions.Player.InvalidPlayerColorException;
import com.nimble.exceptions.Player.NameMustNotBeEmptyException;
import com.nimble.utils.ColorUtils;

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
		handCard = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name.isEmpty()) {
			throw new NameMustNotBeEmptyException();
		}
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		if (!ColorUtils.isValidPlayerColor(color)) {
			throw new InvalidPlayerColorException(color);
		}
		this.color = color;
	}

	public Deck getOnHandsDeck() {
		return onHandsDeck;
	}

	public Deck getDiscardDeck() {
		return discardDeck;
	}

	public Card getHandCard() {
		return handCard;
	}

	public void draw() {
		if(onHandsDeck.size() > 0){
			handCard = onHandsDeck.draw();
		}else{
			if(handCard == null){
				while(discardDeck.size() > 0){
					onHandsDeck.add(discardDeck.draw());
				}
			}
		}


	}

}
