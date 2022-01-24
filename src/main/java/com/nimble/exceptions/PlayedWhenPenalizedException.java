package com.nimble.exceptions;

public class PlayedWhenPenalizedException extends Exception {

	public PlayedWhenPenalizedException(String userId) {
		super(userId + " trato de jugar pero estaba penalizado!");
	}
}
