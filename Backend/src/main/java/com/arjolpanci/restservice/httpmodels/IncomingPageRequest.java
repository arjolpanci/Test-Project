package com.arjolpanci.restservice.httpmodels;

import lombok.Data;

@Data
public class IncomingPageRequest {
    private int pageSize;
    private int pageNumber;
}
