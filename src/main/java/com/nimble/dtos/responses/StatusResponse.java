package com.nimble.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StatusResponse {

    private String method;

    private String status;

    private String description;

    public static StatusResponse SuccessfulResponse(String method){
        return new StatusResponse(method, "success", "" );
    }

    public static StatusResponse ErrorResponse(String method, String description){
        return new StatusResponse(method, "error", description);
    }
}
