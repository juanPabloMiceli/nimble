package com.nimble.dtos.responses.status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusResponse {

	private String method;

	private String status;

	private String description;

	public StatusResponse(String status, String description) {
		this.status = status;
		this.description = description;
		this.method = "operation_status";
	}

}
