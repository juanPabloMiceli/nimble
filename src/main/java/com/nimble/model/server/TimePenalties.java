package com.nimble.model.server;

import lombok.*;

import java.time.Duration;

@Getter
@Setter
@Builder
public class TimePenalties {

	@Builder.Default
	private Duration discardPenalty = Duration.ofMillis(250);

	@Builder.Default
	private Duration wrongPlayPenalty = Duration.ofMillis(1000);

	@Builder.Default
	private Duration recoverPenalty = Duration.ofMillis(500);

	@Builder.Default
	private Duration successfulPlayPenalty = Duration.ofMillis(250);

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
