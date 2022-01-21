package com.nimble.dtos.responses.errors;

import lombok.Getter;

@Getter
public class CreateErrorResponse {

	private final String method;
	private final String description;

	public CreateErrorResponse() {
		this("");
	}

	public CreateErrorResponse(String description) {
		this.method = "create_error";
		this.description = description;
	}
}
