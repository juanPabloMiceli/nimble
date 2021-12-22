package com.nimble.model;

import com.nimble.exceptions.card.InnerColorSameAsOuterColorException;
import com.nimble.exceptions.card.InvalidBackCardColorException;
import com.nimble.exceptions.card.InvalidInnerCardColorException;
import com.nimble.exceptions.card.InvalidOuterCardColorException;
import com.nimble.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;


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

	private void setInnerColor(@NotNull String innerColor) {
		if (!ColorUtils.isValidCardColor(innerColor)) {
			throw new InvalidInnerCardColorException(innerColor);
		}
		this.innerColor = innerColor;
	}

	private void setOuterColor(@NotNull String outerColor) {
		if (!ColorUtils.isValidCardColor(outerColor)) {
			throw new InvalidOuterCardColorException(outerColor);
		}
		this.outerColor = outerColor;
	}

	private void setBackColor(String backColor) {
		if (!ColorUtils.isValidPlayerColor(backColor)) {
			throw new InvalidBackCardColorException(backColor);
		}
		this.backColor = backColor;
	}

	public String getInnerColor() {
		return innerColor;
	}
	public String getOuterColor() {
		return outerColor;
	}
	public String getBackColor() {
		return backColor;
	}

	public boolean compare(Card anotherCard) {
		return  this.outerColor == anotherCard.getInnerColor();
	}
}
