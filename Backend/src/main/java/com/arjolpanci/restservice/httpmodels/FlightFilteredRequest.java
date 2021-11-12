package com.arjolpanci.restservice.httpmodels;

import lombok.Data;

import java.sql.Date;

@Data
public class FlightFilteredRequest {

    private String destinationSearchString;
    private String departureSearchString;
    private Date beforeDate;
    private Date afterDate;
    private IncomingPageRequest incomingPageRequest;

}
