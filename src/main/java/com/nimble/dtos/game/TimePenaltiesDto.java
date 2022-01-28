package com.nimble.dtos.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimble.model.server.TimePenalties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimePenaltiesDto {

	@JsonProperty("discard_penalty")
	private long discardPenalty;

	@JsonProperty("wrong_play_penalty")
	private long wrongPlayPenalty;

	@JsonProperty("recover_penalty")
	private long recoverPenalty;

	@JsonProperty("successful_play_penalty")
	private long successfulPlayPenalty;

	public TimePenaltiesDto(TimePenalties penalties) {
		if (penalties == null) {
			return;
		}
		discardPenalty = penalties.getDiscardPenalty().toMillis();
		wrongPlayPenalty = penalties.getWrongPlayPenalty().toMillis();
		recoverPenalty = penalties.getRecoverPenalty().toMillis();
		successfulPlayPenalty = penalties.getSuccessfulPlayPenalty().toMillis();
	}
}
