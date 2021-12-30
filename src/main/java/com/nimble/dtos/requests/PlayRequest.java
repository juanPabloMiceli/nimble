package com.nimble.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimble.dtos.enums.PlayFrom;
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

	@JsonProperty("play_from")
	private PlayFrom playFrom;

	@JsonProperty("play_to")
	private int playTo;

}
