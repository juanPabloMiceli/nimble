package com.nimble.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimble.dtos.game.TimePenaltiesDto;
import com.nimble.dtos.game.UserDto;
import com.nimble.model.server.TimePenalties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LobbyInfoResponse {

	private final String method = "lobby_info";

	private boolean owner;

	@JsonProperty("lobby_id")
	private String lobbyId;

	private List<UserDto> users;

	private TimePenaltiesDto timePenalties;

	public LobbyInfoResponse(boolean owner, List<UserDto> users, String lobbyId, TimePenalties timePenalties) {
		this.owner = owner;
		this.users = users;
		this.lobbyId = lobbyId;
		this.timePenalties = new TimePenaltiesDto(timePenalties);
	}
}
