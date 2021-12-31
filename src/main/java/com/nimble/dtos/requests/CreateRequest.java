package com.nimble.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateRequest {

	private String method;

	@JsonProperty("session_id")
	private String sessionId;

	private String name;

}
