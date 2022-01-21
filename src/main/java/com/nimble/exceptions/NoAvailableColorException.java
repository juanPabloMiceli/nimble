package com.nimble.exceptions;

public class NoAvailableColorException extends Exception {

	public NoAvailableColorException() {
		super("No hay mas colores disponibles!");
	}
}
