package com.nimble.dtos.game;

import com.nimble.model.server.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

	private String name;

	private String color;

	public UserDto(User user) {
		if (user == null) {
			return;
		}
		name = user.getName();
		color =
			String.format("#%02x%02x%02x", user.getColor().getRed(), user.getColor().getGreen(), user.getColor().getBlue());
	}
}
