package com.nimble.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateRequest {

	private String method;

	@JsonProperty("lobby_id")
	private String lobbyId;// TODO: Autogenerar y devolver al usuario

	@JsonProperty("session_id")
	private String sessionId;

	private String name;

}
