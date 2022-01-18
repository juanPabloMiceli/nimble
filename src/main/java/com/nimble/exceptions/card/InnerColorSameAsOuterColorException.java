package com.nimble.exceptions.card;

public class InnerColorSameAsOuterColorException extends RuntimeException {

	public InnerColorSameAsOuterColorException(String innerColor, String outerColor) {
		super(String.format("Fijate que inner (%s) es igual a outer (%s)!!!", innerColor, outerColor));
	}
}
