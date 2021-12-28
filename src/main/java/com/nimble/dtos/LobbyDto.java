package com.nimble.dtos;

import com.nimble.dtos.game.GameDto;
import com.nimble.dtos.game.UserDto;
import com.nimble.model.Lobby;
import com.nimble.model.game.Game;
import com.nimble.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LobbyDto {

	private List<UserDto> users;

	private String id;

	private GameDto game;

	public LobbyDto(Lobby lobby) {
		if (lobby == null) {
			return;
		}
		users = new ArrayList<>();
		lobby.getUsers().forEach(user -> this.users.add(new UserDto(user)));
		this.id = lobby.getId();
		this.game = new GameDto(lobby.getGame());

	}

}
