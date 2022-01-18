package com.nimble.dtos.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimble.model.game.Player;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlayerDto {

	@JsonProperty("on_hands_deck")
	private DeckDto onHandsDeck;

	@JsonProperty("discard_deck")
	private DeckDto discardDeck;

	@JsonProperty("total_cards")
	private Integer totalCards;

	public PlayerDto(Player player) {
		if (player == null) {
			return;
		}
		onHandsDeck = new DeckDto(null, player.getOnHandsDeckSize());
		discardDeck = new DeckDto(new CardDto(player.peekDiscardDeck()), player.getDiscardDeckSize());
		totalCards = player.totalCards();
	}
}
