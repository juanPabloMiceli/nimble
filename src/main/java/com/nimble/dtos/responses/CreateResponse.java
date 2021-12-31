package com.nimble.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateResponse {

    private String method;

    private String lobbyId;

    public CreateResponse(String lobbyId) {
        this.lobbyId = lobbyId;
    }
}
