package com.nimble.dtos.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimble.model.game.Deck;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeckDto {

	@JsonProperty("top_card")
	private CardDto topCard;

	private Integer size;

	public DeckDto(Deck deck) {
		if (deck == null) {
			return;
		}
		topCard = new CardDto(deck.peek());
		size = deck.size();
	}

}
