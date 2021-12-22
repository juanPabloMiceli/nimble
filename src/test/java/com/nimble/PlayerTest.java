package com.nimble;

import com.nimble.exceptions.Player.InvalidPlayerColorException;
import com.nimble.exceptions.Player.NameMustNotBeEmptyException;
import com.nimble.model.Card;
import com.nimble.model.Player;
import com.nimble.model.enums.ValidPlayerColors;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;


/**
 * {@link Player}
 */
@SpringBootTest
public class PlayerTest {

	@Test
	public void Test01_EmptyNameIsInvalid() {
		Assertions.assertThrows(NameMustNotBeEmptyException.class, () -> new Player("", ValidPlayerColors.BLUE.name()));
	}

	@Test
	public void Test02_PlayerColorMustBeValid() {
		Assertions.assertThrows(InvalidPlayerColorException.class, () -> new Player("name", "invalid color"));
	}

	@Test
	public void Test03_PlayerGetsChosenNameAndColor(){
		Player player = new Player("name", ValidPlayerColors.BLUE.name());
		Assertions.assertEquals("name", player.getName());
		Assertions.assertEquals(ValidPlayerColors.BLUE.name(), player.getColor());
	}

	@Test
	public void Test04_PlayerStartsWithEmptyDiscardDeck() {
		Player player = getPlayer();
		Assertions.assertEquals(0, player.getDiscardDeck().size());
	}

	@Test void Test05_PlayerStartsWithNoCardInHand(){
		Player player = getPlayer();
		Assertions.assertNull(player.getHandCard());
	}

	@Test
	public void Test06_PlayerStartsWithAllCardsInTheirHands() {
		Player player = getPlayer();
		Assertions.assertEquals(30, player.getOnHandsDeck().size());
	}

	@Test
	public void Test07_WhenOnHandsDeckIsNotEmptyDrawCardDecreasesSizeByOne(){
		Player player = getPlayer();

		int startingOnHandsSize = player.getOnHandsDeck().size();
		int i = 1;

		while(player.getOnHandsDeck().size() > 0){
			player.draw();
			Assertions.assertEquals(startingOnHandsSize-i, player.getOnHandsDeck().size());
			i++;
		}

		Assertions.assertEquals(31, i);
	}

//	@Test
//	public void Test08_WhenOnHandsDeckIsNotEmptyDrawCardMovesTopCardToHandAndFlipsItFacingUp() {
//		Player player = getPlayer();
//
//		Card topCard = new Card(player.getOnHandsDeck().peek());
//		topCard.flip();
//
//		player.draw();
//
//		Assertions.assertTrue(player.getHandCard().isVisible());
//		Assertions.assertEquals(topCard, player.getHandCard());
//	}

//	@Test
//	public void Test09_WhenThereIsMoreThanOneCardOnHandsDeckDrawCardLeavesTopCardFacingDown() {
//		Player player = getPlayer();
//
//		player.draw();
//
//		Assertions.assertFalse(player.getOnHandsDeck().peek().isVisible());
//	}

	@Test
	public void Test10_WhenThereIsNotOnHandCardDrawCardDoesNotChangeDiscardDeck() {
		Player player = getPlayer();

		ArrayList<Card> initialDiscardCards = new ArrayList<>(){};
		initialDiscardCards.addAll(player.getDiscardDeck().getCards());

		player.draw();

		Assertions.assertEquals(initialDiscardCards, player.getDiscardDeck().getCards());
	}

	@Test
	public void Test11_WhenThereIsNotOnHandCardAndOnHandsDeckIsEmptyDrawCardEmptiesDiscardDeck() {
		Player player = getPlayer();

		while(player.getOnHandsDeck().size() > 0){
			player.draw();
		}

		player.draw();

		Assertions.assertEquals(0, player.getDiscardDeck().size());
	}

	@Test
	public void Test12_WhenThereIsNotOnHandCardAndOnHandsDeckIsEmptyDrawCardEmptiesDiscardDeck() {
		Player player = getPlayer();

		while(player.getOnHandsDeck().size() > 0){
			player.draw();
		}

		player.draw();

		Assertions.assertEquals(0, player.getDiscardDeck().size());
	}

	@NotNull
	private Player getPlayer() {
		return new Player("name", ValidPlayerColors.BLUE.name());
	}

}
