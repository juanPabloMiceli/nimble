package com.nimble.dtos.responses;

import com.nimble.dtos.game.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReconnectResponse {

	// TODO: Esto es necesario?

	private String method;

	private UserDto user;

	public ReconnectResponse(UserDto user) {
		this.method = "reconnected";
		this.user = user;
	}

}
