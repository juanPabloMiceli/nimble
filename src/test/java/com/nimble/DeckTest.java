package com.nimble;

import com.nimble.exceptions.card.InvalidBackCardColorException;
import com.nimble.exceptions.deck.EmptyDeckException;
import com.nimble.model.Card;
import com.nimble.model.Deck;
import com.nimble.model.enums.ValidPlayerColors;
import com.nimble.model.enums.ValidCardColors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * {@link Deck}
 */
@SpringBootTest
public class DeckTest {

	@Test
	public void Test01_NoArgsConstructorInitializesEmptyDeck() {
		Deck deck = new Deck();
		Assertions.assertEquals(0, deck.size());
	}

	@Test
	public void Test02_CanNotPeekCardFromEmptyDeck() {
		Deck deck = new Deck();
		Assertions.assertThrows(EmptyDeckException.class, deck::peek);
	}

	@Test
	public void Test03_CanNotDrawCardFromEmptyDeck() {
		Deck deck = new Deck();
		Assertions.assertThrows(EmptyDeckException.class, deck::draw);
	}

	@Test
	public void Test04_DeckInitializedWithCollectionHasCorrespondingSizeAndOrder() {
		Card card1 = new Card(ValidCardColors.YELLOW.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.RED.name());
		Card card2 = new Card(ValidCardColors.YELLOW.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.BLUE.name());
		Card card3 = new Card(ValidCardColors.YELLOW.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.GREEN.name());
		Card card4 = new Card(ValidCardColors.YELLOW.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.ORANGE.name());
		ArrayList<Card> cards = new ArrayList<>(List.of(card1, card2, card3, card4));

		Deck deck = new Deck(cards);

		Assertions.assertEquals(cards, deck.getCards());
		Assertions.assertEquals(4, deck.size());
	}

	@Test
	public void Test05_PeekCardReturnsCardOnTopButDoesNotModifyDeck() {
		Card card = new Card(ValidCardColors.YELLOW.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.RED.name());
		ArrayList<Card> cards = new ArrayList<>(List.of(card));
		Deck deck = new Deck(cards);

		Assertions.assertEquals(card, deck.peek());
		Assertions.assertEquals(cards, deck.getCards());
		Assertions.assertEquals(1, deck.size());

	}

	@Test
	public void Test06_DrawCardReturnsCardOnTopAndRemovesTopCardsFromDeck() {
		Card card1 = new Card(ValidCardColors.YELLOW.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.RED.name());
		Card card2 = new Card(ValidCardColors.YELLOW.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.BLUE.name());
		ArrayList<Card> cards = new ArrayList<>(List.of(card1, card2));
		Deck deck = new Deck(cards);

		Assertions.assertEquals(card2, deck.draw());
		Assertions.assertEquals(new ArrayList<>(List.of(card1)), deck.getCards());
		Assertions.assertEquals(1, deck.size());

	}

	@Test
	public void Test08_TopCardChangesAfterAddingNewCard() {
		Card card1 = new Card(ValidCardColors.YELLOW.name(), ValidCardColors.ORANGE.name(),
				ValidPlayerColors.RED.name());
		Card card2 = new Card(ValidCardColors.ORANGE.name(), ValidCardColors.RED.name(),
				ValidPlayerColors.RED.name());
		Card card3 = new Card(ValidCardColors.RED.name(), ValidCardColors.GREEN.name(),
				ValidPlayerColors.RED.name());
		ArrayList<Card> cards = new ArrayList<>(List.of(card1, card2));
		Deck deck = new Deck(cards);
		deck.add(card3);

		Assertions.assertEquals(new ArrayList<>(List.of(card1, card2, card3)), deck.getCards());
		Assertions.assertEquals(3, deck.size());

	}

	@Test
	public void Test09_StartingDeckMustHaveValidColor() {
		Assertions.assertThrows(InvalidBackCardColorException.class,
				() -> Deck.startingDeck("invalid color").size());
	}

	@Test
	public void Test10_StartingDeckSizeIs30() {
		Assertions.assertEquals(30, Deck.startingDeck(ValidPlayerColors.RED.name()).size());
	}

	@Test
	public void Test12_EveryBackColorInStartingDeckIsTheChosenOne() {
		Deck.startingDeck(ValidPlayerColors.RED.name()).getCards().forEach(
				(nimbleCard) -> Assertions.assertEquals(ValidPlayerColors.RED.name(), nimbleCard.getBackColor()));
	}

	@Test
	public void Test13_StartingDeckHasNoRepeatedCards() {

		Deck startingDeck = Deck.startingDeck(ValidPlayerColors.RED.name());
		Stack<Card> startingCards = startingDeck.getCards();

		Assertions.assertEquals(startingCards.size(), new HashSet<>(startingCards).size());
	}

}
