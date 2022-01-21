package com.nimble.dtos.responses.errors;

import lombok.Getter;

@Getter
public class StartErrorResponse {

	private final String method;
	private final String description;

	public StartErrorResponse(String description) {
		this.method = "start_error";
		this.description = description;
	}

	public StartErrorResponse() {
		this("");
	}
}
