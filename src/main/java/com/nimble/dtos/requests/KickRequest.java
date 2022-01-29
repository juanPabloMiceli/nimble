package com.nimble.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KickRequest {

    private String method;

    @JsonProperty("session_id")
    private String sessionId;

    private int kicked;
}
