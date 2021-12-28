package com.nimble.dtos.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PlayFrom {

	DISCARD("discard"), HAND("hand");

	private final String value;

	PlayFrom(String value) {
		this.value = value;
	}

	@JsonValue
	public String value() {
		return value;
	}

}
