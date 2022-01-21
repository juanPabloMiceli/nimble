package com.nimble.dtos.responses.errors;

import lombok.Getter;

@Getter
public class InvalidMoveErrorResponse {

	private final String method;

	private final String description;

	public InvalidMoveErrorResponse(String description) {
		this.method = "invalid_move_error";
		this.description = description;
	}
}
