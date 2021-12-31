package com.nimble.dtos.responses.errors;

public class UnexpectedErrorResponse extends StatusResponse {

	public UnexpectedErrorResponse() {
		super("error", "Error inesperado, refresca o contactate con el chiki :)");
	}

}
