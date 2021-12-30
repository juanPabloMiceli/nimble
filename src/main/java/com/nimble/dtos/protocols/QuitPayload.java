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
public class QuitPayload {

	private String method;

	@JsonProperty("session_id")
	private String sessionId;

}
