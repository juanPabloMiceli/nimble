package com.nimble.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetPenaltiesRequest {

	private String method;

	@JsonProperty("session_id")
	private String sessionId;

	@JsonProperty("discard_penalty")
	private long discardPenalty;

	@JsonProperty("wrong_play_penalty")
	private long wrongPlayPenalty;

	@JsonProperty("recover_penalty")
	private long recoverPenalty;

	@JsonProperty("successful_play_penalty")
	private long successfulPlayPenalty;
}
