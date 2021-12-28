package com.nimble;

import com.nimble.model.game.Card;
import com.nimble.model.game.Deck;
import com.nimble.model.game.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * {@link Player}
 */
@SpringBootTest
public class PlayerTest {

	@Mock
	Deck deck = Mockito.mock(Deck.class);

	@Test
	public void Test01_PlayerStartsWithEmptyDiscardDeck() {
		Player player = getPlayer();
		Assertions.assertEquals(0, player.getDiscardDeckSize());
	}

	@Test
	void Test02_PlayerStartsWith30Cards() {
		Player player = getPlayer();
		Assertions.assertEquals(30, player.totalCards());
	}

	@Test
	public void Test03_PlayerStartsWith29CardsInOnHandsDeck() {
		Player player = getPlayer();
		Assertions.assertEquals(29, player.getOnHandsDeckSize());
	}

	@Test
	public void Test04_WhenOnHandsDeckIsNotEmptyDiscardReducesItSizeByOneAndIncreasesDiscardDeckSizeByOne() {
		Player player = getPlayer();

		int onHandsDeckSize = player.getOnHandsDeckSize();
		int discardDeckSize = player.getDiscardDeckSize();
		while (onHandsDeckSize > 0) {
			player.discard();
			Assertions.assertEquals(onHandsDeckSize - 1, player.getOnHandsDeckSize());
			Assertions.assertEquals(discardDeckSize + 1, player.getDiscardDeckSize());
			onHandsDeckSize--;
			discardDeckSize++;
		}
	}

	@Test
	public void Test05_WhenOnHandsDeckIsEmptyDiscardDeckPassesToOnHandsDeckAndFirstCardGoesToHandAgain() {
		Player player = getPlayer();

		Card firstCard = player.getHandCard();
		String innerColor = firstCard.getInnerColor();
		String outerColor = firstCard.getOuterColor();

		while (player.getOnHandsDeckSize() > 0) {
			player.discard();
		}
		player.discard();

		Assertions.assertEquals(0, player.getDiscardDeckSize());
		Assertions.assertEquals(innerColor, player.getHandCard().getInnerColor());
		Assertions.assertEquals(outerColor, player.getHandCard().getOuterColor());
	}

	@Test
	public void Test06_AfterSuccessfulPlayFromHandPlayerHasOneCardLessAndOnHandsDeckReducesSizeByOne() {
		Player player = getPlayer();

		int startingCards = player.totalCards();
		int startingOnHandsCards = player.getOnHandsDeckSize();

		Mockito.when(deck.canplay(player.getHandCard())).thenReturn(true);
		player.playHandCard(deck);

		Assertions.assertEquals(startingCards - 1, player.totalCards());
		Assertions.assertEquals(startingOnHandsCards - 1, player.getOnHandsDeckSize());
	}

	@Test
	public void Test07_AfterUnsuccessfulPlayFromHandPlayerSameAmountOfCards() {
		Player player = getPlayer();
		int startingCards = player.totalCards();

		Mockito.when(deck.canplay(player.getHandCard())).thenReturn(false);
		player.playHandCard(deck);

		Assertions.assertEquals(startingCards, player.totalCards());
	}

	@Test
	public void Test08_AfterSuccessfulPlayFromDiscardPlayerHasOneCardLessAndDiscardDeckReducesSizeByOne() {
		Player player = getPlayer();

		player.discard();
		int startingCards = player.totalCards();
		int startingDiscardCards = player.getDiscardDeckSize();

		Mockito.when(deck.canplay(player.peekDiscardDeck())).thenReturn(true);
		player.playDiscardCard(deck);

		Assertions.assertEquals(startingCards - 1, player.totalCards());
		Assertions.assertEquals(startingDiscardCards - 1, player.getDiscardDeckSize());
	}

	@Test
	public void Test09_AfterUnsuccessfulPlayFromDiscardPlayerHasSameAmountOfCards() {
		Player player = getPlayer();
		int startingCards = player.totalCards();

		Mockito.when(deck.canplay(player.getHandCard())).thenReturn(false);
		player.playHandCard(deck);

		Assertions.assertEquals(startingCards, player.totalCards());
	}

	@NotNull
	private Player getPlayer() {
		return new Player();
	}

}
