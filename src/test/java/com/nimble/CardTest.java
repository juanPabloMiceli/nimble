package com.nimble;

import com.nimble.exceptions.card.InnerColorSameAsOuterColorException;
import com.nimble.exceptions.card.InvalidInnerCardColorException;
import com.nimble.exceptions.card.InvalidOuterCardColorException;
import com.nimble.model.enums.ValidCardColors;
import com.nimble.model.game.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * {@link Card}
 */

@SpringBootTest
public class CardTest {

	@Test
	public void Test01_InnerColorCanNotBeInvalid() {
		Assertions.assertThrows(
			InvalidInnerCardColorException.class,
			() -> new Card("invalid color", ValidCardColors.BLUE.name())
		);
	}

	@Test
	public void Test02_OuterColorCanNotBeInvalid() {
		Assertions.assertThrows(
			InvalidOuterCardColorException.class,
			() -> new Card(ValidCardColors.BLUE.name(), "invalid color")
		);
	}

	@Test
	public void Test03_InnerColorEqualsOuterColorIsAnInvalidCard() {
		Assertions.assertThrows(
			InnerColorSameAsOuterColorException.class,
			() -> new Card(ValidCardColors.BLUE.name(), ValidCardColors.BLUE.name())
		);
	}

	@Test
	public void Test04_CardComparesTrueWhenItsOuterColorMatchesAnotherCardInnerColor() {
		Card card1 = new Card(ValidCardColors.BLUE.name(), ValidCardColors.ORANGE.name());
		Card card2 = new Card(ValidCardColors.ORANGE.name(), ValidCardColors.GREEN.name());
		Assertions.assertTrue(card1.canBePlayedAfter(card2));
	}

	@Test
	public void Test05_CardComparesFalseWhenItsOuterColorDoesNotMatchAnotherCardInnerColor() {
		Card card1 = new Card(ValidCardColors.BLUE.name(), ValidCardColors.ORANGE.name());
		Card card2 = new Card(ValidCardColors.ORANGE.name(), ValidCardColors.GREEN.name());
		Assertions.assertFalse(card2.canBePlayedAfter(card1));
	}
}
