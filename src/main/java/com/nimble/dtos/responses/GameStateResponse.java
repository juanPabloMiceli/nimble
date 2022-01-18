package com.nimble.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimble.dtos.game.GameDto;
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
public class GameStateResponse {

	private String method;

	@JsonProperty("player_number")
	private int playerNumber;

	private List<UserDto> users;

	private GameDto game;

	public GameStateResponse(int playerNumber, List<UserDto> users, GameDto game) {
		this.method = "game_state";
		this.playerNumber = playerNumber;
		this.users = users;
		this.game = game;
	}
}
