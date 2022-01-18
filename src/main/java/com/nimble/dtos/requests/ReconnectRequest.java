package com.nimble.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReconnectRequest {

	private String method;

	@JsonProperty("session_id")
	private String sessionId;
}
