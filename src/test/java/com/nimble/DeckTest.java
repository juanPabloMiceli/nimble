package com.nimble;

import com.nimble.exceptions.card.InvalidBackCardColorException;
import com.nimble.exceptions.deck.EmptyDeckException;
import com.nimble.model.Card;
import com.nimble.model.Deck;
import com.nimble.model.enums.ValidPlayerColors;
import com.nimble.model.enums.ValidCardColors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

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

		List<String> innerColors = getInnerColors();
		List<String> outerColors = getOuterColors();

		List<Card> cards = getCards(innerColors, outerColors);

		Deck deck = new Deck(cards);
		int totalCards = deck.size();

		Assertions.assertEquals(totalCards, deck.size());
		assertDeckEqualsList(deck, innerColors, outerColors);
	}

	@Test
	public void Test05_PeekCardReturnsCardOnTopButDoesNotModifyDeck() {

		List<String> innerColors = getInnerColors();
		List<String> outerColors = getOuterColors();

		List<Card> cards = getCards(innerColors, outerColors);

		Deck deck = new Deck(cards);
		int totalCards = deck.size();

		Assertions.assertEquals(totalCards, deck.size());
		assertDeckEqualsList(deck, innerColors, outerColors);

	}

	@Test
	public void Test06_DrawCardReturnsCardOnTopAndRemovesTopCardsFromDeck() {

		List<String> innerColors = getInnerColors();
		List<String> outerColors = getOuterColors();

		List<Card> cards = getCards(innerColors, outerColors);

		Deck deck = new Deck(cards);
		int totalCards = deck.size();

		Card drawedCard = deck.draw();
		totalCards--;

		Assertions.assertEquals(totalCards, deck.size());
		assertCardEquals(drawedCard, innerColors.get(totalCards), outerColors.get(totalCards));
		innerColors.remove(totalCards);
		outerColors.remove(totalCards);

		assertDeckEqualsList(deck, innerColors, outerColors);

	}

	@Test
	public void Test07_AddingCardAddsItToTheTopOfTheDeck() {

		List<String> innerColors = getInnerColors();
		List<String> outerColors = getOuterColors();

		List<Card> cards = getCards(innerColors, outerColors);

		Card cardToAdd = cards.remove(cards.size()-1);
		Deck deck = new Deck(cards);
		int totalCards = deck.size();

		deck.add(cardToAdd);
		totalCards++;
		Assertions.assertEquals(totalCards, deck.size());
		assertDeckEqualsList(deck, innerColors, outerColors);

	}

	@Test
	public void Test8_StartingDeckSizeIs30() {
		Assertions.assertEquals(30, Deck.startingDeck().size());
	}

	@Test
	public void Test9_StartingDeckHasNoRepeatedCards() {

		Deck startingDeck = Deck.startingDeck();
		int deckSize = startingDeck.size();

		Assertions.assertEquals(deckSize, deckToSet(startingDeck).size());
	}

	/**
	 * Private methods
	 */

	private void assertCardEquals(Card card, String innerColor, String outerColor){
		Assertions.assertEquals(innerColor, card.getInnerColor());
		Assertions.assertEquals(outerColor, card.getOuterColor());
	}


	private void assertTopCard(Deck deck, String innerColor, String outerColor){
		Card card = deck.peek();
		assertCardEquals(card, innerColor, outerColor);
	}

	private void assertAndDrawTopCard(Deck deck, String innerColor, String outerColor){
		assertTopCard(deck, innerColor, outerColor);
		deck.draw();
	}

	private List<String> getInnerColors(){
		return Arrays.stream(ValidCardColors.values()).map(ValidCardColors::name).collect(Collectors.toList());
	}

	private List<String> getOuterColors(){
		List<String> colors = getInnerColors();
		colors.add(colors.remove(0));
		return colors;
	}

	private List<Card> getCards(List<String> innerColors, List<String> outerColors){
		List<Card> cards = new ArrayList<>();
		for(int i = 0; i < innerColors.size(); i++){
			cards.add(new Card(innerColors.get(i), outerColors.get(i)));
		}
		return cards;
	}

	private void assertDeckEqualsList(Deck deck, List<String> innerColors, List<String> outerColors){
		int n = deck.size();
		while(n > 0){
			n--;
			assertAndDrawTopCard(deck, innerColors.get(n), outerColors.get(n));
		}
	}

	private Set<Card> deckToSet(Deck deck){
		Set<Card> cards = new HashSet<>();
		while(!deck.isEmpty()){
			cards.add(deck.draw());
		}

		return cards;
	}

}
