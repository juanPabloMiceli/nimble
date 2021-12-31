package com.nimble.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimble.dtos.game.UserDto;
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

	private String method;

	@JsonProperty("lobby_id")
	private String lobbyId;

	private List<UserDto> users;

	public LobbyInfoResponse(List<UserDto> users, String lobbyId) {
		this.users = users;
		this.lobbyId = lobbyId;
		this.method = "list_players";
	}

}
