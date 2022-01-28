package com.nimble.model.server;

import com.nimble.exceptions.lobby.UserDoesNotBelongToLobbyException;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeReferee {

	private final Clock clock;
	private final Map<String, Instant> nextValidInstant = new HashMap<>();
	private TimePenalties timePenalties = TimePenalties.builder().build();

	public TimeReferee(Clock clock, List<String> usersIds) {
		this.clock = clock;
		Instant now = clock.instant();
		usersIds.forEach(userId -> nextValidInstant.put(userId, now));
	}

	private void penalize(String userId, Duration duration) {
		if (!nextValidInstant.containsKey(userId)) {
			throw new UserDoesNotBelongToLobbyException(userId, ""); //TODO: Migrar a user not found
		}
		nextValidInstant.put(userId, clock.instant().plusMillis(duration.toMillis()));
	}

	public void discardPenalize(String userId) {
		penalize(userId, timePenalties.getDiscardPenalty());
	}

	public void recoverPenalize(String userId) {
		penalize(userId, timePenalties.getRecoverPenalty());
	}

	public void successfulPlayPenalize(String userId) {
		penalize(userId, timePenalties.getSuccessfulPlayPenalty());
	}

	public void wrongPlayPenalize(String userId) {
		penalize(userId, timePenalties.getWrongPlayPenalty());
	}

	public boolean isAvailable(String userId) {
		return clock.instant().isAfter(nextValidInstant.get(userId));
	}

	public TimePenalties getPenalties() {
		return timePenalties;
	}

	public void setPenalties(TimePenalties penalties) {
		this.timePenalties = penalties;
	}
}
