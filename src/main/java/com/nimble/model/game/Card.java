package com.nimble.model.game;

import com.nimble.exceptions.card.InnerColorSameAsOuterColorException;
import com.nimble.exceptions.card.InvalidInnerCardColorException;
import com.nimble.exceptions.card.InvalidOuterCardColorException;
import com.nimble.model.enums.ValidCardColors;
import com.nimble.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Card {

	private String innerColor;

	private String outerColor;

	public Card(@NotNull String innerColor, @NotNull String outerColor) {
		if (innerColor.equals(outerColor)) {
			throw new InnerColorSameAsOuterColorException(innerColor, outerColor);
		}
		setInnerColor(innerColor);
		setOuterColor(outerColor);
	}

	public Card(Card card) {
		if (card == null) {
			return;
		}
		innerColor = card.innerColor;
		outerColor = card.outerColor;
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

	public String getInnerColor() {
		return innerColor;
	}

	public String getOuterColor() {
		return outerColor;
	}

	public boolean canBePlayedAfter(Card anotherCard) {
		return this.outerColor.equals(anotherCard.innerColor);
	}

	public static Card random() {
		List<ValidCardColors> validColors = Arrays.asList(ValidCardColors.values());
		Collections.shuffle(validColors);
		return new Card(validColors.get(0).name(), validColors.get(1).name());
	}

}
