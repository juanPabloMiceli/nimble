package com.nimble.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListPlayersResponse {

	private String method;

	private List<String> names;

	public ListPlayersResponse(List<String> names) {
		this.names = names;
		this.method = "list_players";
	}

}
