package com.nimble.dtos.responses.errors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

	private String method;

	private String message;

	public ErrorResponse(String message) {
		this.message = message;
		this.method = "error";
	}

}
