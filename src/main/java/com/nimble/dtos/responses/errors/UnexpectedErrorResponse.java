package com.nimble.dtos.responses.errors;

import lombok.Getter;

@Getter
public class UnexpectedErrorResponse {

	private final String method;
	private final String description;

	public UnexpectedErrorResponse() {
		this("");
	}

	public UnexpectedErrorResponse(String description) {
		this.method = "unexpected_error";
		this.description = description;
	}
}
