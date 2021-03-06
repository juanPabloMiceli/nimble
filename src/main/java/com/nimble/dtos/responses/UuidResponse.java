package com.nimble.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UuidResponse {

	private String method;

	@JsonProperty("session_id")
	private String sessionId;

	public UuidResponse(String sessionId) {
		this.method = "uuid";
		this.sessionId = sessionId;
	}
}
