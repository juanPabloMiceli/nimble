package com.nimble.model.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "penalties")
public class TimePenalties {

	private Duration discardPenalty;
	private Duration wrongPlayPenalty;
	private Duration recoverPenalty;
	private Duration successfulPlayPenalty;

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
			discardPenalty +
			", wrongPlayPenalty=" +
			wrongPlayPenalty +
			", recoverPenalty=" +
			recoverPenalty +
			", successfulPlayPenalty=" +
			successfulPlayPenalty +
			'}'
		);
	}
}
