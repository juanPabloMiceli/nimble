package com.nimble;

import com.nimble.model.Card;
import com.nimble.model.Deck;
import com.nimble.model.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;


/**
 * {@link Player}
 */
@SpringBootTest
public class PlayerTest {

    @Mock
    Deck centerDeck = Mockito.mock(Deck.class);

	@Test
	public void Test01_PlayerStartsWithEmptyDiscardDeck() {
		Player player = getPlayer();
		Assertions.assertEquals(0, player.getDiscardDeckSize());
	}

	@Test void Test05_PlayerStartsWithNoCardInHand(){
		Player player = getPlayer();
		Assertions.assertFalse(player.hasCardOnHand());
	}

	@Test
	public void Test06_PlayerStartsWithAllCardsInHisHands() {
		Player player = getPlayer();
		Assertions.assertEquals(30, player.getOnHandsDeckSize());
	}

//	@Test
//	public void Test07_WhenOnHandsDeckIsNotEmptyDrawTakesTopCardAndPutIsInPlayerHand(){
//		Player player = getPlayer();
//
//		player.draw();
//	}
//
    @Test
    public void TestXX_AfterSuccessfulPlayFromHandPlayerHasOneCardLess(){
        Player player = getPlayer();
        int startingCards = player.getTotalCards();
        player.draw();

        Mockito.when(centerDeck.canplay(player.getHandCard())).thenReturn(true);
        player.playHandCard(centerDeck);

        Assertions.assertEquals(startingCards-1, player.getTotalCards());
    }

	@Test
	public void TestXX_AfterUnsuccessfulPlayFromHandPlayerSameAmountOfCards(){
		Player player = getPlayer();
		int startingCards = player.getTotalCards();
		player.draw();

		Mockito.when(centerDeck.canplay(player.getHandCard())).thenReturn(false);
		player.playHandCard(centerDeck);

		Assertions.assertEquals(startingCards, player.getTotalCards());
	}


	@NotNull
	private Player getPlayer() {
		return new Player();
	}

}
