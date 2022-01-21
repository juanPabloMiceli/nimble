package com.nimble.dtos.responses;

import lombok.Getter;

@Getter
public class EnteringLobbyResponse {

	private final String method;

	public EnteringLobbyResponse() {
		this.method = "entering_lobby";
	}
}
