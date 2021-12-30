package com.nimble.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SessionDto {

	private String method;

	@JsonProperty("session_id")
	private String sessionId;

	public SessionDto(String sessionId) {
		this.method = "session_share";
		this.sessionId = sessionId;
	}

}
