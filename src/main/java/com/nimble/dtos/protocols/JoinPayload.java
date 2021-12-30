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

	private String id;

	private String name;

}
