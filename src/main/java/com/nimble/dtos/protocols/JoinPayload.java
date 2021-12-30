package com.nimble.dtos.protocols;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JoinPayload {

	private String method;

	@JsonProperty("lobby_id")
	private String lobbyId;

	@JsonProperty("session_id")
	private String sessionId;

	private String name;

}
