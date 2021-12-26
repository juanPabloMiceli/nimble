package com.nimble.dtos.protocols;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreatePayload {

	private String method;

	@JsonProperty("lobby_id")
	private String lobbyId;

	private String name;

}
