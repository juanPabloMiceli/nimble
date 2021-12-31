package com.nimble.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {

    private String method;
    private String description;

    public ErrorResponse(String description) {
        this.description = description;
        this.method = "error";
    }
}
