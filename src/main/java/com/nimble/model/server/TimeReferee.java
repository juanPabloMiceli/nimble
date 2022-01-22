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
	private final TimePenalties timePenalties = new TimePenalties();

	public TimeReferee(Clock clock, List<String> usersIds) {
		this.clock = clock;
		Instant now = clock.instant();
		usersIds.forEach(userId -> nextValidInstant.put(userId, now));
		System.out.println(timePenalties);
	}

	public void penalize(String userId, Duration duration) {
		if (!nextValidInstant.containsKey(userId)) {
			throw new UserDoesNotBelongToLobbyException(userId, ""); //TODO: Migrar a user not found
		}
		nextValidInstant.put(userId, clock.instant().plusMillis(duration.toMillis()));
	}
}
