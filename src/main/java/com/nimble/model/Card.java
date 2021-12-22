package com.nimble.model;

import com.nimble.exceptions.card.InnerColorSameAsOuterColorException;
import com.nimble.exceptions.card.InvalidBackCardColorException;
import com.nimble.exceptions.card.InvalidInnerCardColorException;
import com.nimble.exceptions.card.InvalidOuterCardColorException;
import com.nimble.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Card {

	private String innerColor;

	private String outerColor;

	private String backColor;



	public Card(@NotNull String innerColor, @NotNull String outerColor, @NotNull String backColor) {
		if (innerColor.equals(outerColor)) {
			throw new InnerColorSameAsOuterColorException(innerColor, outerColor);
		}
		setInnerColor(innerColor);
		setOuterColor(outerColor);
		setBackColor(backColor);
	}

	public Card(Card card) {
		this(card.innerColor, card.outerColor, card.backColor);
	}

	public String getInnerColor() {
		return innerColor;
	}

	private void setInnerColor(@NotNull String innerColor) {
		if (!ColorUtils.isValidCardColor(innerColor)) {
			throw new InvalidInnerCardColorException(innerColor);
		}
		this.innerColor = innerColor;
	}

	public String getOuterColor() {
		return outerColor;
	}

	private void setOuterColor(@NotNull String outerColor) {
		if (!ColorUtils.isValidCardColor(outerColor)) {
			throw new InvalidOuterCardColorException(outerColor);
		}
		this.outerColor = outerColor;
	}

	public String getBackColor() {
		return backColor;
	}

	private void setBackColor(String backColor) {
		if (!ColorUtils.isValidPlayerColor(backColor)) {
			throw new InvalidBackCardColorException(backColor);
		}
		this.backColor = backColor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Card))
			return false;
		Card card = (Card) o;
		return getInnerColor().equals(card.getInnerColor()) && getOuterColor().equals(card.getOuterColor())
				&& getBackColor().equals(card.getBackColor());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getInnerColor(), getOuterColor(), getBackColor());
	}

	public boolean tops(Card anotherCard) {
		return this.outerColor.equals(anotherCard.getInnerColor());
	}

}
