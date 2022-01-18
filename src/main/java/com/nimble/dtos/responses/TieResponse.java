package com.nimble.dtos.responses;

import lombok.Getter;

@Getter
public class TieResponse {

	private String method;

	public TieResponse() {
		this.method = "tie";
	}
}
