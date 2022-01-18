package com.nimble.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PlayRequest {

	private String method;

	@JsonProperty("session_id")
	private String sessionId;

	@JsonProperty("play_to")
	private int playTo;
}
