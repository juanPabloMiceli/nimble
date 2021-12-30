package com.nimble.dtos.protocols;

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
public class PlayPayload {

	private String method;

	private String id;

	@JsonProperty("play_from")
	private PlayFrom playFrom;

	@JsonProperty("play_to")
	private int playTo;

}
