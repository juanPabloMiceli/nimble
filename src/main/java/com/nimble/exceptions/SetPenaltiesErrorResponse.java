package com.nimble.exceptions;

public class SetPenaltiesErrorResponse extends Exception {

	public SetPenaltiesErrorResponse(String error) {
		super("No se pudieron actualizar los penalties: " + error);
	}
}
