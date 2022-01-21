package com.nimble.dtos.responses.errors;

import lombok.Getter;

@Getter
public class JoinErrorResponse {

	private final String method;
	private final String description;

	public JoinErrorResponse(String description) {
		this.method = "join_error";
		this.description = description;
	}
}
