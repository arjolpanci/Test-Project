package com.arjolpanci.restservice.httpmodels;

import lombok.Data;

@Data
public class NewFlightRequest {

    private Long userId;
    private Long flightId;
    private String status;
    private String rejection_reason;

}
