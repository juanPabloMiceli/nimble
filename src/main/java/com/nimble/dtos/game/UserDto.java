package com.nimble.dtos.game;

import com.nimble.model.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

	private String name;

	public UserDto(User user) {
		if (user == null) {
			return;
		}
		name = user.getName();
	}
}
