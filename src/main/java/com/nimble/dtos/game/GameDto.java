package com.nimble.dtos.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimble.model.game.Game;
import lombok.*;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GameDto {

	private ArrayList<PlayerDto> players;

	@JsonProperty("decks_board")
	private ArrayList<DeckDto> decksBoard;

	public GameDto(Game game) {
		if (game == null) {
			return;
		}
		players = new ArrayList<>();
		decksBoard = new ArrayList<>();
		for (int i = 0; i < game.totalPlayers(); i++) {
			players.add(PlayerDto.builder()
					.discardDeck(DeckDto.builder().topCard(new CardDto(game.getDiscardTop(i)))
							.size(game.getDiscardDeckSize(i)).build())
					.onHandsDeck(DeckDto.builder().topCard(null).size(game.getOnHandsDeckSize(i)).build())
					.handCard(new CardDto(game.getHandCard(i))).totalCards(game.getTotalCards(i)).build());
		}

		for (int i = 0; i < game.totalDecks(); i++) {
			decksBoard.add(DeckDto.builder().topCard(new CardDto(game.getDeckBoardTopCard(i))).size(null).build());
		}
	}

}
