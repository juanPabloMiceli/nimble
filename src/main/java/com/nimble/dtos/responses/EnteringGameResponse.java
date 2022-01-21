package com.nimble.dtos.responses;

import lombok.Getter;

@Getter
public class EnteringGameResponse {

	private final String method;

	public EnteringGameResponse() {
		this.method = "entering_game";
	}
}
