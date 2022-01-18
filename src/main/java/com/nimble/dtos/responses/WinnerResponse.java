package com.nimble.dtos.responses;

import com.nimble.dtos.game.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WinnerResponse {

	private String method;

	private UserDto user;

	public WinnerResponse(UserDto userDto) {
		this.method = "winner";
		this.user = userDto;
	}
}
