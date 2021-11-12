package com.arjolpanci.restservice.util;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseManager {

    public <T> ResponseObject<T> getFormattedResponse(HttpStatus statusCode, String message, T data) {
        return new ResponseObject<T>(statusCode, message, data);
    }

    @Data
    public class ResponseObject<T> {
        private HttpStatus statusCode;
        private String message;
        private T data;

        public ResponseObject(HttpStatus statusCode, String message, T data) {
            this.statusCode = statusCode;
            this.message = message;
            this.data = data;
        }
    }


}
