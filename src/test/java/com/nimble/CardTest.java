package com.nimble;

import com.nimble.exceptions.card.InnerColorSameAsOuterColorException;
import com.nimble.exceptions.card.InvalidInnerCardColorException;
import com.nimble.exceptions.card.InvalidOuterCardColorException;
import com.nimble.model.Card;
import com.nimble.model.enums.ValidPlayerColors;
import com.nimble.model.enums.ValidCardColors;
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
		Assertions.assertThrows(InvalidInnerCardColorException.class,
				() -> new Card("invalid color", ValidCardColors.BLUE.name(), ValidPlayerColors.ORANGE.name()));
	}

	@Test
	public void Test02_OuterColorCanNotBeInvalid() {
		Assertions.assertThrows(InvalidOuterCardColorException.class,
				() -> new Card(ValidCardColors.BLUE.name(), "invalid color", ValidPlayerColors.ORANGE.name()));
	}

	@Test
	public void Test03_InnerColorEqualsOuterColorIsAnInvalidCard() {
		Assertions.assertThrows(InnerColorSameAsOuterColorException.class,
				() -> new Card(ValidCardColors.BLUE.name(), ValidCardColors.BLUE.name(),
						ValidPlayerColors.ORANGE.name()));
	}

	@Test
	public void Test04_BackColorCanNotBeInvalid() {
		Assertions.assertThrows(InnerColorSameAsOuterColorException.class,
				() -> new Card(ValidCardColors.BLUE.name(), ValidCardColors.BLUE.name(), "invalid color"));
	}

	@Test
	public void Test05_CardsWithSameFrontAndBackColorsAreConsiderEquals() {
		Card card1 = new Card(ValidCardColors.BLUE.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.GREEN.name());
		Card card2 = new Card(ValidCardColors.BLUE.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.GREEN.name());
		Assertions.assertEquals(card1, card2);
	}

	@Test
	public void Test06_CardsWithSameFrontAndDifferentBackColorsAreNotConsiderEquals() {
		Card card1 = new Card(ValidCardColors.BLUE.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.GREEN.name());
		Card card2 = new Card(ValidCardColors.BLUE.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.RED.name());
		Assertions.assertNotEquals(card1, card2);
	}

	@Test
	public void Test07_CardWhoseOuterColorMatchesAnotherCardInnerColorTopsIt() {
		Card card1 = new Card(ValidCardColors.BLUE.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.GREEN.name());
		Card card2 = new Card(ValidCardColors.ORANGE.name(), ValidCardColors.GREEN.name(),
				ValidPlayerColors.RED.name());
		Assertions.assertTrue(card1.tops(card2));
	}

	@Test
	public void Test08_CardWhoseOuterColorDoesNotMatchAnotherCardInnerColorDoesNotTopIt() {
		Card card1 = new Card(ValidCardColors.BLUE.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.GREEN.name());
		Card card2 = new Card(ValidCardColors.ORANGE.name(), ValidCardColors.GREEN.name(),
				ValidPlayerColors.RED.name());
		Assertions.assertFalse(card2.tops(card1));
	}

	@Test
	public void Test15_CardCopyClonesCard(){
		Card card = new Card(ValidCardColors.BLUE.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.GREEN.name());
		Card cardCopy = new Card(card);

		Assertions.assertEquals(card, cardCopy);
		Assertions.assertNotSame(card, cardCopy);
	}

}
