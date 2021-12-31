package com.nimble.dtos.responses;

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

	private String lobbyId;

	private List<UserDto> names;

	public LobbyInfoResponse(List<UserDto> names, String lobbyId) {
		this.names = names;
		this.lobbyId = lobbyId;
		this.method = "list_players";
	}

}
