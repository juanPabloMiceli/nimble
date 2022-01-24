package com.nimble.model.server;

import java.time.Duration;

public class TimePenalties {

	private Duration discardPenalty = Duration.ofMillis(250);
	private Duration wrongPlayPenalty = Duration.ofMillis(1000);
	private Duration recoverPenalty = Duration.ofMillis(500);
	private Duration successfulPlayPenalty = Duration.ofMillis(250);

	public Duration getDiscardPenalty() {
		return discardPenalty;
	}

	public void setDiscardPenalty(Duration discardPenalty) {
		this.discardPenalty = discardPenalty;
	}

	public Duration getWrongPlayPenalty() {
		return wrongPlayPenalty;
	}

	public void setWrongPlayPenalty(Duration wrongPlayPenalty) {
		this.wrongPlayPenalty = wrongPlayPenalty;
	}

	public Duration getRecoverPenalty() {
		return recoverPenalty;
	}

	public void setRecoverPenalty(Duration recoverPenalty) {
		this.recoverPenalty = recoverPenalty;
	}

	public Duration getSuccessfulPlayPenalty() {
		return successfulPlayPenalty;
	}

	public void setSuccessfulPlayPenalty(Duration successfulPlayPenalty) {
		this.successfulPlayPenalty = successfulPlayPenalty;
	}

	@Override
	public String toString() {
		return (
			"TimePenalties{" +
			"discardPenalty=" +
			discardPenalty.toMillis() +
			", wrongPlayPenalty=" +
			wrongPlayPenalty.toMillis() +
			", recoverPenalty=" +
			recoverPenalty.toMillis() +
			", successfulPlayPenalty=" +
			successfulPlayPenalty.toMillis() +
			'}'
		);
	}
}
