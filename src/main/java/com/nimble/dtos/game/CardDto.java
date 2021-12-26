package com.nimble.dtos.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimble.model.game.Card;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CardDto {

	@JsonProperty("inner_color")
	private String innerColor;

	@JsonProperty("outer_color")
	private String outerColor;

	public CardDto(Card card) {
		if (card == null) {
			return;
		}
		innerColor = card.getInnerColor();
		outerColor = card.getOuterColor();
	}

}
