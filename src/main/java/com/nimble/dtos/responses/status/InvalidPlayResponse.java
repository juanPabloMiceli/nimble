package com.nimble.dtos.responses.status;

public class InvalidPlayResponse extends StatusResponse {

	public InvalidPlayResponse(String name) {
		super("error", "La cagaste con esa jugada " + name);
	}

}
